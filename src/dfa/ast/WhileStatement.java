package ast;

public class WhileStatement extends Statement {
  public final Expression condition;
  public final Statement body;

  public WhileStatement(Expression condition, Statement body) {
    this.condition = condition;
    this.body      = body;
  }

  public String toString() {
    return "\nwhile(" + this.condition + ")" + this.body;
  }
}
