package ast;

import java.util.List;

import visitor.Visitor;

public class IfStatement extends Statement {
  public final Expression condition;
  public final Statement then_body;
  public final Statement else_body;

  public IfStatement(Expression condition, Statement then_body, Statement else_body) {
    this.condition = condition;
    this.then_body = then_body;
    this.else_body = else_body;
  }

  public Statement getFirstStatement() {
    return this;
  }

  public List<Statement> getLastStatements() {
    List<Statement> thenLastStatements = then_body.getLastStatements();
    List<Statement> elseLastStatements = else_body.getLastStatements();

    thenLastStatements.addAll(elseLastStatements);
    return thenLastStatements;
  }

  public String toString() {
    return "\nif(" + this.condition + ")" + this.then_body + "\nelse\n" + this.else_body;
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitIfStatement(this);
  }
}
