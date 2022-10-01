package constabl.consimulator;

import ast.*;
import java.util.*;

public class MixedExecutionSequence extends ExecutionSequence{
    public List<ExecutionSequence> sequencelist =new ArrayList<ExecutionSequence>();
    public String toString(){
        String s="[ ";
        for(ExecutionSequence ses: sequencelist)
            s+=ses.toString()+", ";
        return s+" ]";
    }

}