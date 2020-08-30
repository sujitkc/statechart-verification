package symbolic_execution.se_tree;

import ast.*;

public class StateEntryNode extends SETNode{
    
    public final State state;
    
    public StateEntryNode(State s, SETNode p)
    {
        super(p);
        this.state = s;
    }

    public State getState()
    {
        return this.state;
    }
}

