package ast;
import java.util.*;

public class UnaryExpression extends Expression {

  public final Expression expression;
  public final int operator;
  public List<Expression> variables;

  public UnaryExpression(Expression expression, int operator) {
    this.expression = expression;
    this.operator   = operator;
    variables=new ArrayList<Expression>();
    variables.add(expression);
  }
  public List<Expression> getVariables(){
  
	return this.variables;
  }
}
