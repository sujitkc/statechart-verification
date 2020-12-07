package ast;

import visitor.ExpressionVisitor;

public class UnaryExpression extends Expression {

  public enum Operator {
    ADD,
    MUL,
    SUB,
    DIV,
    GE,
    GT,
    LE,
    LT,
    EQ,
    NE,
    AND,
    OR,
    UMIN,
    NOT
  };

  public final Expression expression;
  public final Operator operator;

  public UnaryExpression(Expression expression, Operator operator) {
    this.expression = expression;
    this.operator   = operator;
  }

  public void visit (ExpressionVisitor visitor) throws Exception {
	  visitor.visitUnaryExpression(this);
  }
}
