package constablsim;
import ast.*;
import java.util.*;
import constablsim.ast.*;
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
        System.out.println("simulate");

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
        computeCode(activeconfig,identifiedTransitions);
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
                computeActionDefaultEntryForState(currentconfig,statechart,new CFA());
            }
            else{
                
            }
        }
    }
    public CFA computeActionDefaultEntryForState(Configuration currentconfig, State entryState, CFA actionBlock)
    {      
        try{                  
           System.out.println("\n== entering state : "+entryState.name);
           State state=entryState;    
                if(state instanceof ast.Shell){
                    System.out.print("Shell detected:  "+state.name);
                    
                    //actionSequence=addProgramPoints(actionSequence,state,null, ActionType.STATE_ENTRY_ACTION);
                    actionBlock=StatementToCFA.convertToCFA(state.entry, actionBlock);
                    actionBlock.addSuccessor(actionBlock.getFinalNode(), new Fork(), null);
                    List<State> newEntryStates=state.getAllSubstates();
                    for(State s:newEntryStates){
                        System.out.print(s.name+", ");
                        //this is wrong
                        actionBlock=computeActionDefaultEntryForState(currentconfig,s,actionBlock);
                        
                    }
                    actionBlock.addSuccessor(actionBlock.getFinalNode(), new Join(), null);
                 
                  
                    
                    
                }else if(state.getAllSubstates().size()>0){
                    
                    System.out.println("Composite detected:  "+state.name);
                    computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionBlock);
                    //actionBlock.addAll(computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionBlock));
                    

                    
                }
                else{
                    //found atomic state
                    System.out.println("Atomic detected:  "+state.name);
                    activeconfig.addState(state);
                   // actionSequence=addProgramPoints(actionSequence,state,null,ActionType.STATE_ENTRY_ACTION);

                   
                }
           
           if(state==entryState){
                //System.out.println("ActionSequence.next : "+actionSequence.next); 
                return actionBlock;
           }
           else{
               return computeActionDefaultEntryForState(currentconfig,entryState,actionBlock);
            }
          
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return actionBlock;
    }
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
