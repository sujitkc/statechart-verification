package ast;

public class UnaryExpression extends Expression {

  public final Expression expression;
  public final int operator;

  public UnaryExpression(Expression expression, int operator) {
    this.expression = expression;
    this.operator   = operator;
  }
}
