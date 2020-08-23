package symbolic_execution.se_tree;

import ast.*;

public class SETNode{
    
    public final SETNode parent;

    public SETNode(SETNode p)
    {
        this.parent = p;
    }

    public SETNode getParent()
    {
        return this.parent;
    }

}
