package ast;

import visitor.Visitor;

public abstract class Expression {
/*
  public enum Operator {
    ADD,
    MUL,
    SUB,
    DIV,
    GE,
    GT,
    LE,
    LT,
    EQ,
    NE,
    AND,
    OR,
    UMIN,
    NOT
  };
*/

  protected Type type;


  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void visit (Visitor visitor) throws Exception {
    System.out.println(this.toString());
    this.visit(visitor);
  }
}
