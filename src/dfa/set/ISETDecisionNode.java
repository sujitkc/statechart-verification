package set;

import set.ISETNode;
import ast.Expression;

public interface ISETDecisionNode extends ISETNode {
  public Expression getCondition();
  public void setCondition(Expression expr);
}
