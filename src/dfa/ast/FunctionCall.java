package ast;

import java.util.List;

public class FunctionCall extends Expression {
  public final String name;
  public List<Expression> argumentList;

  public FunctionCall(String name, List<Expression> argumentList) {
    this.name     = name;
    this.argumentList = argumentList;
  }

  public List<Expression> getArgumentList() {
    return this.argumentList;
  }

  public String toString() {
    String s = this.name + "(";
    for(Expression arg : this.argumentList) {
      s += arg.toString() + ", ";
    }
    return s + ")";
  }
}
