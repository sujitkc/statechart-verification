package simulator2.simulator;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import ast.*;

import simulator2.*;
import simulator2.tree.*;
import simulator2.cfg.*;
import simulator2.code.*;

public class Simulator {

  public final Statechart statechart;
  private final Set<Transition> allTransitions;
  public final Tree<State> stateTree;
  public final Map<Statement, CFG> CFGs = new HashMap<>();
  private final ASTToCFG converter = new ASTToCFG();
  private Set<State> configuration;

  public Simulator(Statechart statechart) throws Exception {
    this.statechart = statechart;
    this.allTransitions = this.getAllTransitions(this.statechart);
    this.stateTree = this.getStateTree(this.statechart);
    this.makeCFGs(this.statechart);
  }

  private void simulationStep() {
  /*
   * Compute the set of all enabled transitions
   * Check for non-determinism. Abort if found.
   * For each enabled transition, compute code
   * Compute complete code to be executed as the concurrent composition
   *   of transition-wise code.
   * while, there's code to execute, keep single-stepping
  */
  }

  private Set<Transition> getAllTransitions(State state) {
    Set<Set<Transition>> allTransitionSets = new HashSet<>();
    for(State childState : state.states) {
      allTransitionSets.add(this.getAllTransitions(childState));
    }
    Set<Transition> allTransitions = new HashSet<>();
    allTransitions.addAll(state.transitions);
    for(Set<Transition> transitions : allTransitionSets) {
      allTransitions.addAll(transitions);
    }
    return allTransitions;
  }
 
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
    return this.getSourceCode(tree.root, tree);
  }

  private Code getSourceCode(CFG cfg, Tree<CFG> tree) throws Exception {
    Set<CFG> childCFGs = tree.getChildren(cfg);
    List<CFG> childCFGList = new ArrayList<>(childCFGs);
    Code myCode = new CFGCode(cfg);
    if(childCFGs.size() == 0) {
      return myCode;
    }
    else if(childCFGs.size() == 1) { // sequence code
      Code childCode = this.getSourceCode(childCFGList.get(0), tree);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
    else { // concurrent code
      Set<Code> childCodes = new HashSet<>();
      for(CFG child : childCFGs) {
	childCodes.add(this.getSourceCode(child, tree));
      }
      Code childCode = new ConcurrentCode(childCodes);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
  }

  private Code getSourceCode(Transition t) throws Exception {

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

  private Code getDestinationCode(Tree<CFG> tree) {
    return null;
  }
}
