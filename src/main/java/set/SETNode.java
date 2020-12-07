package set;

import ast.Declaration;
import ast.Expression;

abstract public class SETNode{
    protected final SETNode _parent;
	protected int _depth;

	public SETNode (SETNode parent) {
		this._parent = parent;
		if (parent == null) {
			this._depth = 0;
		}
		else {
			this._depth = parent._depth + 1;
		}
	}

    public SETNode getParent() {return this._parent;}
	public int getDepth () {return this._depth;}

	abstract public Expression getVarValue (Declaration decl) throws Exception;
}

