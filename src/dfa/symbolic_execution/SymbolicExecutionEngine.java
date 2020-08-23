package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;

import java.util.ArrayList;

public class SymbolicExecutionEngine{
    
    private Statechart statechart;

    public SymbolicExecutionEngine(Statechart statechart) throws Exception{
        try{
            this.statechart = statechart;
            this.execute(statechart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    } 

    // TODO
    public void execute(Statechart statechart){ 
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        
    }

    /*
    // Functions need implementation
    public static SymbolicExecutionResult takeTransition();
    public static SymbolicExecutionResult enterState();
    public static SymbolicExecutionResult exitState();
    public static SymbolicExecutionResult executeStatement();
    */

}
