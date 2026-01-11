package simulator2;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import ast.*;
import simulator2.cfg.*;

public class ASTToCFG {
  /*
  private final Statement statement;

  public ASTToCFG(Statement statement) {
    this.statement = statement;
  }
  public CFG convert() throws Exception {
    return this.convert(this.statement);
  }
  */
  public String actionname="";
  public CFG convert(Statement s) throws Exception {
    if(s instanceof AssignmentStatement) {
     
      return this.convertAssignmentStatement((AssignmentStatement) s);
    }
    else if(s instanceof IfStatement) {
      return this.convertIfStatement((IfStatement) s);
    }
    else if(s instanceof WhileStatement) {
      return this.convertWhileStatement((WhileStatement) s);
    }
  
    else if(s instanceof StatementList) {
    
      return this.convertStatementList((StatementList) s);
    }

    else {
      throw new Exception("ASTToCFG.convert: Not implemented for this class - " + s.getClass() + s);
    }
  }

  private CFG convertAssignmentStatement(AssignmentStatement s) throws Exception {
    CFGBasicBlockNode node = new CFGAssignmentNode(s);
    return new CFG(actionname, node, node);
  }

  private CFG convertIfStatement(IfStatement s) throws Exception {
    CFG thenCFG = this.convert(s.then_body);
    CFG elseCFG = this.convert(s.else_body);
    CFGNode node = new CFGDecisionNode(s.condition, thenCFG.entryNode, elseCFG.entryNode);
    CFGBasicBlockNode exitNode = new CFGSkipNode();
    thenCFG.exitNode.setSuccessor(exitNode);
    elseCFG.exitNode.setSuccessor(exitNode);
    return new CFG(actionname, node, exitNode);
  }

  private CFG convertWhileStatement(WhileStatement s) throws Exception {
    CFG bodyCFG = this.convert(s.body);
    CFGBasicBlockNode exitNode = new CFGSkipNode();
    CFGNode node = new CFGDecisionNode(s.condition, bodyCFG.entryNode, exitNode);
    bodyCFG.exitNode.setSuccessor(node);
    return new CFG(actionname, node, exitNode);
  }

  private CFG convertStatementList(StatementList s) throws Exception {
    if(s.getStatements().size() == 0) {
      CFGBasicBlockNode node = new CFGSkipNode();
      return new CFG(actionname, node, node);
      
    }
    List<CFG> cfgs = new ArrayList<>();
    for(Statement statement : s.getStatements()) {
    	
      cfgs.add(this.convert(statement));
    }
   // System.out.println("cfgs :" + cfgs);
    for(int i = 0; i < cfgs.size() - 1; i++) {
      CFG cfg1 = cfgs.get(i);
      CFG cfg2 = cfgs.get(i + 1);
      cfg1.exitNode.setSuccessor(cfg2.entryNode);
    }

    return new CFG(actionname, cfgs.get(0).entryNode, cfgs.get(cfgs.size() - 1).exitNode);
  }
}
