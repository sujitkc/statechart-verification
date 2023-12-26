package searchsim.simulator; 

import ast.*; 
import searchsim.cfg.*;

import java.util.HashSet; 
import java.util.Set;
import java.util.HashMap; 
import java.util.Map; 

/*
 * Pair of delta Env and 
 * next control points 
 */

public class Internode{
    private Map<Declaration, Expression> envd; 
    private Set<CFGNode> cp; 

    public Internode(Map<Declaration, Expression> e)
    {
        this.envd = e; 
        this.cp = new HashSet<CFGNode>();
    }

    public void setEnv(Map<Declaration, Expression> e)
    {
        this.envd = e; 
    }

    public void setCP(CFGNode cp)
    {
        this.cp = new HashSet<CFGNode>(); 
        this.cp.add(cp); 
    }

    public void addCP(CFGNode cp)
    {
        this.cp.add(cp); 
    }

    public Map<Declaration, Expression> getEnv()
    {
        return this.envd; 
    }

    public Set<CFGNode> getCP()
    {
        return this.cp; 
    }

    public Map<Declaration, Expression> getCloneEnv()
    {
        Map<Declaration, Expression> newEnv = new HashMap<Declaration, Expression>(); 
        for(Map.Entry<Declaration , Expression>entry : this.envd.entrySet())
        {
            newEnv.put(entry.getKey(), entry.getValue()); 
        }
        return newEnv; 
    }

    public int getCPsize(){
        return this.cp.size(); 
    }

    public String toString(){
        return this.cp.toString(); 
    }
}