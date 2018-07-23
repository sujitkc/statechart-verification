package cfg;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import ast.*;

public class CFGBasicBlock extends CFGNode{
  private List<CFGNode> child;
  private Statement s;

  public CFGBasicBlock(Statement s){
    this.s=s;
    child=new ArrayList<CFGNode>();
  }

  public List<CFGNode> getChildNode(){
    return child;
  }

  public void setChildNode(CFGNode n){
    child.add(n);
    n.setParentNode(this);
    n.setSuccessorNode(this.getSuccessorNode());
  }

  public String getDetailsbb(){
    return "SUCCESSOR BLOCK: "+s.toString();
  }

  public String toStringbb(){
    String det="BASIC BLOCK: "+s.toString()+"\n";
    if(this.getSuccessorNode()!=null)
      det+=getSuccessorNode().getDetails()+"\n";
    for(CFGNode i: child)
      det+=i.toString();
    return det;
  }
}
