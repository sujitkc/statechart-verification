package ast;
import java.util.*;

public class UnaryExpression extends Expression {

  public final Expression expression;
  public final int operator;
  public List<Expression> variables;

  public UnaryExpression(Expression expression, int operator) {
    this.expression = expression;
    this.operator   = operator;
    this.variables=new ArrayList<Expression>();
    
  }
  public List<Expression> getVariables(){
	this.variables.add(this.expression);
	return this.variables;
  }
}
