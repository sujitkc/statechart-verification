package simulator2.cfg;

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
}
