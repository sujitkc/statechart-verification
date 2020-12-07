package set;

import ast.Expression;
import ast.Declaration;

public class SETDecisionNode extends SETNode {
	final Expression _condition;

	public SETDecisionNode (SETNode parent, Expression condition) {
		super (parent);
		this._condition = condition;
	}

	public Expression getVarValue(Declaration decl) throws Exception {
		if (this._parent == null) {
			throw new Exception ("No such var");
		} else {
			return this._parent.getVarValue(decl);
		}
	}
}
