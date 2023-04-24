package symbolic_execution;

import ast.*;

import java.util.ArrayList;

public class SymbolicExecutionEngine{
    
    private Statechart statechart;

    public SymbolicExecutionEngine(Statechart statechart) throws Exception{
        try{
            this.statechart = statechart;
            this.execute(statechart);
        }
        catch (Exception e){
        }
    } 

    // TODO
    public execute(Statechart statechart){
        return null;
    }

    /*
    // Functions need implementation
    public static SymbolicExecutionResult takeTransition();
    public static SymbolicExecutionResult enterState();
    public static SymbolicExecutionResult exitState();
    public static SymbolicExecutionResult executeStatement();
    */

}
