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

  public void simulate(List<String> events) throws Exception {

    this.configuration =  this.getEntrySubTree(this.statechart).getLeafNodes();
    Tree<State> subtree = this.getEntrySubTree(statechart);
    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.entry);
        }
      },
      subtree);

    Code code = this.getDestinationCode(CFGTree);
    CodeSimulator codeSimulator = new CodeSimulator(code, this.valueEnvironment);
    codeSimulator.simulate();

    for(String event : events) {
      this.simulationStep(event);
    }
  }

  public Set<State> simulationStep(String event) throws Exception {
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
    Code code = null;
    System.out.print("Enabled Transitions :");
    if(enabledTransitions.size() > 1) {
      Set<Code> codes = new HashSet<>();
      
      for(Transition t: enabledTransitions) {
      	 System.out.print(t.name+",");
        codes.add(this.getCode(t));
      }
      System.out.println();
      this.detectNondeterminism(codes);
      code = new ConcurrentCode(codes);
    }
    else if(enabledTransitions.size() == 1) {
      List<Transition> tlist = new ArrayList<>(enabledTransitions);
      Transition t = tlist.get(0);
      System.out.println(t.name+",");
      code = this.getCode(t);
    }
    else {
      System.out.println("No transition enabled.");
      return newConfiguration;
    }
    CodeSimulator codeSimulator = new CodeSimulator(code, this.valueEnvironment);
    codeSimulator.simulate();
    System.out.println("Value environment");
    for(Declaration d : this.valueEnvironment.keySet()) {
      System.out.println(d + " : " + this.valueEnvironment.get(d));
    }

    
    for(Transition t : enabledTransitions) {
      Set<State> atomicStates = this.getDestinationTree(t).getLeafNodes();
      newConfiguration.addAll(atomicStates);
    }
    
    if(newConfiguration.isEmpty() == false) {
      this.configuration = newConfiguration;
    }
    System.out.print("States in configuration : {");
    for(State s : this.configuration) {
      System.out.print(s.name+", ");
    }
    System.out.println("}");
    return newConfiguration;
  }

  private void detectNondeterminism(Set<Code> codes) throws Exception {
    Set<CFG> cfgs = new HashSet<>();
    for(Code code : codes) {
      Set<CFG> codeCFGs = this.getAllCFGsinCode(code);
      Set<CFG> intersect = new HashSet<>(cfgs);
      intersect.retainAll(codeCFGs);
      if(intersect.isEmpty()) {
        cfgs.addAll(codeCFGs);
      }
      else {
        throw new Exception("Simulator::detectNondeterminism : Non-determinism detected.");
      }
    }
  }

  private Set<CFG> getAllCFGsinCode(Code code) throws Exception {
    Set<CFG> cfgs = new HashSet<>();
    if(code instanceof CFGCode) {
      CFGCode cfgCode = (CFGCode)code;
      cfgs.add(cfgCode.cfg);
    }
    else if(code instanceof SequenceCode) {
      SequenceCode sequenceCode = (SequenceCode)code;
      for(Code c : sequenceCode.codes) {
        cfgs.addAll(this.getAllCFGsinCode(c));
      }
    }
    else if(code instanceof ConcurrentCode) {
      ConcurrentCode concurrentCode = (ConcurrentCode)code;
      for(Code c : concurrentCode.codes) {
        cfgs.addAll(this.getAllCFGsinCode(c));
      }
    }
    else {
      throw new Exception("Simulator::getAllCFGsinCode - Not implemented.");
    }
    return cfgs;
  }

  private Map<Declaration, Expression> makeValueEnvironment() {
    Map<Declaration, Expression> environment = new HashMap<>();
    Set<Declaration> declarations = this.getAllDeclarations();
    Expression defaultValue = null;
    for(Declaration declaration : declarations) {
      if(declaration.typeName.equals("int")) {
        defaultValue = new IntegerConstant(0);
      }
      else if(declaration.typeName.equals("bool")) {
        defaultValue = new BooleanConstant(true);
      }
      environment.put(declaration, defaultValue);
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
    /* action name is added by Karthika */
    this.converter.actionname=state.name+"_N";
    this.CFGs.put(state.entry, this.converter.convert(state.entry));
    this.converter.actionname=state.name+"_X";
    this.CFGs.put(state.exit, this.converter.convert(state.exit));
    for(Transition t : state.transitions) {
      this.converter.actionname=t.name+"_A";
      this.CFGs.put(t.action, this.converter.convert(t.action));
    }
    for(State s : state.states) {
      this.makeCFGs(s);
    }
  }

  private Set<Transition> getEnabledTransitions(String event)
      throws Exception {
    Set<Transition> eTransitions = new HashSet<>();
    for(Transition t : this.allTransitions) {
      BooleanConstant evaluatedGuard =
        (BooleanConstant)ActionLanguageInterpreter
	  .evaluate(t.guard, this.valueEnvironment);
      if(
          t.trigger.equals(event) &&
	  evaluatedGuard.equals(BooleanConstant.True))
      {
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

  private Set<State> getActiveAtomicSubstates(State state) throws Exception {
    Set<State> atomicStates = new HashSet<>();
    for(State atomicState : this.configuration) {
      if(this.stateTree.getAllAncestors(atomicState).contains(state)) {
        atomicStates.add(atomicState);
      }
    }
    return atomicStates;
  }

  private Tree<State> getSourceTree(Transition t) throws Exception {
    State lub = this.stateTree.lub(t.getSource(), t.getDestination());
    List<State> sourceAncestors = this.stateTree.getAllAncestorsUpto(t.getSource(), lub);
    Tree<State> sourceStateTree = null;
    if(sourceAncestors.size() > 1) {
      sourceAncestors.remove(sourceAncestors.size() - 1); // removing t.source.
      Shell shellAncestor = null;
      for(State ancestor : sourceAncestors) {
	if(ancestor instanceof Shell) {
          shellAncestor = (Shell)ancestor;
	  break;
	}
      }
      if(shellAncestor != null) {
        Set<State> atomicStates = this.getActiveAtomicSubstates(shellAncestor);
	Tree<State> subtree = this.stateTree.getSlicedSubtree(shellAncestor, atomicStates);
        List<State> higherAncestors = this.stateTree.getAllAncestorsUpto(shellAncestor, lub);
	if(higherAncestors.size() > 1) {
	  higherAncestors.remove(higherAncestors.size() - 1); // removing shell ancestor.
          sourceStateTree = new Tree<State>(higherAncestors.get(0));
          sourceStateTree.addPath(higherAncestors);
          State currentLeaf = higherAncestors.get(higherAncestors.size() - 1);
          sourceStateTree.addSubtree(currentLeaf, subtree);
	}
	else {
          sourceStateTree =subtree;
	}
      }
      else {
        Set<State> atomicStates = this.getActiveAtomicSubstates(t.getSource());
	Tree<State> subtree = this.stateTree.getSlicedSubtree(t.getSource(), atomicStates);
        sourceStateTree = new Tree<State>(sourceAncestors.get(0));
        sourceStateTree.addPath(sourceAncestors);
        State currentLeaf = sourceAncestors.get(sourceAncestors.size() - 1);
        sourceStateTree.addSubtree(currentLeaf, subtree);
      }
    }
    else {
      Set<State> atomicStates = this.getActiveAtomicSubstates(t.getSource());
      sourceStateTree = this.stateTree.getSlicedSubtree(t.getSource(), atomicStates);
    }
    return sourceStateTree;
  }

  private Code getSourceCode(Transition t) throws Exception {

    Tree<State> sourceStateTree = this.getSourceTree(t);
    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.exit);
        }
      },
      sourceStateTree);

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

  private Tree getDestinationTree(Transition t) throws Exception{
    Tree<State> destinationStateTree = null;
    State lub = this.stateTree.lub(t.getSource(), t.getDestination());
    List<State> destinationAncestors = this.stateTree.getAllAncestorsUpto(t.getDestination(), lub);
    if(destinationAncestors.size() > 1) {
      destinationAncestors.remove(destinationAncestors.size() - 1); // removing t.destination.
      Shell shellAncestor = null;
      for(State ancestor : destinationAncestors) {
	if(ancestor instanceof Shell) {
          shellAncestor = (Shell)ancestor;
	  break;
	}
      }
      if(shellAncestor != null) {
        Tree<State> subtree = this.getEntrySubTree(shellAncestor);
        List<State> higherAncestors = this.stateTree.getAllAncestorsUpto(shellAncestor, lub);
	if(higherAncestors.size() > 1) {
	  higherAncestors.remove(higherAncestors.size() - 1); // removing shell ancestor.
          destinationStateTree = new Tree<State>(higherAncestors.get(0));
          destinationStateTree.addPath(higherAncestors);
          State currentLeaf = higherAncestors.get(higherAncestors.size() - 1);
          destinationStateTree.addSubtree(currentLeaf, subtree);
	}
	else {
          destinationStateTree = subtree;
	}
      }
      else {
        Tree<State> subtree = this.getEntrySubTree(t.getDestination());
        destinationStateTree = new Tree<State>(destinationAncestors.get(0));
        destinationStateTree.addPath(destinationAncestors);
        State currentLeaf = destinationAncestors.get(destinationAncestors.size() - 1);
        destinationStateTree.addSubtree(currentLeaf, subtree);
      }
    }
    else {
      destinationStateTree = this.getEntrySubTree(t.getDestination());
    }

    return destinationStateTree;
  }

  private Code getDestinationCode(Transition t) throws Exception {
    Tree<State> destinationStateTree = this.getDestinationTree(t);

    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.entry);
        }
      },
      destinationStateTree);

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
