package constabl.simulator;
import ast.*;
import constabl.ast.*;

import java.util.*;
public class StatechartSimulator extends Simulator{
    private List<String> eventQueue=null;
    private Configuration activeconfig=new Configuration();
    
    
    EventQueue eq=new EventQueue();
    private final String tinit=EventQueue.tinit;
    public StatechartSimulator(Statechart sc){
        super(sc);
        simulate();
    }
    
    public void simulate(){
        enterDefaultState(null, statechart);
        System.out.println("Execute node list : "+execnodelist);
        for(String ev : eq.eventQueue){
            consumeEvent(ev);   
        }
    }
    public ArrayList<Transition> findTransitions(Configuration c, String e){
            
        String str="Finding the transitions suitable for event : "+e ;
        if(c!=null){
            str+=" for configuration : "+c.getCurrentStatesName();
        }
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
                             
                    takeTransitions(transitions);
                    
                }
                System.out.println("Execute node list : "+execnodelist);
    }
    public void takeTransitions(List<Transition> tlist)
    {
        for(Transition t:tlist){

            State lub=t.lub();
            //exit state
            State source=t.getSource();
            List<State> exancestors=source.getAllSuperstates();
            State exitancestor=null;
            for(State s:exancestors){
                if(!(s instanceof ast.Statechart)&&s.getSuperstate()==lub)
                    exitancestor=s;
            }
            if(exitancestor==null) exitancestor=source;
            System.out.println("calling activeconfig : "+activeconfig.getCurrentStatesName());
            for(State s: activeconfig.getCurrentStates()){
                
                exitState(null, s, exitancestor);
            }
            activeconfig.clearActiveStates();
            
            //enter state
            State dest=t.getDestination();
            List<State> enancestors=dest.getAllSuperstates();
            State entryancestor=null;
            for(State s:enancestors){

                if(!(s instanceof ast.Statechart) && s.getSuperstate()==lub)
                    entryancestor=s;
            }
            if(entryancestor==null) entryancestor=dest;
            enterState(null, entryancestor, dest);
        }
        //return null;
    }
    public void enterDefaultState(CFA prev, State s){
        System.out.println("Enter default state : " +s.getFullName());
        
        CFA cfa=getCFAfromList(s.getFullName()+"_N", codenodelist);
        if(prev!=null)
        {
            cfa.addPrev(prev);
        }
        else{
            State sup=s.getSuperstate();
            if(sup!=null)
                cfa.addPrev(getCFAfromList(sup.getFullName()+"_N", execnodelist));
        }
        execnodelist.add(cfa);
        CFA scfa=getCFAfromList(s.getFullName()+"_N", execnodelist);
        
        if((s.states).size()>0 && s instanceof ast.Shell){
            
            for(State e:s.states){
                //System.out.println(e.getFullName());
                enterDefaultState(scfa,e);
            }
            
        }
        else if((s.states).size()>0){
            State e=(s.states).get(0);
            //System.out.println(e.getFullName());
            enterDefaultState(scfa,e);
        }
        else{
            //System.out.println("Add to configuration here");
            activeconfig.addState(s);
        }
    }
    public void enterState(CFA prev, State enstate, State destination){
        System.out.println("Entering state :"+enstate.getFullName());
        if(enstate.states.size()==0)
            activeconfig.addState(enstate);
        CFA cfa=getCFAfromList(enstate.getFullName()+"_N", codenodelist);
        if(prev!=null)
        {
            cfa.addPrev(prev);
        }
        else{
            State s=enstate.getSuperstate();
            if(s!=null)
                cfa.addPrev(getCFAfromList(s.getFullName()+"_N", codenodelist));
        }
        execnodelist.add(cfa);
        if(enstate==destination){            
            if(destination instanceof ast.Shell){
                for(State s:destination.states){
                    enterDefaultState(getCFAfromList(destination.getFullName()+"_N", codenodelist), s);
                }
            }
            else if(destination.getSuperstate() instanceof ast.Shell){
                State shellstate=destination.getSuperstate();
                for(State s:shellstate.states){
                    enterDefaultState(getCFAfromList(shellstate.getFullName()+"_N", codenodelist), s);
                }
            }
            else if((destination.states).size()>0){
                enterDefaultState(getCFAfromList(destination.getFullName()+"_N", codenodelist), (destination.states).get(0));
            }
        }
        else{
            if(enstate instanceof ast.Shell){
                for(State s:enstate.states){
                    enterDefaultState(getCFAfromList(enstate.getFullName()+"_N", codenodelist), s);
                }
            }
            else{
            State newEnterState=null;
            for(State s:enstate.states){
                //System.out.println("substate of entrystate :"+s.name);
                List<State> superstates=destination.getAllSuperstates();
                if(superstates.contains(s)||s==destination){
                    newEnterState=s;
                    break;
                }
            }
            
                enterState(getCFAfromList(enstate.getFullName()+"_N", codenodelist),newEnterState, destination);
            }
        }
        

    }
    public void exitState(CFA prev, State exstate, State exitancestor){
        System.out.println("Exiting state :"+exstate.getFullName());
        CFA cfa=getCFAfromList(exstate.getFullName()+"_N", codenodelist);
        if(prev!=null){
            cfa.addPrev(prev);
        }else{
            //prev will be added later
        }
        execnodelist.add(cfa);
        
        if(exstate!=exitancestor){
            exitState(cfa,exstate.getSuperstate(),exitancestor);

        }
    
    }
}