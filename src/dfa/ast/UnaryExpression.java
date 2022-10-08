package ast;
import java.util.Map;
import java.util.HashMap;
public class UnaryExpression extends Expression {

  public final Expression expression;
  public final String operator;

  public enum OperatorType {
    UMIN, // "-"
    NOT  // "!"
    
  }
  public static final Map<OperatorType, String> operator_string = new HashMap<OperatorType, String>();

  {
    UnaryExpression.operator_string.put(OperatorType.UMIN, "-");
    UnaryExpression.operator_string.put(OperatorType.NOT,  "!");
    
  }

  public UnaryExpression(Expression expression, String operator) {
    this.expression = expression;
    this.operator   = operator;
  }
}
