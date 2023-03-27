package simulator2.cfg;

import java.util.Set;

import ast.*;

public class CFGDecisionNode extends CFGNode {
  public final Expression condition;
  public CFGNode thenSuccessor;
  public CFGNode elseSuccessor;

  public CFGDecisionNode(Expression condition, CFGNode thenSuccessor, CFGNode elseSuccessor) {
    this.condition     = condition;
    this.thenSuccessor = thenSuccessor;
    this.elseSuccessor = elseSuccessor;
  }

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
