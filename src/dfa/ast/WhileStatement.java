package ast;

import java.util.List;

import visitor.Visitor;

import java.util.ArrayList;

public class WhileStatement extends Statement {
  public final Expression condition;
  public final Statement body;

  public WhileStatement(Expression condition, Statement body) {
    this.condition = condition;
    this.body      = body;
  }

  public Statement getFirstStatement() {
    return this;
  }

  public List<Statement> getLastStatements() {
    List<Statement> last = new ArrayList<Statement>();
    last.add(this);
    return last;
  }

  public String toString() {
    return "\nwhile(" + this.condition + ")" + this.body;
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitWhileStatement(this);
  }
}
