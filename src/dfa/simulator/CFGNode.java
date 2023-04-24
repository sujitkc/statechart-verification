package simulator;

import ast.*;

import java.util.List;
import java.util.ArrayList;

public class CFGNode 
{
    private State associatedState = null;
    private Transition associatedTransition = null;
    private StatementList associatedStatementList = new StatementList();
    private CFGNode parent = null;
    private List<Expression> p = new ArrayList<Expression>();
    private List<CFGNode> children = new ArrayList<CFGNode>();
    

    public StatementList getAssociatedStatementList()
    {
        return this.associatedStatementList;
    }

    public State getAssociatedState()
    {
        return this.associatedState;
    }

    public Transition getAssociatedTransition()
    {
        return this.associatedTransition;
    }

    public List<Expression> getP()
    {
        return this.p;
    }

    public List<CFGNode> getChildren()
    {
        return this.children;
    }

    public CFGNode getParent()
    {
        return this.parent;
    }

}