package constabl;
import ast.*;
import java.util.*;
import constabl.consimulator.*;
import constabl.actionprogram.*;

public class ConStaBLSimulator1 extends SimulStatechart{
    private Statechart statechart = null;
    private List<String> eventQueue=null;
    private Configuration activeconfig=null;
    private final String tinit="tinit";
    public ConStaBLSimulator1(Statechart sc){
        try{
            this.statechart=sc;

            //Initializing the event queue
            this.eventQueue=new ArrayList<String>();
            this.eventQueue.add(tinit);
            this.eventQueue.add("e1");
            this.eventQueue.add("e2");

            //initializing the configuration
            //DotProgramPoint initPoint=new DotProgramPoint();
            //statechartentry.setState(sc);
            List<ProgramPoint> initialProgramPoints=new ArrayList<ProgramPoint>();
            initialProgramPoints.add(new DotProgramPoint());
            this.activeconfig=new Configuration(initialProgramPoints);

            System.out.println("Starting concurrent simulation");
            
            for(String event : eventQueue){
                List<Transition> transitionList=consumeEvent(activeconfig,event);
                
                //send transition list to detect non-determinism
                //detectNonDeterminism(transitionList);

                for(Transition t: transitionList){
                    takeTransition(activeconfig,t);
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    
    public  List<Transition> consumeEvent(Configuration currentconfig, String event)
    {
        System.out.println("Consume event : "+event);
        List<Transition> identifiedTransitions=new ArrayList<Transition>();
        try{
            if(event.equals(tinit)){
                //initialize the statechart with tinit transition
                Transition initTransition=new Transition(tinit,null,null,null,null,null);
                identifiedTransitions.add(initTransition);
            }
            else{
                //find transitions

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return identifiedTransitions;

    }
    

    public Configuration takeTransition(Configuration currentconfig, Transition t)
    {
        System.out.println("Take Transition : "+t.name);
        Configuration config=currentconfig;
 
        try{
            if(t.name.equals(tinit)&&t.getSource()==null){
                //Transition is going to initialize the statechart
                ((DotProgramPoint)(currentconfig.getProgramPoints()).get(0)).setState(this.statechart);
                computeActionDefaultEntry(currentconfig);
            }
            else{

            }
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

    public ExecutionSequence computeActionDefaultEntry(Configuration currentconfig)
    {
        ExecutionSequence actionSequence=new ExecutionSequence();
        try{
            ArrayList<State> activeStates=new ArrayList<State>();
            for(ProgramPoint p:currentconfig.getProgramPoints()){
                if(p instanceof DotProgramPoint){
                    State stateToEnter=((DotProgramPoint)p).getState();
                    activeStates.add(stateToEnter);
                }
                    
            }
            ExecutionSequence exsequence;
            if(activeStates.size()==0){
                exsequence=new SequentialExecutionSequence();
               // exsequence.add()
            }
            else{
                exsequence=new ConcurrentExecutionSequence();
            }
            System.out.println(getDefaultAtomicSubStateSequence(activeStates, exsequence));

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return actionSequence;
    }
    public List<State> getDefaultAtomicSubStateSequence(ArrayList<State> substate, ExecutionSequence executionSequence){
            
            ExecutionSequence currentSequence=executionSequence;
            ArrayList<State> returnsubstates=new ArrayList<State>();
            
            for(State s:substate){
                if(s instanceof ast.Shell){
                    if(currentSequence instanceof SequentialExecutionSequence){
                        
                    }
                    if(currentSequence instanceof ConcurrentExecutionSequence){
                        
                    }
                }
                else if(s.states.size()>0){ //composite state

                }
                else{ //atomic state

                }
            }

            for(int i=0;i<substate.size();i++){
            
                    if(substate.get(i) instanceof ast.Shell){

                        returnsubstates.addAll(i,(substate.get(i)).states);
                    }
                    else if((substate.get(i)).states.size()!=0){    
                        ArrayList<State> child=new ArrayList<State>();
                        child.add((substate.get(i)).states.get(0));
                        returnsubstates.add(i,getDefaultAtomicSubStateSequence(child,currentSequence).get(0));
                        //returnsubstates.add(i,(substate.get(i)).states.get(0));

                    }
                    else{
                        // it is an atomic state -- add as it is
                        returnsubstates.add(i,substate.get(i));
                    }
            }
             
            if(returnsubstates.equals(substate))
                return returnsubstates;
            else
                return getDefaultAtomicSubStateSequence(returnsubstates, executionSequence);

        }

}