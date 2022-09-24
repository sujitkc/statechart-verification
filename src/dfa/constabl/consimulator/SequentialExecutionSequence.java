package constabl.consimulator;

import ast.*;
import java.util.ArrayList;

public class SequentialExecutionSequence extends ExecutionSequence{
    public ArrayList<State> stateList=new ArrayList<State>();
    public String toString(){
        String s="";
        
        return s;
    }
    public void addState(State s){
        this.stateList.add(s);
    }

}