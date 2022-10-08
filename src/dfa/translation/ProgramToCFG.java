package translation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import program.Program;
import ast.*;
import stablcfg.*;
import utilities.Pair;
import utilities.prettyprint.CFGToDot;

public class ProgramToCFG {

  private Map<Statement, CFGNode> nodeMap = new HashMap<Statement, CFGNode>();
  private Map<Statement, Statement> successors = new HashMap<Statement, Statement>();
//  private Map<Statement, Pair<Statement, Statement>> decisionSuccessors = new HashMap<Statement, Pair<Statement, Statement>>();
  private CFG theCFG;

  public CFG translate(Program program) throws Exception {

    /* cleanup from possible earlier run -- begin */
    this.nodeMap.clear();
    this.successors.clear();
    this.theCFG = null;
    /* cleanup from possible earlier run -- end */

    this.makeNodeMap(program);
    for(Statement statement : this.nodeMap.keySet()) {
      System.out.println("****\n" + statement + "\n--->>\n" + this.nodeMap.get(statement) + "\n****\n");
    }

    this.theCFG = new CFG(
      program.name,
      program.declarations,
      program.types,
      program.functionDeclarations,
      new CFGBasicBlockNode(new SkipStatement()),
      new CFGBasicBlockNode(new HaltStatement()));

    for(Statement statement : this.nodeMap.keySet()) {
      CFGNode node = this.nodeMap.get(statement);
      if(node instanceof CFGBasicBlockNode) {
        theCFG.addBasicBlockNode((CFGBasicBlockNode)node);
      }
      else {
        theCFG.addDecisionNode((CFGDecisionNode)node);
      }
    }

    this.addSuccessor(program);
    this.addCFEdge(program);

    CFGToDot cfgToDot = new CFGToDot();
    String dot = cfgToDot.toDot(this.theCFG);
    try {
      PrintWriter out = new PrintWriter("cfg.dot");
      out.print(dot);
      out.close();
    }
    catch(FileNotFoundException e) {
      System.out.println("Couldn't find file cfg.dot.");
    }

    return this.theCFG;
  }

  private void makeNodeMap(Program program) throws Exception {

    for(Statement s : program.statements.getStatements()) {
      this.addNodeMapping(s);
    }
  }

  private void addNodeMapping(Statement s) throws Exception {
    if(s instanceof InstructionStatement) {
      this.addNodeMappingInstruction((InstructionStatement)s);
    }
    else if (s instanceof IfStatement) {
      this.addNodeMappingIf((IfStatement)s);
    }
    else if(s instanceof WhileStatement) {
      this.addNodeMappingWhile((WhileStatement)s);
    }
    else if(s instanceof StatementList) {
      this.addNodeMappingStatementList((StatementList)s);
    }
    else {
      throw new Exception("ProgramToCFG.addNodeMapping : Unknown statement type" + s.getClass().getName() + ".");
    }
  }

  private void addNodeMappingInstruction(InstructionStatement statement) {
    this.nodeMap.put(statement, new CFGBasicBlockNode(statement));
  }

  private void addNodeMappingIf(IfStatement statement) throws Exception {
    this.nodeMap.put(statement, new CFGDecisionNode(statement.condition));
    this.addNodeMapping(statement.then_body);
    this.addNodeMapping(statement.else_body);
  }

  private void addNodeMappingWhile(WhileStatement statement) throws Exception {
    this.nodeMap.put(statement, new CFGDecisionNode(statement.condition));
    this.addNodeMapping(statement.body);
  }

  private void addNodeMappingStatementList(StatementList slist) throws Exception {
    for(Statement statement : slist.getStatements()) {
      this.addNodeMapping(statement);
    }
  }

  private void addSuccessor(Program program) throws Exception {
    this.addSuccessor(program.statements);
  }

  private void addSuccessor(Statement statement) throws Exception {
    if(statement instanceof StatementList) {
      this.addSuccessorStatementList((StatementList)statement);
    }
    else if(statement instanceof IfStatement) {
      this.addSuccessorIfStatement((IfStatement)statement);
    }
    else if(statement instanceof WhileStatement) {
      this.addSuccessorWhileStatement((WhileStatement)statement);
    }
    else {
      throw new Exception("ProgramToCFG.addSuccessor: statement type not handled.");
    }
  }

