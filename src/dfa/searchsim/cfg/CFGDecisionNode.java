package searchsim.cfg;

import java.util.Set;
import java.util.HashSet;

import ast.*;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
import searchsim.simulator.ActionLanguageInterpreter;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CFGDecisionNode extends CFGNode {
  public final Expression condition;
  public CFGNode thenSuccessor;
  public CFGNode elseSuccessor;

  public CFGDecisionNode(Expression condition, CFGNode thenSuccessor, CFGNode elseSuccessor) {
    this.condition     = condition;
    this.thenSuccessor = thenSuccessor;
    this.elseSuccessor = elseSuccessor;
  }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public Set<Declaration> getReadSet() {
    Set<Declaration> readSet = new HashSet<>();
    // read from condition
    Set<Declaration> condDeps = ActionLanguageInterpreter.getDependentVarSet(condition);
    if (condDeps != null) {
      readSet.addAll(condDeps);
    }
    return readSet;
  }

  @Override
  public Set<Declaration> getWriteSet() {
    // DecisionNode has no writes
    return new HashSet<>();
  }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
  protected void setCFG(CFG cfg, Set<CFGNode> added) {
    if(added.contains(this) == false) {
      this.cfg = cfg;
      added.add(this);
      if(this.thenSuccessor != null) {
        this.thenSuccessor.setCFG(cfg, added);
      }
      if(this.elseSuccessor != null) {
        this.elseSuccessor.setCFG(cfg, added);
      }
    }
  }

  public String toString() {
    String s = this.cfg.name+" : "+ "IfNode\n";
    s += "\tCondition = " + this.condition;
    s += "\tThen statement = " + this.thenSuccessor;
    s += "\tElse statement = " + this.elseSuccessor;
    return s;
  }
}
