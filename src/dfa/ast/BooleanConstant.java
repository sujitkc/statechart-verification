package ast;

import visitor.Visitor;

public class BooleanConstant extends Expression {

  public final boolean value;

  public BooleanConstant(boolean value) {
    this.value = value;
  }

  public String toString() {
    if(this.value) {
      return "true";
    } else {
      return "false";
    }
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitBooleanConstant(this);
  }
}
