package cfg;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import ast.*;

public class CFGDecisionBlock extends CFGNode{
  private Expression condition;
  private List<CFGNode> thenNode, elseNode;

  public CFGDecisionBlock(Expression condition){
    this.condition=condition;
    thenNode=new ArrayList<CFGNode>();
    elseNode=new ArrayList<CFGNode>();
  }

  public Expression getCondition(){
    return condition;
  }

  public void setThenNode(CFGNode n){
    thenNode.add(n);
    n.setParentNode(this);
    n.setSuccessorNode(this.getSuccessorNode());
  }

  public void setElseNode(CFGNode n){
    elseNode.add(n);
    n.setParentNode(this);
    n.setSuccessorNode(this.getSuccessorNode());
  }

  public List<CFGNode> getThenNode(){
    return thenNode;
  }

  public List<CFGNode> getElseNode(){
    return elseNode;
  }

  public String getDetailsdb(){
    return "SUCCESSOR BLOCK: "+condition.toString();
  }

  public String toStringdb(){
    String det="DECISION BLOCK: "+condition.toString()+"\n";
    if(this.getSuccessorNode()!=null){
      det+=getSuccessorNode().getDetails()+"\n";
    }
    det+="\nTHEN BLOCK: ";
    for(CFGNode i: thenNode)
      det+=i.toString()+"\n";
    if(elseNode.size()!=0){
      det+="\nELSE BLOCK: ";
      for(CFGNode i: elseNode)
        det+=i.toString()+"\n";
    }
    return det;
  }
}
