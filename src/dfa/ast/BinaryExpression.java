package ast;

import java.util.Map;

import visitor.Visitor;

import java.util.HashMap;

public class BinaryExpression extends Expression {

  public final Expression left;
  public final Expression right;

  public final String operator;

  public enum OperatorType {
    PLUS, // "+"
    MUL,  // "*"
    SUB,  // "-"
    DIV,  // "/"

    GE,   // ">="
    GT,   // ">"
    LE,   // "<="
    LT,   // "<"
    NE,   // "!="
    EQ,   // "="

    AND,  // "&&"
    OR    // "||"
  }

  public static final Map<OperatorType, String> operator_string = new HashMap<OperatorType, String>();

  {
    BinaryExpression.operator_string.put(OperatorType.PLUS, "+");
    BinaryExpression.operator_string.put(OperatorType.MUL,  "*");
    BinaryExpression.operator_string.put(OperatorType.SUB,  "-");
    BinaryExpression.operator_string.put(OperatorType.DIV,  "/");
    BinaryExpression.operator_string.put(OperatorType.GE,  ">=");
    BinaryExpression.operator_string.put(OperatorType.GT,   ">");
    BinaryExpression.operator_string.put(OperatorType.LE,  "<=");
    BinaryExpression.operator_string.put(OperatorType.LT,   "<");
    BinaryExpression.operator_string.put(OperatorType.NE,  "!=");
    BinaryExpression.operator_string.put(OperatorType.EQ,   "=");
    BinaryExpression.operator_string.put(OperatorType.AND,  "&&");
    BinaryExpression.operator_string.put(OperatorType.OR,   "||");
  }

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

  public void visit (Visitor visitor) throws Exception {
    visitor.visitBinaryExpression(this);
  }
}
