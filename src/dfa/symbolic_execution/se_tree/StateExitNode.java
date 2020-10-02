package symbolic_execution.se_tree;

import ast.*;

public class StateExitNode extends SETNode{
    
    public final State state;

    public StateExitNode(State s, SETNode p)
    {
        super(p);
        this.state = s;
		System.out.println("State Exit node created at depth : "+this.depth +" : "+s.getFullName());

    }

    public State getState()
    {
        return this.state;
    }
}
