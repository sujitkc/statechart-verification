package cfg;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import ast.*;

public class CFG{
  private List<Statement>sl;
  private CFGBasicBlock head, tail;

  public CFG(StatementList s){
    sl=s.getStatements();
    ExpressionStatement temp=new ExpressionStatement(new StringLiteral("BEGIN"));
    head=new CFGBasicBlock(temp);
    temp=new ExpressionStatement(new StringLiteral("END"));
    tail=new CFGBasicBlock(temp);
    head.setSuccessorNode(tail);
  }

  public CFGBasicBlock CFGcreator(){
    Statement stmt=sl.get(0);
    CFGNode t1=makeCFG(stmt);
    for(int i=1; i<sl.size(); i++){
      stmt=sl.get(i);
      CFGNode t2=makeCFG(stmt);
      t1.setSuccessorNode(t2);
      t1=t2;
      head.setChildNode(t1);
    }
    t1.setSuccessorNode(tail);
    return head;
  }

  public CFGNode makeCFG(Statement st){
    if(st instanceof IfStatement){
      IfStatement is=(IfStatement)st;
      CFGDecisionBlock db=new CFGDecisionBlock(is.condition);

      if(is.then_body instanceof StatementList){
        StatementList sls=(StatementList)is.then_body;
        List<Statement>slist=sls.getStatements();
        CFGNode n1=makeCFG(slist.get(0)), n2;
        db.setThenNode(n1);
        for(int i=1; i<slist.size(); i++){
          n2=makeCFG(slist.get(i));
          n1.setSuccessorNode(n2);
          db.setThenNode(n2);
          n1=n2;
        }
        n1.setSuccessorNode(db.getSuccessorNode());
      }
      else{
        CFGNode n1=makeCFG(is.then_body);
        db.setThenNode(n1);
        n1.setSuccessorNode(db.getSuccessorNode());
      }

      if(is.else_body!=null){
        if(is.else_body instanceof StatementList){
          StatementList sls=(StatementList)is.then_body;
          List<Statement>slist=sls.getStatements();
          CFGNode n1=makeCFG(slist.get(0)), n2;
          db.setElseNode(n1);
          for(int i=1; i<slist.size(); i++){
            n2=makeCFG(slist.get(i));
            n1.setSuccessorNode(n2);
            db.setElseNode(n2);
            n1=n2;
          }
          n1.setSuccessorNode(db.getSuccessorNode());
        }
        else{
          CFGNode n1=makeCFG(is.else_body);
          db.setElseNode(n1);
          n1.setSuccessorNode(db.getSuccessorNode());
        }
      }
      return db;
    }
    else if(st instanceof WhileStatement){
      WhileStatement ws=(WhileStatement)st;
      CFGDecisionBlock db=new CFGDecisionBlock(ws.condition);

      if(ws.body instanceof StatementList){
        StatementList sls=(StatementList)ws.body;
        List<Statement>slist=sls.getStatements();
        CFGNode n1=makeCFG(slist.get(0)), n2;
        db.setThenNode(n1);
        for(int i=1; i<slist.size(); i++){
          n2=makeCFG(slist.get(i));
          n1.setSuccessorNode(n2);
          db.setThenNode(n2);
          n1=n2;
        }
        n1.setSuccessorNode(db.getSuccessorNode());
      }
      else{
        CFGNode n1=makeCFG(ws.body);
        db.setThenNode(n1);
        n1.setSuccessorNode(db.getSuccessorNode());
      }
      return db;
    }
    else /*if(st instanceof Statement)*/{
      CFGBasicBlock bb=new CFGBasicBlock(st);
      return bb;
    }
  }
}
