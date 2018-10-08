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
	// There can be FunctionCall within BinaryExpression 
	//or BinaryExpression inside FunctionCall
	//if Functioncall - recursively identify all variables
	//if BinaryExpression - recursively identify all variables
    variables=new ArrayList<Expression>();
    if(left instanceof IntegerConstant || left instanceof StringLiteral || left instanceof BooleanConstant){}
	else if(left instanceof FunctionCall){
		variables.addAll(((FunctionCall)left).getVariables());
	}
	else if(left instanceof BinaryExpression){
		variables.addAll(((BinaryExpression)left).getVariables());
	}
    else variables.add(left);
    if(right instanceof IntegerConstant || right instanceof StringLiteral || right instanceof BooleanConstant){}
    else if(right instanceof FunctionCall){
		variables.addAll(((FunctionCall)right).getVariables());
	}
	else if(right instanceof BinaryExpression){
		variables.addAll(((BinaryExpression)right).getVariables());
	}
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
