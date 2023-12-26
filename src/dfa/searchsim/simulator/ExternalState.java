package searchsim.simulator;

import ast.*;

import searchsim.*;
import searchsim.cfg.*;
import searchsim.code.*;
import java.util.Map; 
import java.util.Set; 
import java.util.HashSet; 
import java.util.HashMap; 

public class ExternalState extends SimState{
    private Map<Declaration, Expression>environment; 
    private Set<State> configuration; 
    private String event; 

    public ExternalState(Set<State>c,  Map<Declaration, Expression>env , String event)
    {
        this.configuration = c; 
        this.environment = env; 
        this.event = event; 
    }

    public ExternalState(Set<State>c,  Map<Declaration, Expression>env)
    {
        this.configuration = c; 
        this.environment = env; 
    }

    public void setEvent(String e)
    {
        this.event = e; 
    }

    public String toString()
    {
        String res = ""; 

        for(State s : this.configuration)
        {
            res = res + s.getUniqueName() + " "; 
        }

        for(Map.Entry<Declaration , Expression>entry : this.environment.entrySet())
        {
            res = res + entry.getKey().getFullVName() + " " + entry.getValue() + "\n";
        }

        return res; 
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

    public Set<State> getConfiguration()
    {
        return this.configuration; 
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