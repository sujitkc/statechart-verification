package constabl.consimulator;

import ast.*;
import java.util.*;

public class ConcurrentExecutionBlock extends ExecutionBlock{
    public List<ExecutionBlock> sequencelist =new ArrayList<ExecutionBlock>();
    public SequentialExecutionBlock next=null;
    public String toString(){
        String s="[ ";
        for(ExecutionBlock ses: sequencelist)
            s+=ses.toString()+" || ";
        return s+" ]";
    }

    public List<SequentialExecutionBlock> getFinalProgramPointInSequence(ConcurrentExecutionBlock conseq, List<SequentialExecutionBlock> returnSequence){
        for(ExecutionBlock ses:conseq.sequencelist){
           
            if(ses.hasNext()){
                returnSequence.remove(ses);
                if(ses.next instanceof ConcurrentExecutionBlock)
                    getFinalProgramPointInSequence((ConcurrentExecutionBlock)ses.next,returnSequence);

            }
            else{
                returnSequence.add((SequentialExecutionBlock)ses);
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