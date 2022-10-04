package constabl;
import ast.*;
import java.util.*;
import constabl.consimulator.*;
import constabl.actionprogram.*;

public class ConStaBLSimulator1 extends SimulStatechart{
    private Statechart statechart = null;
    private List<String> eventQueue=null;
    private Configuration activeconfig=null;
    private final String tinit=EventQueue.tinit;

    public ConStaBLSimulator1(Statechart sc){
        try{
            EventQueue eq=new EventQueue();
            this.statechart=sc;

            //Initializing the event queue
            this.eventQueue=new ArrayList<String>();
            this.eventQueue.addAll(eq.eventQueue);
            //this.eventQueue.add(tinit);
            //this.eventQueue.add("e1");
            //this.eventQueue.add("e2");

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
                    activeconfig=takeTransition(activeconfig,t);
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    
    public  List<Transition> consumeEvent(Configuration currentconfig, String event)
    {
        System.out.println("Consume event : "+event +" : Config : "+currentconfig);
        List<Transition> identifiedTransitions=new ArrayList<Transition>();
        try{
            if(event.equals(tinit)){
                //initialize the statechart with tinit transition
                Transition initTransition=new Transition(tinit,null,null,null,null,null);
                identifiedTransitions.add(initTransition);
            }
            else{
                //find transitions
                List<Transition> transitions=findTransitions(currentconfig,event);
                System.out.print("Transitions found: ");
                for(Transition t:transitions)
                    System.out.print(t.name+", ");
                System.out.println();
                identifiedTransitions.addAll(transitions);
                findNonDeterminism(identifiedTransitions);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return identifiedTransitions;

    }

     public ArrayList<Transition> findTransitions( Configuration c, String e){
            
            System.out.println("Finding the transitions suitable for event : "+e + " for configuration : "+c);
            ArrayList<Transition> allTransitionsInConfiguration=new ArrayList<Transition>();
            ArrayList<State> allSourceStatesInConfiguration=new ArrayList<State>();
            
            ArrayList<Transition> selectedTransitions=new ArrayList<Transition>();
            
            allSourceStatesInConfiguration.addAll(c.getActiveStates());
            // adding all the superstates of the states in the current configuration
            for(State s:c.getActiveStates()){
                //System.out.println("State : "+s);
                if(s.transitions!=null)
                     allTransitionsInConfiguration.addAll(s.transitions);
                ArrayList<State> superstates=new ArrayList<State>();
                superstates.addAll(s.getAllSuperstates());
                allSourceStatesInConfiguration.addAll(superstates);  
                for(State sup:superstates){
                    allTransitionsInConfiguration.addAll(sup.transitions);                    
                }
            }
            //System.out.println("Source states found : ");
            /*for(State s:allSourceStatesInConfiguration){
                System.out.println(s.name);
            }*/
            //System.out.println("All transitions found: "+allTransitionsInConfiguration);
            for(Transition t: allTransitionsInConfiguration){
                //System.out.println("source state : "+ t.getSource().name + "in "+ allSourceStatesInConfiguration);
                if(allSourceStatesInConfiguration.contains(t.getSource())&&t.trigger.equals(e))
                {
                    if(!selectedTransitions.contains(t))
                        selectedTransitions.add(t);
                }
            }
            return selectedTransitions;
        }
        public void findNonDeterminism(List<Transition> activeTransitions){
             List<State> activeStates=new ArrayList<State>();
             for(Transition t:activeTransitions){
                    //For each transition, identify its source and destination and compute the program
                    //case 1 - two transitions are from a state and its ancestor
                    //case 2 - two transitions are from different regions of a shell state
                    activeStates.add(t.getSource());
                }

                //System.out.println("Active states are : "+activeStates);
                for(State s: activeStates){
                    ArrayList<State> ancestorStates=new ArrayList<State>();
                    ancestorStates.addAll(s.getAllSuperstates());

                    ancestorStates.retainAll(activeStates);
                    if(ancestorStates.size()!=0){
                        System.out.println("Non Determinism Detected between outgoing transitions of two states :" +s.name+" and ");
                        System.exit(0);
                    }
                    //if(checkStates.contains(s.getAllSuperstates()))
                }
        }
    public ExecutionSequence iterateSequence(ExecutionSequence exseq){
        System.out.println("Iterate : "+exseq.getClass());
        if(exseq.hasNext()){
            System.out.println("Hello : ");
            return iterateSequence(exseq.next);
        }
        else if(exseq==null) {
            System.out.println("Hello Else If: "+exseq);
            return null;
        }
        else{
             System.out.println("Hello Else: "+exseq);
             return exseq;
        }
            
    }
    public Configuration computeConfiguration(ExecutionSequence exseq, Configuration config){
        //System.out.println("computeConfiguration : " +exseq);
         iterateSequence(exseq);
         if(exseq!=null&&exseq.next!=null){
            System.out.println("Inside if :"+exseq.next);
    
            return computeConfiguration(exseq.next, config);
        }
        else{
            System.out.println("inside else : "+exseq.getClass()+ exseq.next);
            if(exseq instanceof SequentialExecutionSequence){
                config.programpoints.add(((SequentialExecutionSequence)exseq).getLastProgramPoint());
                System.out.println("inside "+config);

                //return config;
                 }
            else if(exseq instanceof ConcurrentExecutionSequence){
                for(ExecutionSequence es: ((ConcurrentExecutionSequence)exseq).sequencelist){
                        System.out.println("es : "+es);
                        //config.programpoints.add((computeConfiguration(es, config)).getLastProgramPoint());
                        return computeConfiguration(es, config);
                }
            }
            
        }
        System.out.println(config);

        return config;
    }
    public Configuration takeTransition(Configuration currentconfig, Transition t)
    {
        System.out.println("*********************************");
        
        System.out.println("Take Transition : "+t.name);
        Configuration config=currentconfig;
 
        try{
            System.out.println("Transition : "+t.name +" : Configuration : "+config);
            if(t.name.equals(tinit)&&t.getSource()==null){
                //Transition is going to initialize the statechart
                //((DotProgramPoint)(currentconfig.getProgramPoints()).get(0)).setState(this.statechart);
                
                ArrayList<State> listofstates=new ArrayList<State>();
                listofstates.add(this.statechart);
               // computeActionDefaultEntry(currentconfig, listofstates,new SequentialExecutionSequence());
                ExecutionSequence initialExecutionSequence=computeActionDefaultEntryForState(currentconfig, this.statechart,new SequentialExecutionSequence());
                System.out.println("tinit : "+initialExecutionSequence);
                Configuration newconfig=computeConfiguration(initialExecutionSequence,new Configuration());
                //Configuration newconfig=executeAction(currentconfig,initialExecutionSequence);
                System.out.println("Configuration after tinit:"+newconfig.programpoints);
                config=new Configuration(newconfig.programpoints);
            }
            else{
                //Compute ExitActionSequence

                ExecutionSequence exitSequence=computeExitExecutionSequence(currentconfig,t.lub());
                //Compute TransitionSequence
                ExecutionSequence transitionActionSequence=computeTransitionActionSequence(t);
                
                //Compute EntryActionSequence
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Return config : "+config);
        return config;
    }


    public ExecutionSequence computeTransitionActionSequence(Transition t)
    {
        SequentialExecutionSequence actionSequence=new SequentialExecutionSequence();
        try{
                actionSequence=(SequentialExecutionSequence)addProgramPoints(actionSequence,null,t, ActionType.TRANSITION_ACTION);
                System.out.println("Transition action sequence : "+actionSequence);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return actionSequence;
    }
    
    public Configuration computeEntryExecutionSequence(){
        Configuration config=null;
        return config;
    }

    public Configuration executeAction(Configuration currentconfig, ExecutionSequence exseq)
    {
        Configuration config=currentconfig;
         Configuration returnConfiguration=new Configuration();
        try{
            System.out.println("Printing execute action method");
            // perform executing the action in each step

            // compute the new configuration

            //System.out.println(exseq);
           
            if(exseq instanceof SequentialExecutionSequence){
                System.out.println("SequentialExecutionSequence Detected");
                if(((SequentialExecutionSequence)exseq).hasNext()){
                    //has a concurrent executionsequence
                    System.out.println("ConcurrentExecutionSequence Detected");

                    ConcurrentExecutionSequence conexseq=((SequentialExecutionSequence)exseq).next;
                    List<SequentialExecutionSequence> finalSeq=conexseq.getFinalProgramPointInSequence(conexseq, new ArrayList<SequentialExecutionSequence>());
                    for(SequentialExecutionSequence ses:finalSeq){
                        List<ProgramPoint> programpoints=((SequentialExecutionSequence)ses).points;
                        //add the last program point in the sequence to the return configuration
                        returnConfiguration.programpoints.add(programpoints.get(programpoints.size()-1));
                       
                    }
                }
                else{
                    List<ProgramPoint> programpoints=((SequentialExecutionSequence)exseq).points;
                    //add the last program point in the sequence to the return configuration
                    returnConfiguration.programpoints.add(programpoints.get(programpoints.size()-1));
                }
            }
            else if(exseq instanceof ConcurrentExecutionSequence){

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return returnConfiguration;
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
    public ExecutionSequence computeExitActionSequence(Configuration config, Transition trans){
        ExecutionSequence returnsequence=null;
        if(config.programpoints.size()==1){
            //Sequential execution in progress
            SequentialExecutionSequence ses=new SequentialExecutionSequence();

        }
        else{
            //Concurrent execution in progress
            ConcurrentExecutionSequence ces=new ConcurrentExecutionSequence();

        }
        return returnsequence;
    }

    public ExecutionSequence computeActionDefaultEntryForState(Configuration currentconfig, State entryState, ExecutionSequence actionSequence)
    {
        
        try{
                       
           System.out.println("\n== entry states coming are : "+entryState.name);
           State state=entryState;
                
                if(state instanceof ast.Shell){
                    System.out.print("Shell detected:  ");
                    
                    actionSequence=addProgramPoints(actionSequence,state,null, ActionType.STATE_ENTRY_ACTION);
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
                    actionSequence=addProgramPoints(actionSequence,state,null, ActionType.STATE_ENTRY_ACTION);

                    //entryStates.add(state.getAllSubstates().get(0));
                    //entryStates.remove(state);
                    System.out.println("Composite detected:  "+state.name);
                    computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionSequence);
                    

                    
                }
                else{
                    actionSequence=addProgramPoints(actionSequence,state,null,ActionType.STATE_ENTRY_ACTION);

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
    public ExecutionSequence addProgramPoints(ExecutionSequence actionSequence, State state, Transition t, String actionType){
        //Adding program points
                    StatementList stmtlist=null;
                    if(actionSequence instanceof SequentialExecutionSequence){
                        if(actionType.equals(ActionType.STATE_ENTRY_ACTION)){
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new EntryBeginProgramPoint(actionType+"_Begin_"+state.name,state));
                            stmtlist=(StatementList)state.entry;
                        }   
                        else if(actionType.equals(ActionType.STATE_EXIT_ACTION)){
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new ExitBeginProgramPoint(actionType+"_Begin_"+state.name,state));
                            stmtlist=(StatementList)state.exit;
                        }  
                        else if(actionType.equals(ActionType.TRANSITION_ACTION)){
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new ActionBeginProgramPoint(actionType+"_Begin_"+t.name));
                            stmtlist=(StatementList)t.action;
                        }
                            
                        for(Statement stmt:stmtlist.getStatements()){
                            System.out.println("Statement detected : "+stmt);
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new StatementProgramPoint(stmt.toString()));
                        
                        }

                        if(actionType.equals(ActionType.STATE_ENTRY_ACTION))
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new EntryEndProgramPoint(actionType+"_End_"+state.name,state));
                        else if(actionType.equals(ActionType.STATE_EXIT_ACTION))
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new ExitEndProgramPoint(actionType+"_End_"+state.name,state));
                        else if(actionType.equals(ActionType.TRANSITION_ACTION))
                            ((SequentialExecutionSequence)actionSequence).addProgramPoint(new ActionEndProgramPoint(actionType+"_End_"+t.name));
                        
                        
                    }
            return actionSequence;

    }
   /* public List<State> getDefaultAtomicSubStateSequence(ArrayList<State> substate, ExecutionSequence executionSequence){
            
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

        }*/
         public ExecutionSequence computeExitExecutionSequence(Configuration config, State LUB){
            System.out.println("Computing Exit execution sequence");
            ExecutionSequence es=null;
            ArrayList<State> activestates=config.getActiveStates();
            if(activestates.size()>1){
                //concurrent execution going on
                //multiple execution sequences should be calculated
                //CASE 1 - all program points belong to same shell state
                    //* Exit until shell and then create a sequential execution sequence as next
                //CASE 2 - program points belong to different shell states
                    //* Group the program points to the shell states and compute the exit sequences
                // In both CASE1 and CASE 2 - output is a concurrent execution sequence with a sequential execution sequence up until the lub
                ConcurrentExecutionSequence ces=new ConcurrentExecutionSequence();
                Set<State> shellAncestors=this.statechart.shellLubofStates(activestates);
                if(shellAncestors.size()==1){
                    System.out.println("Single shell ancestor");
                }
                else{
                    System.out.println("Multiple shell ancestor");
                }
               // if(this.statechart.lub)
            }
            else{
                //single state to exit - 
                //is it possible to exit a shell state when number of program points is 1? - No
                //contained within OR state, so computeSequenceUntilLUB can be used to find a sequence?
                State s=activestates.get(0);
                es=computeSequenceUntilLUB(s, LUB, new SequentialExecutionSequence());
                System.out.println(es);            
            }
        return es;
         
         }
         
         public ExecutionSequence computeExitExecutionSequenceOld(Configuration config, State LUB){
         
            // List<ExecutionSequence> exitExecutionSequence=new ArrayList<ExecutionSequence>();
            if(config.getActiveStates().size()>1){
                //concurrent execution ongoing..
                ConcurrentExecutionSequence ces=new ConcurrentExecutionSequence();
                ArrayList<State> shellparents=new ArrayList<State>();
                
                for(State exitstate:config.getActiveStates()){
                    SequentialExecutionSequence ses=new SequentialExecutionSequence();
                    ses.stateList.addAll(exitUntilShell(exitstate,  new ArrayList<State>()));
                    shellparents.add(ses.stateList.get(ses.stateList.size()-1));
                    for (State s:ses.stateList){
                        //ses.seqStatements.add(s.exit);
                    }
                    ces.sequencelist.add(ses);
                }
                
                //exitExecutionSequence.add(ces);
                SequentialExecutionSequence ses=new SequentialExecutionSequence();
                State shellState=(ces.sequencelist.get(0)).stateList.get((ces.sequencelist.get(0)).stateList.size()-1).getSuperstate();
                System.out.println("Shell state : "+shellState.getFullName());
             
                ses.stateList.addAll(exitUntilLub(shellState, LUB, new ArrayList<State>()));
                System.out.println("ses.statelist : "+ses.stateList.size());
                
                //exitExecutionSequence.add(ses);
                ces.next=ses;
                return ces;
            }
            else{
                System.out.println("inside else");
                SequentialExecutionSequence ses=new SequentialExecutionSequence();
                ses.stateList.addAll(exitUntilLub(config.activestates.get(0), LUB, ses.stateList));
                //exitExecutionSequence.add(ses);
                return ses;
            }
            //System.out.println("Execution Sequence : "+exitExecutionSequence);

        }
        public ArrayList<State> exitUntilShell(State s, ArrayList<State> returnList){

            if(s instanceof ast.Shell)
                return returnList;
            else{
                System.out.println("Adding (until shell) : "+s.getFullName());
                returnList.add(s);
                return exitUntilShell(s.getSuperstate(), returnList);
            }
        }
        public ArrayList<State> exitUntilLub(State s, State lub, ArrayList<State> returnList){
            System.out.println("Adding (until lub) : "+s.getFullName()+(s == lub));
            if(s == lub)
                return returnList;
            else{
                returnList.add(s);
                return exitUntilLub(s.getSuperstate(),lub, returnList);
            }
        }
        public SequentialExecutionSequence computeSequenceUntilLUB(State s, State lub, SequentialExecutionSequence ses){
            if(s == lub)
                return ses;
            else{
                ses=(SequentialExecutionSequence)addProgramPoints(ses,s,null, ActionType.STATE_EXIT_ACTION);           
                return computeSequenceUntilLUB(s.getSuperstate(),lub, ses);
            }

        }

}