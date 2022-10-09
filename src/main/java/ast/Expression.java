package ast;

import visitor.ExpressionVisitor;

public abstract class Expression {
  protected Type type;


  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void visit (ExpressionVisitor visitor) throws Exception {
    System.out.println(this.toString());
    this.visit(visitor);
  }
}
