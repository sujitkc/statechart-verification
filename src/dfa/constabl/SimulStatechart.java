package constabl;
import ast.*;
import java.util.*;
import constabl.consimulator.*;
public abstract class SimulStatechart{
    /* This is same as taking transition with "init" */
    // public abstract Configuration initialize(Statechart SC, String event);
    
    /* This flags non determinism also */
    public abstract List<Transition> consumeEvent(Configuration currentconfig, String event);
    

    public abstract Configuration takeTransition(Configuration currentconfig, Transition t);
  
    public abstract ExecutionSequence computeAction(Configuration currentconfig, Transition t);
  
    public abstract Configuration executeAction(Configuration currentconfig, ExecutionSequence exseq);


}