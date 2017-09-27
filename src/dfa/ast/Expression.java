package ast;

public abstract class Expression {
  protected Type type;

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}
