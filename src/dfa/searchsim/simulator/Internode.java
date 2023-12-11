package searchsim.simulator; 

import ast.*; 
import searchsim.cfg.*;

import java.util.HashSet; 
import java.util.Set;

import java.util.Map; 

/*
 * Pair of delta Env and 
 * next control points 
 */

public class Internode{
    private Map<Declaration, Expression> envd; 
    private CFGNode cp; 

    public Internode(Map<Declaration, Expression> e , CFGNode cp)
    {
        this.envd = e; 
        this.cp = cp; 
    }

    public void setEnv(Map<Declaration, Expression> e)
    {
        this.envd = e; 
    }

    public void setCP(CFGNode cp)
    {
        this.cp = cp; 
    }

    public Map<Declaration, Expression> getEnv()
    {
        return this.envd; 
    }

    public CFGNode getCP()
    {
        return this.cp; 
    }
}