  private void addSuccessorIfStatement(IfStatement ifStatement) throws Exception {
    this.successors.put(ifStatement.then_body, this.successors.get(ifStatement));
    if((ifStatement.then_body instanceof InstructionStatement) == false) {
      this.addSuccessor(ifStatement.then_body);
    }
    this.successors.put(ifStatement.else_body, this.successors.get(ifStatement));
    if((ifStatement.else_body instanceof InstructionStatement) == false) {
      this.addSuccessor(ifStatement.else_body);
    }
  }

  private void addSuccessorWhileStatement(WhileStatement whileStatement) throws Exception {
    this.successors.put(whileStatement.body, whileStatement);
    if((whileStatement.body instanceof InstructionStatement) == false) {
      this.addSuccessor(whileStatement.body);
    }
  }

  private void addSuccessorStatementList(StatementList statementList) throws Exception {
    List<Statement> list = statementList.getStatements();
    if(list.isEmpty()) {
      return;
    }
    for(int i = 0; i < list.size() - 1; i++) {
      Statement s1 = list.get(i);
      Statement s2 = list.get(i + 1);
      this.successors.put(s1, s2);
      if((s1 instanceof InstructionStatement) == false) {
        this.addSuccessor(s1);
      }
    }
    Statement last = list.get(list.size() - 1);
    Statement listSuccessor = this.successors.get(statementList);
    this.successors.put(last, listSuccessor); 
    if((last instanceof InstructionStatement) == false) {
      this.addSuccessor(last);
    }
  }

  private void addCFEdge(Program program) throws Exception {
    this.addCFEdge(program.statements);
    this.theCFG.addEdge(new CFEdge(this.theCFG.getStartNode(), this.nodeMap.get(program.statements.getFirstStatement())));
    List<Statement> lastStatements = program.statements.getLastStatements();
    for(Statement lastStatement : lastStatements) {
      this.theCFG.addEdge(new CFEdge(this.nodeMap.get(lastStatement), this.theCFG.getStopNode()));
    }
  }

  private void addCFEdge(Statement statement) throws Exception {
    if(statement instanceof StatementList) {
      this.addCFEdgeStatementList((StatementList)statement);
    }
    else if(statement instanceof InstructionStatement) {
      this.addCFEdgeInstruction((InstructionStatement)statement);
    }
    else if(statement instanceof IfStatement) {
      this.addCFEdgeIfStatement((IfStatement)statement);
    }
    else if(statement instanceof WhileStatement) {
      this.addCFEdgeWhileStatement((WhileStatement)statement);
    }
    else {
      throw new Exception("ProgramToCFG.addCFEdge: statement type not handled.");
    }
  }

  private void addCFEdgeStatementList(StatementList statement) throws Exception {
    for(Statement s : statement.getStatements()) {
      this.addCFEdge(s);
    }
  }

  private void addCFEdgeInstruction(InstructionStatement statement) throws Exception {
    CFGNode node = this.nodeMap.get(statement);
    CFGNode successor = this.nodeMap.get(this.successors.get(statement));
    this.theCFG.addEdge(new CFEdge(node, successor));
  }

  private void addCFEdgeIfStatement(IfStatement statement) throws Exception {
    CFGDecisionNode node = (CFGDecisionNode)this.nodeMap.get((Statement)statement);
    CFGNode thenNode = this.nodeMap.get(statement.then_body.getFirstStatement());
    CFGNode elseNode = this.nodeMap.get(statement.else_body.getFirstStatement());
    CFEdge thenEdge = new CFEdge(node, thenNode);
    CFEdge elseEdge = new CFEdge(node, elseNode);
    this.theCFG.addEdge(thenEdge);
    this.theCFG.addEdge(elseEdge);
    this.addCFEdge(statement.then_body);
    this.addCFEdge(statement.else_body);
  }

  private void addCFEdgeWhileStatement(WhileStatement statement) throws Exception {
    CFGDecisionNode node = (CFGDecisionNode)this.nodeMap.get((Statement)statement);
    CFGNode bodyNode = this.nodeMap.get(statement.body.getFirstStatement());
    CFGNode nextNode = this.nodeMap.get(this.successors.get(statement).getFirstStatement());
    CFEdge thenEdge = new CFEdge(node, bodyNode);
    CFEdge elseEdge = new CFEdge(node, nextNode);
    this.theCFG.addEdge(thenEdge);
    this.theCFG.addEdge(elseEdge);
    this.addCFEdge(statement.body);
  }
}
