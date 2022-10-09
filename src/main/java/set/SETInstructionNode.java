package set;

import ast.Expression;
import ast.Declaration;
import ast.Name;

public class SETInstructionNode extends SETNode {
	final private Expression _expr;
	final private Name _name;

	public SETInstructionNode (SETNode parent, Name name, Expression expr) {
		super (parent);
		this._name = name;
		this._expr = expr;
	}

	public Name getName () {return _name;}
	public Expression getExpression() {return _expr;}

	public Expression getVarValue (Declaration decl) throws Exception {
		if (this._name.getDeclaration() == decl) {
			return this._expr;
		} else if (this._parent != null) {
			return this._parent.getVarValue(decl);
		} else {
			throw new Exception ("No such var" + decl.vname);
		}
	}
}
