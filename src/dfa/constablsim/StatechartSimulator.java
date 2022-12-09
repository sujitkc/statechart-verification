package constablsim;
import ast.*;
import java.util.*;
import constablsim.ast.*;
import constablsim.ast.connectors.*;

public class StatechartSimulator extends Simulator {
    private Statechart statechart = null;
    private List<String> eventQueue=null;
    private Configuration activeconfig=new Configuration();
    
    
    EventQueue eq=new EventQueue();
    private final String tinit=EventQueue.tinit;
    public StatechartSimulator(Statechart sc){
        this.statechart=sc;
        this.simulate();
    }
    public void simulate(){
        System.out.println("simulate inside Statechart simulator");

        initialize();
        for(String ev : eq.eventQueue){
            consumeEvent(ev);   
        }
    }
    public void initialize(){
        //initialize the statechart with tinit transition
        System.out.println("initialize");
        List<Transition> identifiedTransitions=new ArrayList<Transition>();
        Transition initTransition=new Transition(tinit,null,null,null,null,null);
        identifiedTransitions.add(initTransition);
        //computeCFA();
        computeCode(activeconfig,identifiedTransitions);
    }
    public void computeCFA(){
        System.out.println("Computing CFAs... ");
        /* yet to be implemented */
    }
    public void consumeEvent(String event){
        //detectNonDeterminism
            //find transitions
            List<Transition> transitions=findTransitions(activeconfig,event);
            System.out.print(transitions.size()+" Transitions found - ");
            for(Transition t:transitions)
                System.out.print(t.name+", ");
            System.out.println();
            findNonDeterminism(transitions);
            //if no non determinism detected
            if(transitions.size()>0){             
                activeconfig=takeTransitions(activeconfig,transitions);
                }
        //computeCode

    }
    public void computeCode(Configuration currentconfig,List<Transition> transitions){
        for(Transition t:transitions){
            if(t.name.equals(tinit)){
                currentconfig.addState(statechart);
                while(!currentconfig.isStable())
                    currentconfig=computeDefaultEntry(currentconfig);
                // for the stable config
                    computeDefaultEntry(currentconfig);
                
                //CodeSimulator cs=new CodeSimulator(cfalist, forklist, joinlist, seqlist);
                CodeSimulator cs=new CodeSimulator(codenodelist);
                Seq sq=getSeqfromList("start");
                //CFA startCFA=getNextCFAtoSeqConnector(cfalist,sq);
                List<CodeNode> nodelist=getNextNode(sq);
                CFA startCFA=(CFA)nodelist.get(0);
                cs.execute(startCFA);
                activeconfig=currentconfig;
            }
            else{
                
            }
        }
    }
    public Configuration computeDefaultEntry(Configuration currentconfig)
    {
        Configuration newconfig=new Configuration();
        for (State entryState : currentconfig.getCurrentStates()){
            if(entryState instanceof ast.Shell){
                System.out.println( "Shell state : "+entryState.getFullName());
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                Seq f=getSeqfromList(entryState.getSuperstate().getFullName());
                cfa.addPrev(f);
                //cfalist.add(cfa);  
                codenodelist.add(cfa);
                newconfig.addAllState(entryState.states);          
            }
            else if(entryState.states.size()>0){
                System.out.println( "Composite state : "+entryState.getFullName());
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                if(entryState instanceof ast.Statechart){
                    //do nothing
                    Seq s=getSeqfromList("start");
                    cfa.addPrev(s);
                }
                else if(entryState.getSuperstate() instanceof ast.Shell){
                    Fork f=getForkfromList(entryState.getSuperstate().getFullName());
                    cfa.addPrev(f);
                }
                else{
                    //System.out.println()
                    Seq s=getSeqfromList((entryState.getSuperstate()).getFullName());
                    cfa.addPrev(s);
                }
                //cfalist.add(cfa); 
                codenodelist.add(cfa);
                newconfig.addState(entryState.states.get(0)); 
            }
            else{
                System.out.println( "Atomic state : "+entryState.getFullName());
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                Seq f=getSeqfromList(entryState.getSuperstate().getFullName());
                cfa.addPrev(f);
                //cfalist.add(cfa);
                codenodelist.add(cfa);
            }

        }
        //System.out.println()
        return newconfig;
        
    }
  
    // public CFA computeCFADefaultEntry(Configuration currentconfig, State entryState)
    // {      
    //     try{                  
    //        System.out.println("\n== entering state : "+entryState.name);
    //        State state=entryState;    
    //             if(state instanceof ast.Shell){
    //                 System.out.print("Shell detected:  "+state.name);  
    //                 CFA actionBlock=StatementToCFA.convertToCFA(state.entry);
    //                 System.out.println("action block cfa 1: "+actionBlock);
    //                 Fork f=new Fork();
    //                 actionBlock.addNext(f);
    //                 List<State> newEntryStates=state.getAllSubstates();
    //                 for(State s:newEntryStates){
    //                     System.out.print(s.name+", ");
    //                     //this is wrong
    //                     actionBlock=computeActionDefaultEntryForState(currentconfig,s);
    //                     System.out.println("action block cfa 2: "+actionBlock);
    //                     f.addNext(actionBlock);
    //                 }
    //                 System.out.println("action block cfa 2: "+actionBlock);                    
    //             }else if(state.getAllSubstates().size()>0){
                    
