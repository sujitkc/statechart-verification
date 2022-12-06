package constablsim;
import ast.*;
import java.util.*;
import constablsim.ast.*;
import constablsim.ast.connectors.*;

public class StatechartSimulator extends Simulator {
    private Statechart statechart = null;
    private List<String> eventQueue=null;
    private Configuration activeconfig=new Configuration();
    private List<CFA> cfalist=new ArrayList<CFA>();
    private List<Fork> forklist=new ArrayList<Fork>();
    private List<Join> joinlist=new ArrayList<Join>();
    private List<Seq> seqlist=new ArrayList<Seq>();
    
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
        computeCFA();
        computeCode(activeconfig,identifiedTransitions);
    }
    public void computeCFA(){
        System.out.println("Computing CFAs... YTI ");
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
                //printing current CFA list
                for(CFA cfa: cfalist)
                    System.out.println(cfa);
                for(Fork cfa: forklist)
                    System.out.println(cfa);
                for(Join cfa: joinlist)
                    System.out.println(cfa);
                for(Seq cfa: seqlist)
                    System.out.println(cfa);
                
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
                System.out.println( "Shell state : "+entryState.name);
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.name+"_N");
                Seq f=getSeqfromList(entryState.getSuperstate().name);
                cfa.addPrev(f);
                cfalist.add(cfa);  
                newconfig.addAllState(entryState.states);          
            }
            else if(entryState.states.size()>0){
                System.out.println( "Composite state : "+entryState.name);
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.name+"_N");
                if(entryState instanceof ast.Statechart){
                    //do nothing
                }
                else if(entryState.getSuperstate() instanceof ast.Shell){
                    Fork f=getForkfromList(entryState.getSuperstate().name);
                    cfa.addPrev(f);
                }
                else{
                    //System.out.println()
                    Seq f=getSeqfromList((entryState.getSuperstate()).name);
                    cfa.addPrev(f);
                }
                cfalist.add(cfa); 
                newconfig.addState(entryState.states.get(0)); 
            }
            else{
                System.out.println( "Atomic state : "+entryState.name);
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.name+"_N");
                Seq f=getSeqfromList(entryState.getSuperstate().name);
                cfa.addPrev(f);
                cfalist.add(cfa);
            }

        }
        //System.out.println()
        return newconfig;
        
    }
    public Fork getForkfromList(String name){
        for(int i=0;i<forklist.size();i++){
            if(forklist.get(i).name.equals(name))
                return forklist.get(i);
        }
        Fork f=new Fork(name);
        CFA cfa = getCFAfromList(name+"_N");
        f.setPrev(cfa);
        forklist.add(f);
        return f;
    }
    public Seq getSeqfromList(String name){
        for(int i=0;i<seqlist.size();i++){
            if(seqlist.get(i).name.equals(name))
                return seqlist.get(i);
        }
        Seq s=new Seq(name);
        CFA cfa = getCFAfromList(name+"_N");
        s.setPrev(cfa);
        seqlist.add(s);
        return s;
    }
    public Join getJoinfromList(String name){
        for(int i=0;i<joinlist.size();i++){
            if(joinlist.get(i).name.equals(name))
                return joinlist.get(i);
        }
        Join s=new Join(name);
        joinlist.add(s);
        return s;
    }
    public CFA getCFAfromList(String name){
        for(int i=0;i<cfalist.size();i++){
            if(cfalist.get(i).name.equals(name))
                return cfalist.get(i);
        }
       
        return null;
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
            
        System.out.println("Finding the transitions suitable for event : "+e + " for configuration : "+c);
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
}
