package constabl;
import ast.*;
import constabl.consimulator.*;

import java.util.*;

public class ConStaBLSimulator{
     private Statechart statechart = null;
     private Configuration activeConfiguration;
     private Configuration sourceConfiguration, destConfiguration;
     private Queue<String> eventQueue = new LinkedList<String>();
     private List<ExecutionSequence> exitExecutionSequence=new ArrayList<ExecutionSequence>();
     private List<ExecutionSequence> entryExecutionSequence=new ArrayList<ExecutionSequence>();
     
      public ConStaBLSimulator(Statechart statechart) throws Exception
        {
            
            this.statechart=statechart;
            ArrayList<State> astate=new ArrayList<State>();
            //add the main statechart
            astate.add(this.statechart);
            //recursively get the atomic state of the statechart
            astate.addAll(this.getDefaultAtomicSubState(astate));
            //remove statechart from the list and retain only the atomic states.
            astate.remove(this.statechart);
            
            System.out.println("Atomic substates are :"+ astate);

            this.activeConfiguration=new Configuration(astate);
            this.sourceConfiguration=new Configuration(null);
            this.destConfiguration=new Configuration(null);
            
            /* adding events into the queue */
            this.eventQueue.add("init");
            this.eventQueue.add("e1");
            this.eventQueue.add("e2");
            
            System.out.println("------ Simulating ConStaBL Simulator --------\n");
            //System.out.println("Augumenting statechart with dummy state and init event : ");
            //this.statechart=augumentStatechart();   
            // I will work on augumenting later...

            //System.out.println(this.statechart);         
            System.out.println("Event Queue : "+this.eventQueue.toString());
            //String e;
            

            for(String e : eventQueue){
                //System.out.println("Selected event : "+e);
                ArrayList<Transition> activeTransitions=findTransitions(e,activeConfiguration);
                //System.out.println("Transitions identified: "+activeTransitions);
                
                ArrayList<State> activeStates=new ArrayList<State>();
                /*
                Method to find the Sequence of states to be exited and Sequence of states to be entered of each transition.
                * Given a transition or a destination state - to dynamically bubble down to that state, we need to keep track of the state, as well as whether the state we are currently entering into is one of its ancestors.
                * If the state that we chose to enter into is not one of the ancestor of destination, we need to pick another state in that OR combination
                * But this is not so easy to achieve because of the complexity. Consider destination of a transition is a state which is the 100th in the sequence of a parent that you are currently entering - this blows up the check needed
                * So we need to approach this in bottomup fashion
                * ofcourse we have an LUB function that gives the state which the transition is contained in.
                * List of states to be exited - current config --- up until Ancestor(src(t))<LUB
                * List of states to be entered - Ancestor(d) ---- down until the dest(t)
                */
                /* Taking one transition and performing the test*/
                if(activeTransitions.size()>0){
                        Transition t=activeTransitions.get(0);
                        computeExitExecutionSequence(activeConfiguration, t.lub());
                        //Identifying all the states that the transition should sequentially exit
                        State lub=t.lub();

                        //System.out.println("identified lub : "+lub.getFullName());
                       
                        //Current configuration of the statemachine
                        State s = t.getSource();
                        boolean shellexitflag=false;
                        while(s!=lub){
                            //exitstates.add(s);
                            s = s.getSuperstate();
                            if(s instanceof ast.Shell){
                                shellexitflag=true;
                                break;
                            }
                                
                        }
                        
                        if(shellexitflag){
                            System.out.println("Shell exit has to happen");
                            for(State active : this.activeConfiguration.activestates){
                                System.out.println("Active configuration : "+active.name);
                                
                            }
                        }

                        System.out.println("Identified for event :"+e+" : transition :"+t.name);
                        /*System.out.println("Exit the states : ");
                        for(State exitstate:exitstates)
                            System.out.print(exitstate.name+", ");
                        */
                    }
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
                        System.out.println("Non Determinism Detected between outgoing transitions of two states :" +s.name+" and "+ ancestorStates);
                        System.exit(0);
                    }
                    //if(checkStates.contains(s.getAllSuperstates()))
                }

            }


            

        
        }
        public void computeExitExecutionSequence(Configuration config, State LUB){
            if(config.activestates.size()>1){
                ConcurrentExecutionSequence ces=new ConcurrentExecutionSequence();
                for(State exitstate:config.activestates){
                    SequentialExecutionSequence ses=new SequentialExecutionSequence();
                    ses.stateList.addAll(exitUntilShell(exitstate,  new ArrayList<State>()));
                    ces.sequencelist.add(ses);
                }
                exitExecutionSequence.add(ces);
                SequentialExecutionSequence ses=new SequentialExecutionSequence();
                State shellState=(ces.sequencelist.get(0)).stateList.get((ces.sequencelist.get(0)).stateList.size()-1).getSuperstate();
                System.out.println("Shell state : "+shellState.getFullName());
             
                ses.stateList.addAll(exitUntilLub(shellState, LUB, new ArrayList<State>()));
                System.out.println("ses.statelist : "+ses.stateList.size());
                
                exitExecutionSequence.add(ses);
            }
            else{
                System.out.println("inside else");
                SequentialExecutionSequence ses=new SequentialExecutionSequence();
                ses.stateList.addAll(exitUntilLub(config.activestates.get(0), LUB, ses.stateList));
                exitExecutionSequence.add(ses);
            }
            System.out.println("Execution Sequence : "+exitExecutionSequence);

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
        public State getLUB_Source_Ancestor(){
            return null;
        }
        public List<State> getDefaultAtomicSubState(ArrayList<State> substate){
            ArrayList<State> returnsubstates=new ArrayList<State>();
            int i=0;
            
            for(i=0;i<substate.size();i++){
            //    System.out.println("size :"+(substate.get(i)).states.size());
    
                if((substate.get(i)).states.size()!=0)
                {
                    if(substate.get(i) instanceof ast.Shell)
                        returnsubstates.addAll(i,(substate.get(i)).states);
                    else    
                        returnsubstates.add(i,(substate.get(i)).states.get(0));
                
                }
                else
                    returnsubstates.add(i,substate.get(i));
            }
            //System.out.println("returnsubstates :"+returnsubstates);
            
            if(returnsubstates.equals(substate))
                return returnsubstates;
            else
                return getDefaultAtomicSubState(returnsubstates);

        }
        public ArrayList<Transition> findTransitions(String e, Configuration c){
            //System.out.println("Finding the transitions suitable for event : "+e + " for configuration : "+c);
            ArrayList<Transition> allTransitionsInConfiguration=new ArrayList<Transition>();
            ArrayList<State> allSourceStatesInConfiguration=new ArrayList<State>();
            
            ArrayList<Transition> selectedTransitions=new ArrayList<Transition>();
            
            allSourceStatesInConfiguration.addAll(c.activestates);
            // adding all the superstates of the states in the current configuration
            for(State s:c.activestates){
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
                    selectedTransitions.add(t);
                }
            }
            return selectedTransitions;
        }
        public void takeTransition(String e){
            System.out.println("Taking transition for the event : "+ e);
            
        }
        public Statechart augumentStatechart(){
                //Creating a Dummy state
                String            name="dummy";
                DeclarationList   declarations=new DeclarationList();
                Statement         entry=null;
                Statement         exit=null;
                List<State>       states=new ArrayList<State>();
                List<Transition>  transitions=new ArrayList<Transition>();
                BooleanConstant   history=new BooleanConstant(false);
                State dummy=new State(
                    name,
                    declarations,
                    entry,
                    exit,
                    states,
                    transitions,
                    history
                    );
                dummy.setStatechart(null);
                //Dummy state creation ends
                this.statechart.states.add(dummy);
                
                //Add Dummy state to the statechart

                String tname = "tinit";
                Name tsrc= new Name("dummy");
                Name tdest= new Name(this.statechart.name);
                String trigger="init";
                Expression guard=new BooleanConstant(true);
                Statement action=new ExpressionStatement(new BooleanConstant(true));
                Transition init=new Transition(name,tsrc,tdest,trigger,guard,action);
                this.statechart.transitions.add(init);
                //this.statechart.addEvent("init");
                return this.statechart;
        }
        public void addEvent(String e)
        {
            this.eventQueue.add(e);
        }
}