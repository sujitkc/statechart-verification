package ast;
import program.IProgram;

public class UnaryExpression extends Expression {

  public final Expression expression;
  public final int operator;
  public UnaryExpression(IProgram program, Expression expression, Type type,int operator){
	this.expression = expression;
	 this.operator   = operator;
  }
  public UnaryExpression(Expression expression, int operator) {
    this.expression = expression;
    this.operator   = operator;
  }
}
