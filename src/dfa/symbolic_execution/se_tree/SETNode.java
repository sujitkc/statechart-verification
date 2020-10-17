package symbolic_execution.se_tree;

import ast.*;
import java.util.*;
public class SETNode{
    public Map<Name, Expression> mValues = new HashMap<Name, Expression>();

    public final SETNode parent;
	public int depth;
	public Map<Declaration, String> env;

    public SETNode(SETNode p)
    {
		this.env=new HashMap<Declaration, String>();
        this.parent = p;
    }

    public SETNode getParent()
    {
        return this.parent;
    }
	

}
