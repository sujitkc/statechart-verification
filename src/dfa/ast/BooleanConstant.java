package ast;

import visitor.Visitor;

public class BooleanConstant extends Expression {

  public final boolean value;

  public static final BooleanConstant True = new BooleanConstant(true);
  public static final BooleanConstant False = new BooleanConstant(false);

  public BooleanConstant(boolean value) {
    this.value = value;
  }

  public boolean equals(BooleanConstant b) {
    return this.value == b.value;
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
