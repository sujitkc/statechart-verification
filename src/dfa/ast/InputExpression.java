package ast;
import java.util.Map;
import java.util.HashMap;
public class InputExpression extends FunctionCall {

  public Declaration dec;
  public InputExpression(Declaration dec) {
    this.dec=dec;
  }
 /* public final Expression expression;
  public final String operator;

  public enum OperatorType {
    UMIN, // "-"
    NOT  // "!"
    
  }
  public static final Map<OperatorType, String> operator_string = new HashMap<OperatorType, String>();

  {
    InputExpression.operator_string.put(OperatorType.UMIN, "-");
    InputExpression.operator_string.put(OperatorType.NOT,  "!");
    
  }

  public InputExpression(Expression expression, String operator) {
    this.expression = expression;
    this.operator   = operator;
  }*/
}
