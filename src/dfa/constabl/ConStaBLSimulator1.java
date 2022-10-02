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
            initialProgramPoints.add(new DotProgramPoint("init"));
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
                //((DotProgramPoint)(currentconfig.getProgramPoints()).get(0)).setState(this.statechart);
                ArrayList<State> listofstates=new ArrayList<State>();
                listofstates.add(this.statechart);
               // computeActionDefaultEntry(currentconfig, listofstates,new SequentialExecutionSequence());
                ExecutionSequence initialExecutionSequence=computeActionDefaultEntryForState(currentconfig, this.statechart,new SequentialExecutionSequence());
                System.out.println(initialExecutionSequence);
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

    // public ExecutionSequence computeActionDefaultEntry(Configuration currentconfig, ArrayList<State> entryStates, ExecutionSequence actionSequence)
    // {
        
    //     try{
            
           
    //        System.out.println("\n== entry states coming are : ");
    //        for(State s:entryStates){
    //                     System.out.print(s.name+", ");
    //                 }
    //        System.out.println();
           
    //        ArrayList<State> inputstates=new ArrayList<State>();
    //        inputstates.addAll(entryStates);
           
           
            
           
    //        for(State state: inputstates){
                
    //             if(state instanceof ast.Shell){
    //                 actionSequence=addProgramPointsForStateEntry(actionSequence,state);
    //                 actionSequence.next=new ConcurrentExecutionSequence();

    //                 entryStates.addAll(state.getAllSubstates());
    //                 entryStates.remove(state);
    //                 System.out.print("Shell detected:  ");
                    
    //                 for(State s:entryStates){
    //                     System.out.print(s.name+", ");
    //                 }
    //                 //return computeActionDefaultEntry(currentconfig,entryStates);
    //             }else if(state.getAllSubstates().size()>0){
    //                 actionSequence=addProgramPointsForStateEntry(actionSequence,state);

    //                 entryStates.add(state.getAllSubstates().get(0));
    //                 entryStates.remove(state);
    //                 System.out.print("Composite detected:  ");
    //                 for(State s:entryStates){
    //                     System.out.print(s.name+", ");
    //                 }
                    

    //                 //return computeActionDefaultEntry(currentconfig,entryStates);
    //             }
    //             else{
    //                 actionSequence=addProgramPointsForStateEntry(actionSequence,state);

    //                 System.out.println("Atomic detected:  "+state.name);
                    
    //                 //return actionSequence;
    //             }
    //        }
    //        if(inputstates.equals(entryStates)){
    //             return actionSequence;
    //        }
    //        else{
    //            return computeActionDefaultEntry(currentconfig,entryStates,actionSequence);
    //         }
          
    //     }
    //     catch(Exception e){
    //         e.printStackTrace();
    //     }
    //     return actionSequence;
    // }

    public ExecutionSequence computeActionDefaultEntryForState(Configuration currentconfig, State entryState, ExecutionSequence actionSequence)
    {
        
        try{
            
           
           System.out.println("\n== entry states coming are : "+entryState.name);
           State state=entryState;
                
                if(state instanceof ast.Shell){
                    System.out.print("Shell detected:  ");
                    
                    actionSequence=addProgramPointsForStateEntry(actionSequence,state);
                    if(actionSequence instanceof SequentialExecutionSequence){
                        ConcurrentExecutionSequence ces=new ConcurrentExecutionSequence();
                        List<State> newEntryStates=state.getAllSubstates();
                                       
                            for(State s:newEntryStates){
                                    System.out.print(s.name+", ");
                                    SequentialExecutionSequence ses=new SequentialExecutionSequence();
                                    ses=(SequentialExecutionSequence)computeActionDefaultEntryForState(currentconfig,s,ses);
                                    ces.sequencelist.add(ses);
                             }

                        ((SequentialExecutionSequence)actionSequence).setNextCES(ces);

                    }
                    else{
                        System.out.println("Entry from one concurrent state to another is detected : yet to handle" );
                    }
                        
                    
                    
                }else if(state.getAllSubstates().size()>0){
                    actionSequence=addProgramPointsForStateEntry(actionSequence,state);

                    //entryStates.add(state.getAllSubstates().get(0));
                    //entryStates.remove(state);
                    System.out.println("Composite detected:  "+state.name);
                    computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionSequence);
                    

                    
                }
                else{
                    actionSequence=addProgramPointsForStateEntry(actionSequence,state);

                    System.out.println("Atomic detected:  "+state.name);
                    
                    //return actionSequence;
                }
           
           if(state==entryState){
                return actionSequence;
           }
           else{
               return computeActionDefaultEntryForState(currentconfig,entryState,actionSequence);
            }
          
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return actionSequence;
    }
    public ExecutionSequence addProgramPointsForStateEntry(ExecutionSequence actionSequence, State state){
        //Adding program points
                    if(actionSequence instanceof SequentialExecutionSequence){
                        ((SequentialExecutionSequence)actionSequence).addProgramPoint(new EntryBeginProgramPoint("NB_"+state.name));
                        for(Statement stmt:((StatementList)state.entry).getStatements()){
                            System.out.println("Statement detected : "+stmt);
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new StatementProgramPoint(stmt.toString()));
                        
                        }
                        ((SequentialExecutionSequence)actionSequence).addProgramPoint(new EntryBeginProgramPoint("NE_"+state.name));
                        
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