    //                 System.out.println("Composite detected:  "+state.name);
    //                 CFA actionBlock=StatementToCFA.convertToCFA(state.entry);
    //                 Seq s=new Seq();
    //                 actionBlock.setNext(s);
    //                 s.setNext(computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0)));
                    
    //                 System.out.println("action block cfa composite: "+actionBlock);
    //                 //actionBlock.addAll(computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionBlock));
                    

                    
    //             }
    //             else{
    //                 //found atomic state
    //                 System.out.println("Atomic detected:  "+state.name);
    //                 //actionBlock=computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionBlock);
    //                 CFA actionBlock=StatementToCFA.convertToCFA(state.entry);
    //                 System.out.println("action block cfa composite: "+actionBlock);

    //                 activeconfig.addState(state);
    //                // actionSequence=addProgramPoints(actionSequence,state,null,ActionType.STATE_ENTRY_ACTION);

                   
    //             }
           
    //        if(state==entryState){
    //             //System.out.println("ActionSequence.next : "+actionSequence.next); 
    //             return actionBlock;
    //        }
    //        else{
    //            return computeActionDefaultEntryForState(currentconfig,entryState);
    //         }
          
    //     }
    //     catch(Exception e){
    //         e.printStackTrace();
    //     }
    //     return actionBlock;
    // }
    public ArrayList<Transition> findTransitions( Configuration c, String e){
            
        String str="Finding the transitions suitable for event : "+e + " for configuration : "+c.getCurrentStatesName();
        
        System.out.println(str);
        ArrayList<Transition> allTransitionsInConfiguration=new ArrayList<Transition>();
        ArrayList<State> allSourceStatesInConfiguration=new ArrayList<State>();
        
        ArrayList<Transition> selectedTransitions=new ArrayList<Transition>();
        
        allSourceStatesInConfiguration.addAll(c.getCurrentStates());
        // adding all the superstates of the states in the current configuration
        for(State s:c.getCurrentStates()){
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
    public Configuration takeTransitions(Configuration currentconfig, List<Transition> tlist)
    {
        System.out.println("*********************************");
        // cfalist.clear();
        // forklist.clear();
        // joinlist.clear();
        // seqlist.clear();
        codenodelist.clear();
        System.out.println("Take Transition : ");
        for(Transition t:tlist)
            System.out.print(t.name);
        Configuration config=currentconfig;
        
        try{
            Transition t=tlist.get(0); // still concurrent transitions not handled
            System.out.println(" : Configuration : "+config.getCurrentStatesName());
            Connector con;
            if(tlist.size()>0)
                con=new Fork("start");
            else
                con=new Seq("start");
                //Compute ExitCFAList
                computeExitCFAList(currentconfig,t.lub());
                //ExecutionBlock exitBlock=computeExitExecutionBlock(currentconfig,t.lub());
                //System.out.println ("Exit Execution Block : "+exitBlock);
                //Compute TransitionBlock -- Done
                //ExecutionBlock transitionActionBlock=computeTransitionAction(t);
                //System.out.println ("Transition Action Sequence : "+transitionActionBlock);
                //Compute EntryActionBlock
                 //ExecutionBlock entryBlock=computeEntryExecutionBlock(new SequentialExecutionBlock(),currentconfig,t.lub(), t.getDestination());
                // System.out.println ("Entry Execution Block : "+entryBlock);
                 
                 //ConstaBLActionExecutor.executeAction(exitBlock,null);
                
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Return config : "+config);
        return currentconfig;
    }
    public void computeExitCFAList(Configuration config, State LUB){
    
      

        List<State> activestates=config.getCurrentStates();
        
        State exitancesestor=null;
        for(State s: LUB.states){
           // System.out.println("LUB :"+s.name);
            for(State as:activestates){
                if(as.getAllSuperstates().contains(s))
                    exitancesestor=s;
            }
        }
        if(exitancesestor!=null)
            System.out.println("Exit Ancestor LUB-1 :"+exitancesestor.name);

        if(activestates.size()>1){
            Fork f=new Fork("start");
            State shellancestor=getShellAncestor(activestates.get(0));
            for(State s : activestates){
                
                computeSequenceUntilLUB(s, shellancestor);
                
            }
            Join j=getJoinfromList(shellancestor.name);
            for(State s : shellancestor.states)
                j.addPrev(getCFAfromList(s.name+"_X"));
            // This is not completed    
        }
        else{
            //single state to exit - 
            //is it possible to exit a shell state when number of program points is 1? - No
            //contained within OR state, so computeSequenceUntilLUB can be used to find a sequence?
            State s=activestates.get(0);
            computeSequenceUntilLUB(s, LUB);
            //System.out.println(es);            
        }
    
     
     }
     public void computeSequenceUntilLUB(State s, State lub){
        //System.out.println("State : "+s.name);
        //System.out.println("lub : "+lub.name);
        
        if(s == lub || s==null)
            return;
        else{
            Seq seq=new Seq(s.name);
            seq.addPrev(getCFAfromList(s.name+"_X"));
            CFA cfa=StatementToCFA.convertToCFA(s.exit,s.name+"_X");
            cfa.addPrev(seq);
            //cfalist.add(cfa);
            //seqlist.add(seq);
            codenodelist.add(cfa);
            codenodelist.add(seq);
           // ses=(SequentialExecutionBlock)addProgramPoints(ses,s,null, ActionType.STATE_EXIT_ACTION);           
            computeSequenceUntilLUB(s.getSuperstate(),lub);
        }

    }
}
