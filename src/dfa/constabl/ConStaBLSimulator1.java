package constabl;
import ast.*;
import java.util.*;
import constabl.consimulator.*;
public class ConStaBLSimulator1 extends SimulStatechart{
    private Statechart statechart = null;
    private ArrayList<String> eventQueue=null;
    ConStaBLSimulator1(Statechart sc){
        try{
            this.statechart=sc;
            this.eventQueue.add("init");
            this.eventQueue.add("e1");
            this.eventQueue.add("e2");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    
    public  List<Transition> consumeEvent(Configuration currentconfig, String event)
    {
        List<Transition> identifiedTransitions=new ArrayList<Transition>();
        try{

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return identifiedTransitions;

    }
    

    public Configuration takeTransition(Configuration currentconfig, Transition t)
    {
        Configuration config=currentconfig;
 
         try{

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return config;
    }


    public ExecutionSequence computeAction(Configuration currentconfig, Transition t)
    {
        ExecutionSequence actionSequence=new ExecutionSequence();
        try{

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return actionSequence;
    }


    public Configuration executeAction(Configuration currentconfig, ExecutionSequence exseq)
    {
        Configuration config=currentconfig;
        try{

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return config;
    }

}