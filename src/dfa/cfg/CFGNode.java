package cfg;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import ast.*;

public class CFGNode{
  private CFGNode successor, parent;

  public CFGNode getParentNode(){
    return parent;
  }

  public CFGNode getSuccessorNode(){
    return successor;
  }

  public void setSuccessorNode(CFGNode n){
    successor=n;
    if(this instanceof CFGBasicBlock){
      CFGBasicBlock bb=(CFGBasicBlock)this;
      List<CFGNode>cn=bb.getChildNode();
      if(cn.size()!=0)
        for(CFGNode i: cn)
          if(i.getSuccessorNode()==null)
            i.setSuccessorNode(n);
    }
    else if(this instanceof CFGDecisionBlock){
      CFGDecisionBlock db=(CFGDecisionBlock)this;
      List<CFGNode>cn=db.getThenNode();
      if(cn.size()>0)
        for(CFGNode i: cn)
          if(i.getSuccessorNode()==null)
            i.setSuccessorNode(n);
      cn=db.getElseNode();
      if(cn.size()>0)
        for(CFGNode i: cn)
          if(i.getSuccessorNode()==null)
            i.setSuccessorNode(n);
    }
  }

  public void setParentNode(CFGNode n){
    parent=n;
  }

  public String toString(){
    String det="";
    if(this instanceof CFGBasicBlock){
      CFGBasicBlock bb=(CFGBasicBlock)this;
      det+=bb.toStringbb();
    }
    else{
      CFGDecisionBlock db=(CFGDecisionBlock)this;
      det+=db.toStringdb();
    }
    return det;
  }

  public String getDetails(){
    String det="";
    if(this instanceof CFGBasicBlock){
      CFGBasicBlock bb=(CFGBasicBlock)this;
      det+=bb.getDetailsbb();
    }
    else{
      CFGDecisionBlock db=(CFGDecisionBlock)this;
      det+=db.getDetailsdb();
    }
    if(successor!=null) return det+this.getSuccessorNode().toString();
    else return det;
  }
}
