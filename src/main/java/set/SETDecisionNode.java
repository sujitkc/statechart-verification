package set;

import ast.Expression;

public class SETDecisionNode extends SETNode {
	final Expression _condition;

	public SETDecisionNode (SETNode parent, Expression condition) {
		super (parent);
		this._condition = condition;
	}
}
