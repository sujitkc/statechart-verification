package ast;

import java.util.*;

public class FunctionCall extends Expression {
  public final FunctionName name;
  public List<Expression> argumentList;
  public FunctionDeclaration function; // to be set only when the function call is being substantiated.
  public List<Expression> variables; 
  public FunctionCall(FunctionName name, List<Expression> argumentList) {
    this.name         = name;
    this.argumentList = argumentList;
    this.variables=new ArrayList<Expression>();
    
    
  }
  
  public List<Expression> getArgumentList() {
    return this.argumentList;
  }

  public String toString() {
    String s = this.name.toString() + "(";
    for(Expression arg : this.argumentList) {
      s += arg.toString() + ", ";
    }
    return s + ") : " + this.type;
  }
   public List<Expression> getVariables(){
	for(Expression arg:this.argumentList){
        if(arg instanceof IntegerConstant || arg instanceof StringLiteral){}
		else if(arg instanceof FunctionCall)
				this.variables.addAll(((FunctionCall)arg).getVariables());
		else if(arg instanceof BinaryExpression)
				this.variables.addAll(((BinaryExpression)arg).getVariables());
		else this.variables.add(arg);
	}
	return this.variables;
  }
}
