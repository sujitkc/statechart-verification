package ast;

import java.util.List;

public class FunctionCall extends Expression {
  public final FunctionName name;
  public List<Expression> argumentList;
  public FunctionDeclaration function; // to be set only when the function call is being substantiated.

  public FunctionCall(FunctionName name, List<Expression> argumentList) {
    this.name         = name;
    this.argumentList = argumentList;
  }

  public FunctionDeclaration getFunctionDeclaration(){
    return this.function;
  }

  public List<Expression> getArgumentList() {
    return this.argumentList;
  }

  public String getName(){
    return this.name.getName();
  }

  public String toString() {
    String s = this.name.toString() + "(";
    for(Expression arg : this.argumentList) {
      s += arg.toString() + ", ";
    }
    return s + ") : " + this.type;
  }
}
