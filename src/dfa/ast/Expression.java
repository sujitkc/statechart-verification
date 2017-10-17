package ast;

import java.lang.*;

public abstract class Expression {
  protected Type type;
  public Name n1, n2;

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }
/*
  public Expression(Name n1) {
  	this.n1 = n1;
  }
  public Expression(Name n1,Name n2) {
  	this.n1 = n1;
  	this.n2 = n2;
  }
*/
}
