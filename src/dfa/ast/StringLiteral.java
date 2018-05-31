package ast;

public class StringLiteral extends Expression {

  public final String value;

  public StringLiteral(String value) {
    this.value = value;
  }

  public String toString() {
    return "\"" + this.value + "\"";
  }
}
