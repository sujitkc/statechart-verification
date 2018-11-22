package ast;
import java.util.*;
import program.IProgram;
import visitors.IExprVisitor;
public class UnaryExpression extends Expression {

  public final Expression expression;
  public final int operator;
  public List<Expression> variables;


  public UnaryExpression(IProgram prog,Type t){
	super(prog,t);
	this.expression=null;
	this.operator=0;
}
  public UnaryExpression(Expression expression, int operator) {
    super(null,null);
    this.expression = expression;
    this.operator   = operator;
    this.variables=new ArrayList<Expression>();
    
  }
  public List<Expression> getVariables(){
	this.variables.add(this.expression);
	return this.variables;
  }
  @Override
	public void accept(IExprVisitor<?> visitor) throws Exception {
		visitor.visit(this.expression);
	}
}
