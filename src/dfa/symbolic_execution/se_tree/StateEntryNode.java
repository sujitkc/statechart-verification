package symbolic_execution.se_tree;

import ast.*;

public class StateEntryNode extends SETNode{
    
    public final State state;
    
    public StateEntryNode(State s, SETNode p)
    {
        super(p);
		this.depth=p.depth;
        this.state = s;
		System.out.println();
		System.out.println(this.depth+": SEntryN : "+s.getFullName());

    }

    public State getState()
    {
        return this.state;
    }
}

