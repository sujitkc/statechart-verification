package ast;

public class BooleanConstant extends Expression {

  public final boolean value;

  public BooleanConstant(boolean value) {
    this.value = value;
    this.type = new Type("Boolean");
  }

  public String toString() {
    if(this.value) {
      return "true";
    } else {
      return "false";
    }
  }
}
