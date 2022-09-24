package constabl.consimulator;

import ast.*;
import java.util.ArrayList;

public class SequentialExecutionSequence extends ExecutionSequence{
    public ArrayList<State> stateList=new ArrayList<State>();
    public String toString(){
        String s="{ ";
        for(State ses: stateList)
            s+=ses.getFullName()+", ";
        return s+" } ";
    }
    public void addState(State s){
        this.stateList.add(s);
    }

}