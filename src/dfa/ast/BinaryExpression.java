package ast;

public class BinaryExpression extends Expression {

  public final Expression left;
  public final Expression right;

  public final String operator;

  public BinaryExpression(Expression left, Expression right, String operator) {
    this.left     = left;
    this.right    = right;
    this.operator = operator;
  }

  public String toString() {
    String s = this.left.toString() + " " + this.operator + " " + this.right.toString();
    if(this.type != null) {  
      s += " : " + this.type.name;
    }
    else {
      s += " : not-assigned";
    }
    return s;
  }
}
