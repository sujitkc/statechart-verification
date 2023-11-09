package set;

import ast.Expression;
import ast.BinaryExpression;
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

	public Expression getPathConstraint() {
		if (this.getParent() == null) {
			return _condition;
		} else {
			Expression pc_from_root = this.getParent().getPathConstraint();
			if (pc_from_root == null) {
				return _condition;
			} else {
				return new BinaryExpression(_condition, pc_from_root, "&&");
			}
		}
	
	}

}
