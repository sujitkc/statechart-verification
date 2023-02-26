package simulator2.simulator;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

import ast.*;

import simulator2.*;
import simulator2.tree.*;
import simulator2.cfg.*;
import simulator2.code.*;

public class Simulator {

  public final Statechart statechart;
  private final Set<Transition> allTransitions;
  private final Map<Declaration, Expression> valueEnvironment;
  public final Tree<State> stateTree;
  public final Map<Statement, CFG> CFGs = new HashMap<>();
  private final ASTToCFG converter = new ASTToCFG();
  private Set<State> configuration;

  public Simulator(Statechart statechart) throws Exception {
    this(statechart, new HashSet<State>());
  }

  public Simulator(Statechart statechart, Set<State> configuration) throws Exception {
    this.statechart = statechart;
    this.allTransitions = this.getAllTransitions();
    this.valueEnvironment = this.makeValueEnvironment();
    this.stateTree = this.getStateTree(this.statechart);
    this.makeCFGs(this.statechart);
    this.configuration = configuration;
  }

  public void simulationStep(String event) throws Exception {
  /*
   * Compute the set of all enabled transitions
   * Check for non-determinism. Abort if found.
   * For each enabled transition, compute code
   * Compute complete code to be executed as the concurrent composition
   *   of transition-wise code.
   * while, there's code to execute, keep single-stepping
  */
    Set<Transition> enabledTransitions = this.getEnabledTransitions(event);
    Set<State> newConfiguration = new HashSet<>();
    for(Transition t : enabledTransitions) {
      System.out.println("Enabled transition: " + t.name);
      Set<State> atomicStates = this.getEntrySubTree(t.getDestination())
                                    .getLeafNodes();
      newConfiguration.addAll(atomicStates);
    }
    if(newConfiguration.isEmpty() == false) {
      this.configuration = newConfiguration;
    }
    for(State s : this.configuration) {
      System.out.println("New state : " + s.name);
    }
  }

  private Map<Declaration, Expression> makeValueEnvironment() {
    Map<Declaration, Expression> environment = new HashMap<>();
    Set<Declaration> declarations = this.getAllDeclarations();
    for(Declaration declaration : declarations) {
      environment.put(declaration, null);
    }
    return environment;
  }

  private Set<Declaration> getAllDeclarations() {
    Set<Declaration> declarations = new HashSet<>();
    Queue<State> queue = new LinkedList<>();
    queue.add(this.statechart);
    while(queue.isEmpty() == false) {
      State state = queue.remove();
      declarations.addAll(state.declarations);
      queue.addAll(state.states);
    }
    return declarations;
  }

  // Gives the set of all transitions inside of state in a recursive way.
  // useful in constructing and storing the set of transitions for fast access
  // later on.
  private Set<Transition> getAllTransitions() {
    Set<Transition> transitions = new HashSet<>();
    Queue<State> queue = new LinkedList<>();
    queue.add(this.statechart);
    while(queue.isEmpty() == false) {
      State state = queue.remove();
      transitions.addAll(state.transitions);
      queue.addAll(state.states);
    }
    return transitions;
  }

  // Recursively look for a state by the given name. 
  // Return the first match. Null on no match.
  public State getSubstateByName(String name, State state) {
    List<State> substates = state.states;
    for(State s : substates) {
      if(s.name.equals(name)) {
        return s;
      }
      State ss = this.getSubstateByName(name, s);
      if(ss != null) {
        return ss;
      }
    }
    return null;
  }

  // Generates a tree of states.
  // Useful in later plucking subtrees out of the main tree and
  // creating code fragments out of it. 
  private Tree<State> getStateTree(State state) throws Exception {
    Set<Tree<State>> trees = new HashSet<>();
    for(State child : state.states) {
      trees.add(this.getStateTree(child));
    }
    Tree<State> myStateTree = new Tree<State>(state);
    for(Tree<State> subtree : trees) {
      myStateTree.addSubtree(state, subtree);
    }
    return myStateTree;
  }

  private void makeCFGs(State state) throws Exception {
    this.CFGs.put(state.entry, this.converter.convert(state.entry));
    this.CFGs.put(state.exit, this.converter.convert(state.exit));
    for(Transition t : state.transitions) {
      this.CFGs.put(t.action, this.converter.convert(t.action));
    }
    for(State s : state.states) {
      this.makeCFGs(s);
    }
  }

