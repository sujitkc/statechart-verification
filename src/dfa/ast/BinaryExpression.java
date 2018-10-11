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
	this.variables=new ArrayList<Expression>();
    
    
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
  // There can be FunctionCall within BinaryExpression 
	//or BinaryExpression inside FunctionCall
	//if Functioncall - recursively identify all variables
	//if BinaryExpression - recursively identify all variables
   if(this.left instanceof IntegerConstant || this.left instanceof StringLiteral || this.left instanceof BooleanConstant){}
	else if(this.left instanceof FunctionCall){
		this.variables.addAll(((FunctionCall)this.left).getVariables());
	}
	else if(this.left instanceof BinaryExpression){
		this.variables.addAll(((BinaryExpression)this.left).getVariables());
	}
    else this.variables.add(left);
    if(this.right instanceof IntegerConstant || this.right instanceof StringLiteral || this.right instanceof BooleanConstant){}
    else if(this.right instanceof FunctionCall){
		this.variables.addAll(((FunctionCall)this.right).getVariables());
	}
	else if(this.right instanceof BinaryExpression){
		this.variables.addAll(((BinaryExpression)this.right).getVariables());
	}
	else this.variables.add(right);
	return this.variables;
  }
}
