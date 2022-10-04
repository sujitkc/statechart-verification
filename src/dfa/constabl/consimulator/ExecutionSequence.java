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

public class ExecutionSequence{
    public ArrayList<State> stateList=null;

    public ExecutionSequence next=null;
    public String toString(){
        String s="";
        
        return s;
    }
    public boolean hasNext(){
        if(next==null)
            return false;
        else
            return true;
    }
   

}