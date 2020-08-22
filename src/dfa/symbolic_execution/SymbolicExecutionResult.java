package symbolic_execution;

import ast.*;

import java.util.List;

public class SymbolicExecutionResult{
    
    private final List<SETNode> done;
    private final List<SETNode> live;

    public SymbolicExecutionResult(){
        
    }



    public static getDoneNodes(){
        return done;
    }

    public static getLiveNodes(){
        return live;
    }
}
