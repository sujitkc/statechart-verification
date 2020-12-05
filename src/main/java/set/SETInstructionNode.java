package set;

import ast.Expression;
import ast.Name;

public class SETInstructionNode extends SETNode {
	final Expression _expr;
	final Name _name;

	public SETInstructionNode (SETNode parent, Name name, Expression expr) {
		super (parent);
		this._name = name;
		this._expr = expr;
	}

	public Name getName () {return _name;}
	public Expression getExpression() {return _expr;}
}
