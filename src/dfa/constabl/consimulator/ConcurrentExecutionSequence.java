package constabl.consimulator;

import ast.*;
import java.util.*;

public class ConcurrentExecutionSequence extends ExecutionSequence{
    public List<SequentialExecutionSequence> sequencelist =new ArrayList<SequentialExecutionSequence>();
    public String toString(){
        String s="[ ";
        for(ExecutionSequence ses: sequencelist)
            s+=ses.toString()+", ";
        return s+" ]";
    }

}