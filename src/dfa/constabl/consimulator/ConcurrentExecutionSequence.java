package constabl.consimulator;

import ast.*;
import java.util.*;

public class ConcurrentExecutionSequence extends ExecutionSequence{
    public List<ExecutionSequence> sequencelist =new ArrayList<ExecutionSequence>();
    public SequentialExecutionSequence next=null;
    public String toString(){
        String s="[ ";
        for(ExecutionSequence ses: sequencelist)
            s+=ses.toString()+" || ";
        return s+" ]";
    }

    public List<SequentialExecutionSequence> getFinalProgramPointInSequence(ConcurrentExecutionSequence conseq, List<SequentialExecutionSequence> returnSequence){
        for(ExecutionSequence ses:conseq.sequencelist){
           
            if(ses.hasNext()){
                returnSequence.remove(ses);
                if(ses.next instanceof ConcurrentExecutionSequence)
                    getFinalProgramPointInSequence((ConcurrentExecutionSequence)ses.next,returnSequence);

            }
            else{
                returnSequence.add((SequentialExecutionSequence)ses);
            }
        }
        return returnSequence;
    }
    public boolean hasNext(){
        if(next==null)
            return false;
        else
            return true;
    }
   
}