  private Set<Transition> getEnabledTransitions(String event) throws Exception {
    Set<Transition> eTransitions = new HashSet<>();
    for(Transition t : this.allTransitions) {
      if(t.trigger.equals(event)) {
        eTransitions.add(t);
      }
    }
    Tree<State> slicedStateTree = this.stateTree.getSlicedSubtree(
      this.stateTree.root, this.configuration);
    Set<Transition> enabledTransitions = new HashSet<>();
    Set<State> allSourceStates = slicedStateTree.getAllNodes();
    for(Transition t : eTransitions) {
      if(allSourceStates.contains(t.getSource())) {
        enabledTransitions.add(t);
      }	
    }
    return enabledTransitions;
  }

  private Code getSourceCode(Tree<CFG> tree) throws Exception {
    return this.getCode(tree);
  }

  private Code getDestinationCode(Tree<CFG> tree) throws Exception {
    Code rcode = this.getCode(tree);
    return rcode.reverse();
  }

  private Code getCode(Tree<CFG> tree) throws Exception {
    return this.getCode(tree.root, tree);
  }

  private Code getCode(CFG cfg, Tree<CFG> tree) throws Exception {
    Set<CFG> childCFGs = tree.getChildren(cfg);
    List<CFG> childCFGList = new ArrayList<>(childCFGs);
    Code myCode = new CFGCode(cfg);
    if(childCFGs.size() == 0) {
      return myCode;
    }
    else if(childCFGs.size() == 1) { // sequence code
      Code childCode = this.getCode(childCFGList.get(0), tree);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
    else { // concurrent code
      Set<Code> childCodes = new HashSet<>();
      for(CFG child : childCFGs) {
	childCodes.add(this.getCode(child, tree));
      }
      Code childCode = new ConcurrentCode(childCodes);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
  }

  private Code getSourceCode(Transition t) throws Exception {

    // Source side state tree - begin
    Set<State> atomicStates = new HashSet<>();
    for(State atomicState : this.configuration) {
      if(this.stateTree.getAllAncestors(atomicState).contains(t.getSource())) {
        atomicStates.add(atomicState);
      }
    }
    Tree<State> subtree = this.stateTree.getSlicedSubtree(t.getSource(), atomicStates);

    State lub = this.stateTree.lub(t.getSource(), t.getDestination());
    List<State> sourceAncestors = this.stateTree.getAllAncestorsUpto(t.getSource(), lub);
    sourceAncestors.remove(sourceAncestors.size() - 1); // removing t.source.
    Tree<State> sourceStateTree = new Tree<>(sourceAncestors.get(0));
    sourceStateTree.addPath(sourceAncestors);
    State currentLeaf = sourceAncestors.get(sourceAncestors.size() - 1);
    sourceStateTree.addSubtree(currentLeaf, subtree);
    // Source side state tree - end

    // Source side CFG tree - begin
    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.exit);
        }
      },
      sourceStateTree);
    // Source side CFG tree - end

    return this.getSourceCode(CFGTree);
  }

  private Tree<State> getEntrySubTree(State state) throws Exception {
    Tree<State> tree = new Tree<State>(state);
    if(state.states.isEmpty() == true) {
      return tree;
    }
    else if(state instanceof Shell) {
      for(State s : state.states) {
        tree.addSubtree(state, this.getEntrySubTree(s));
      }
      return tree;
    }
    else {
      tree.addSubtree(state, this.getEntrySubTree(state.states.get(0)));
      return tree;
    }
  }

  private Code getDestinationCode(Transition t) throws Exception {
    Tree<State> subtree = this.getEntrySubTree(t.getDestination());

    State lub = this.stateTree.lub(t.getSource(), t.getDestination());
    List<State> destinationAncestors = this.stateTree.getAllAncestorsUpto(t.getDestination(), lub);
    destinationAncestors.remove(destinationAncestors.size() - 1); // removing t.destination.
    Tree<State> destinationStateTree = new Tree<>(destinationAncestors.get(0));
    destinationStateTree.addPath(destinationAncestors);
    State currentLeaf = destinationAncestors.get(destinationAncestors.size() - 1);
    destinationStateTree.addSubtree(currentLeaf, subtree);

    // Destination side CFG tree - begin
    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.entry);
        }
      },
      destinationStateTree);
    // Destination side CFG tree - end

    return this.getDestinationCode(CFGTree);
  }

  private Code getCode(Transition t) throws Exception {
    Code sourceCode = this.getSourceCode(t);
    Code actionCode = new CFGCode(this.CFGs.get(t.action));
    Code destinationCode = this.getDestinationCode(t);
    Code[] codes = {sourceCode, actionCode, destinationCode};
    SequenceCode code = new SequenceCode(Arrays.asList(codes));
    return code;
  }
}
