package ast;

import visitor.Visitor;

public class IntegerConstant extends Expression {

  public final int value;

  public IntegerConstant(int value) {
    this.value = value;
  }

  public String toString() {
    return new Integer(this.value).toString();
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitIntegerConstant(this);
  }
}
