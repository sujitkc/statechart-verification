package ast;

import visitor.ExpressionVisitor;

public class IntegerConstant extends Expression {

  public final int value;

  public IntegerConstant(int value) {
    this.value = value;
  }

  public String toString() {
    return new Integer(this.value).toString();
  }

  public void visit (ExpressionVisitor visitor) throws Exception {
    visitor.visitIntegerConstant(this);
  }
}
