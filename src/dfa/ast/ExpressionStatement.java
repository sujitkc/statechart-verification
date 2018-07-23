package ast;

public class ExpressionStatement extends Statement {
  public final Expression expression;

  public ExpressionStatement(Expression expression) {
    this.expression = expression;
  }
  public String toString() {
    return (this.expression.toString() + ";\n");
  }
}
