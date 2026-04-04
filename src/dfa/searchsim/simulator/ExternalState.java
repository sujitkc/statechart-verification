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
        String res = "Conf = ";
        for(State s : this.configuration){
            res = res + s.name + " "; 
        } 

        res = res + " | "; 

        for(Map.Entry<Declaration , Expression>entry : this.environment.entrySet())
        {
            res = res + entry.getKey().getFullVName() + " " + entry.getValue() + " \n";
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

    @Override
    public boolean equals(Object o){
///////////////////////////////////////////////////////////////////////////////////////////////////////
        if(o == null) return false;
        if(!(o instanceof ExternalState)) return false;
///////////////////////////////////////////////////////////////////////////////////////////////////////
        
        ExternalState e = (ExternalState)o; 

        if(e.getConfiguration().size() != this.configuration.size()){
            return false; 
        }

        for(State s : this.configuration){
            boolean found = false; 
            for(State t : e.getConfiguration()){
                if(s.toString().equals(t.toString())){
                    found = true; 
                }
            }

            if(!found){
                return false; 
            }
        }

        for(Declaration d : this.environment.keySet()){
            if(!e.getEnv().keySet().contains(d)){
                System.out.println("does not contain key");
                return false; 
            }


            Expression l = e.getEnv().get(d); 
            Expression r = this.environment.get(d); 

            if(l == null ^ r == null){
                System.out.println("null val"); 
                return false; 
            }

            if(l == null && r == null)
            {
                return true; 
            }

            IntegerConstant il = (IntegerConstant)l; 
            IntegerConstant ir = (IntegerConstant)r; 

            if(il.getInt() != ir.getInt()){
                return false;   
            }
        }

        return true; 
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////    
    @Override 
    public int hashCode() {
        // Must be consistent with equals() - use configuration state names and environment
        int hash = 0;
        for(State s : this.configuration) {
            hash += s.toString().hashCode();
        }
        for(Declaration d : this.environment.keySet()) {
            Expression e = this.environment.get(d);
            if(e != null) {
                hash += d.hashCode() * 31 + e.toString().hashCode();
            }
        }
        return hash;
    }
}
//////////////////////////////////////////////////////////////////////////////////////////////////////