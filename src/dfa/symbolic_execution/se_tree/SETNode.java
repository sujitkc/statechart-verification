package symbolic_execution.se_tree;

import ast.*;

public class SETNode{
    
    public final SETNode parent;
	public final int depth;
    public SETNode(SETNode p)
    {
        this.parent = p;
		if(p==null) 
			this.depth=1;
		else 
			this.depth=p.depth+1;
    }

    public SETNode getParent()
    {
        return this.parent;
    }

}
