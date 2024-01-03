package searchsim.simulator; 

import java.util.Set;
import java.util.Map; 
import java.util.HashMap; 

import ast.*;

public abstract class SimState{
    protected SimState parent; 
    protected Map<Declaration, Expression>environment; 
    public SimState(){

    }

    public void setParent(SimState parent){
        this.parent = parent;
    }

    public Map<Declaration, Expression> getDependendentEnvironment(Set<Declaration> depVarSet){
        if(depVarSet == null || depVarSet.size() == 0){
            return new HashMap<Declaration, Expression>(); 
        } 

        Map<Declaration, Expression> res = new HashMap<Declaration, Expression>(); 
        for(Declaration d : depVarSet){
            if(this.environment.containsKey(d))
            {
                res.put(d , this.environment.get(d)); 
                depVarSet.remove(d); 
            }
        }

        if(this instanceof ExternalState && depVarSet.size() != 0){
            System.out.println("Undefined variable found"); 
            return null; 
        }


        Map<Declaration, Expression> remnantMap = this.parent.getDependendentEnvironment(depVarSet); 
        if(remnantMap.size() != 0){
            for(Map.Entry<Declaration , Expression> entry : remnantMap.entrySet()){
                res.put(entry.getKey() , entry.getValue()); 
            }
        }
        return res; 
    } 

    public Map<Declaration, Expression> collectEnv(Map<Declaration, Expression>envSoFar){
        Map<Declaration, Expression> res = new HashMap<>();

        for(Map.Entry<Declaration , Expression> entry : envSoFar.entrySet()){
            res.put(entry.getKey() , entry.getValue());
        }
        
        for(Map.Entry<Declaration, Expression> entry : this.environment.entrySet()){
            if(!envSoFar.containsKey(entry.getKey()))
            {
                envSoFar.put(entry.getKey() , entry.getValue()); 
                res.put(entry.getKey() , entry.getValue()); 
            }
        }

        if(this instanceof ExternalState){
            return res; 
        }

        return this.parent.collectEnv(envSoFar); 
    }
}