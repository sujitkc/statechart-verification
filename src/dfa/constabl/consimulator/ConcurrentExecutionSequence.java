package constabl.consimulator;

import ast.*;
import java.util.*;

public class ConcurrentExecutionSequence extends ExecutionSequence{
    public List<SequentialExecutionSequence> sequencelist =new ArrayList<SequentialExecutionSequence>();
    public SequentialExecutionSequence next=null;
    public String toString(){
        String s="[ ";
        for(ExecutionSequence ses: sequencelist)
            s+=ses.toString()+" || ";
        return s+" ]";
    }

    public List<SequentialExecutionSequence> getFinalProgramPointInSequence(ConcurrentExecutionSequence conseq, List<SequentialExecutionSequence> returnSequence){
        for(SequentialExecutionSequence ses:conseq.sequencelist){
           
            if(ses.hasNext()){
                returnSequence.remove(ses);
                getFinalProgramPointInSequence(ses.next,returnSequence);

            }
            else{
                returnSequence.add(ses);
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