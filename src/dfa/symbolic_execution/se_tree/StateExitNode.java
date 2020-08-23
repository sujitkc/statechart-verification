package symbolic_execution.se_tree;

import ast.*;

public class StateExitNode extends SETNode{
    
    public final State state;

    public StateExitNode(State s, SETNode p)
    {
        super(p);
        this.state = s;
    }

    public State getState()
    {
        return this.state;
    }
}
