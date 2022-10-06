package constabl.consimulator;

import ast.*;
import java.util.ArrayList;

/*
SC -> {C}
C -> C,state | C,C | C,S
S -> [{C},{C}]
Composite sequence - set of states, or set of composite sequences, or composite sequence with concurrent sequence
Concurrent sequence - set of composite sequences. 
 */

public abstract class ExecutionBlock{
    public ArrayList<State> stateList=null;

    public ExecutionBlock next=null;
    public String toString(){
        String s="";
        
        return s;
    }
    public boolean hasNext(){
        System.out.println("Calling super class function");
        if(this.next==null)
            return false;
        else
            return true;
    }
   

}