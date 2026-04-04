package searchsim.simulator; 

import ast.*;

import java.util.HashMap;
import java.util.HashSet; 
import java.util.Set;


import java.util.Map; 

import searchsim.cfg.*; 


public class MachineState extends SimState{
    private Map<CFGNode , Set<CFGNode>>joinPoints;  //joinSet for a particular interleaving
    private Set<CFGNode>currReadySet; // has to be a hashset of cps

    /*
     * Note : 
     * List of parent states in case of digraph
     */


    public MachineState(Set<CFGNode>rs , Map<Declaration , Expression> e) //rooted
    {
        this.currReadySet = rs;
        this.environment = e; 
        this.joinPoints = new HashMap<>(); 
    }

    public MachineState(Map<Declaration , Expression> e , MachineState prev)
    {
        this.environment = e; 
        this.parent = prev; 
    }

    public Set<CFGNode> getReadySet()
    {
        return this.currReadySet; 
    }

    public Map<Declaration , Expression> getEnv()
    {
        return this.environment; 
    }

    public Map<Declaration, Expression> getCloneEnv()
    {
        Map<Declaration, Expression> newEnv = new HashMap<Declaration, Expression>(); 
        for(Map.Entry<Declaration , Expression>entry : this.environment.entrySet())
        {
            newEnv.put(entry.getKey(), entry.getValue()); 
        }
        return newEnv; 
    }

    public void addJoinPoints(Map<CFGNode , Set<CFGNode>>jp)
    {
        this.joinPoints = new HashMap<CFGNode , Set<CFGNode>>(); 
        for(Map.Entry<CFGNode , Set<CFGNode>>entry : jp.entrySet()){
            Set<CFGNode>newval = new HashSet<CFGNode>(); 
            for(CFGNode n : entry.getValue()){
                newval.add(n);  
            }
            this.joinPoints.put(entry.getKey() , newval); 
        }
    }

    public Map<CFGNode , Set<CFGNode>> getjoinPointsClone(){
        Map<CFGNode , Set<CFGNode>>newMap = new HashMap<>(); 
        for(Map.Entry<CFGNode , Set<CFGNode>>entry : this.joinPoints.entrySet()){
            Set<CFGNode>newval = new HashSet<CFGNode>(); 
            for(CFGNode n : entry.getValue()){
                newval.add(n);  
            }
            newMap.put(entry.getKey() , newval); 
        }
        return newMap; 
    }

    public Map<CFGNode , Set<CFGNode>> getjoinPoints()
    {
        return this.joinPoints;
    }

    public void putJoinPoint(CFGNode s , Set<CFGNode> sPredecessors)
    {
        this.joinPoints.put(s , sPredecessors); 
    }

    public void deleteJoinPoint(CFGNode s)
    {
        this.joinPoints.remove(s); 
    }

    public String getEnvString()
    {
        String res = "";
        for(Map.Entry<Declaration , Expression>entry : this.environment.entrySet())
        {
            res = res + entry.getKey().getFullVName() + " " + entry.getValue() + "\n";
        }
        return res; 
    }

    public String toString()
    { 
        String res = "RS = " + this.currReadySet.toString() + " | delta env = ";
        
        for(Map.Entry<Declaration , Expression>entry : this.environment.entrySet())
        {
            res = res + entry.getKey().getFullVName() + " " + entry.getValue() + "\n";
        }
        //System.out.println(res.hashCode()); 
        return res; 
    }


    // @Override
    // public boolean equals(Object o)
    // {
    //     return this.toString().equals(o.toString()); 
    // }

    // @Override
    // public int hashCode()
    // {
    //     return this.toString().hashCode();
    // }
}

