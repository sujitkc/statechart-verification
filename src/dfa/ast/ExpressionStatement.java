package ast;

public class ExpressionStatement extends InstructionStatement {
  public final Expression expression;

  public ExpressionStatement(Expression expression) {
    this.expression = expression;
  }

  public String toString() {
    return (this.expression.toString() + ";\n");
  }
}
