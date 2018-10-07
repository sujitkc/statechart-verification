package ast;
import java.util.*;
public class BinaryExpression extends Expression {

  public final Expression left;
  public final Expression right;
  public List<Expression> variables;
  public final String operator;

  public BinaryExpression(Expression left, Expression right, String operator) {
    this.left     = left;
    this.right    = right;
    this.operator = operator;
    variables=new ArrayList<Expression>();
    if(left instanceof IntegerConstant || left instanceof StringLiteral){}
    else variables.add(left);
    if(right instanceof IntegerConstant || right instanceof StringLiteral){}
    else variables.add(right);
    
  }

  public String toString() {
    String s = this.left.toString() + " " + this.operator + " " + this.right.toString();
    if(this.type != null) {  
      s += " : " + this.type.name;
    }
    else {
      s += " : not-assigned";
    }
    return s;
  }
  public List<Expression> getVariables(){
	return this.variables;
  }
}
