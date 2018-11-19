package program;

import ast.Expression;

public interface IDecisionNode extends IProgramNode {
	public Expression getCondition();
}
