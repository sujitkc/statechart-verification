package stablcfg;

import ast.Expression;

public class CFGDecisionNode extends CFGNode {
  public final Expression condition;
  private CFEdge thenEdge;
  private CFEdge elseEdge;

  public CFGDecisionNode(Expression cond){
    this.condition=cond;
  }

  public Expression getCondition(){
    return this.condition;
  }

  public CFEdge getThenEdge(){
    return this.thenEdge;
  }

  public CFEdge getElseEdge(){
    return this.elseEdge;
  }

  public CFGNode getThenSuccessorNode(){
    return this.thenEdge.head;
  }

  public CFGNode getElseSuccessorNode(){
    return this.elseEdge.head;
  }

  public void setThenEdge(CFEdge edge) throws Exception {
    if(this.equals(edge.getTail()) == false) {
      throw new Exception("CFGDecisionNode.setThenEdge : the edge source doesn't match.");
    }
    this.thenEdge=edge;
  }

  public void setElseEdge(CFEdge edge) throws Exception {
    if(this.equals(edge.getTail()) == false) {
      throw new Exception("CFGDecisionNode.setElseEdge : the edge source doesn't match.");
    }
    this.elseEdge=edge;
  }
}
