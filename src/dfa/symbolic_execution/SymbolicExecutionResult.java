package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;
import java.util.List;

public class SymbolicExecutionResult{
    
    private final List<SETNode> done;
    private final List<SETNode> live;

    public SymbolicExecutionResult()
    {
        
    }

    public List<SETNode> getDoneNodes()
    {
        return this.done;
    }

    public List<SETNode> getLiveNodes()
    {
        return this.live;
    }
}
