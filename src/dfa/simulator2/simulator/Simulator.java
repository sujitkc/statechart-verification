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
  public final Tree<State> stateTree;
  public final Map<Statement, CFG> CFGs = new HashMap<>();
  private final ASTToCFG converter = new ASTToCFG();
  public Simulator(Statechart statechart) throws Exception {
    this.statechart = statechart;
    this.stateTree = this.getStateTree(this.statechart);
    this.makeCFGs(this.statechart);
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

  private Code getSourceCode(Tree<CFG> tree) {
    return this.getSourceCode(tree.root, tree);
  }

  private Code getSourceCode(CFG cfg, Tree<CFG> tree) {
    Set<CFG> childCFGs = tree.getChildren(cfg);
    List<CFG> childCFGList = new ArrayList<>(childCFGs);
    Code myCode = new CFGCode(cfg);
    if(childCFGs.size() == 0) {
      return myCode;
    }
    else if(childCFGs.size() == 1) {
      Code childCode = this.getSourceCode(childCFGList.get(0), tree);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
    else {
      Set<Code> childCodes = new HashSet<>();
      for(CFG child : childCFGs) {
	childCodes.add(this.getSourceCode(child, tree));
      }
      Code childCode = new ConcurrentCode(childCodes);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
  }

  private Code getDestinationCode(Tree<CFG> tree) {
    return null;
  }
}
