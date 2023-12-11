package searchsim.simulator; 

import ast.*;

import java.util.HashSet; 
import java.util.Set;
import java.util.Map; 

import searchsim.cfg.*; 


public class MachineState {
    private Map<CFGNode , Set<CFGNode>>joinPoints;  //joinSet for a particular interleaving
    private Set<CFGNode>currReadySet; // has to be a hashset of cps
    private MachineState prevMachineState; //null for starting of code
    /*
     * Note : 
     * List of parent states in case of digraph
     */
    private Map<Declaration, Expression>envd; //change in environment

    public MachineState(Set<CFGNode>rs , Map<Declaration , Expression> e) //rooted
    {
        this.currReadySet = rs;
        this.envd = e; 
        this.prevMachineState = null; 
    }

    public MachineState(Map<Declaration , Expression> e , MachineState prev)
    {
        this.envd = e; 
        this.prevMachineState = prev; 
    }

    public Set<CFGNode> getReadySet()
    {
        return this.currReadySet; 
    }

    public Map<Declaration , Expression> getEnv()
    {
        return this.envd; 
    }


    public Map<CFGNode , Set<CFGNode>> getjoinPoints()
    {
        return this.joinPoints;
    }

    public String toString()
    { 
        String res = ""; 

        for(Map.Entry<Declaration , Expression>entry : this.envd.entrySet())
        {
            res = res + entry.getKey().getFullVName() + " " + entry.getValue() + "\n";
        }
        return res; 
    }
}
