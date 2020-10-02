package symbolic_execution.se_tree;

import ast.*;

public class StateEntryNode extends SETNode{
    
    public final State state;
    
    public StateEntryNode(State s, SETNode p)
    {
        super(p);
        this.state = s;
		System.out.println("State Entry node created at depth : "+this.depth +" : "+s.getFullName());

    }

    public State getState()
    {
        return this.state;
    }
}

