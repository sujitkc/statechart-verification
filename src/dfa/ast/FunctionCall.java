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
    variables=new ArrayList<Expression>();
    for(Expression arg:argumentList){
        if(arg instanceof IntegerConstant || arg instanceof StringLiteral){}
	else variables.add(arg);
	}
    
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
	return this.variables;
  }
}
