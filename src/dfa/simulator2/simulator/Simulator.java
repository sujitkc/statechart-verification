package simulator2.simulator;

import java.util.Set;
import java.util.HashSet;

import ast.*;

import simulator2.tree.*;

public class Simulator {

  public final Statechart statechart;
  public final Tree<State> stateTree;

  public Simulator(Statechart statechart) throws Exception {
    this.statechart = statechart;
    this.stateTree = this.getStateTree(this.statechart);
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
}
