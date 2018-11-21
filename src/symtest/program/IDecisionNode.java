package program;

import expression.IExpression;

public interface IDecisionNode extends IProgramNode {
	public IExpression getCondition();
}
