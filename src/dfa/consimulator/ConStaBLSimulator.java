package consimulator;
import ast.*;
import java.util.*;

public class ConStaBLSimulator{
     private Statechart statechart = null;
     private Configuration activeConfiguration;
     private Configuration sourceConfiguration, destConfiguration;
     private Queue<String> eventQueue = new LinkedList<String>();

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
                takeTransition(e);
            }
            
        
        }
        public List<State> getDefaultAtomicSubState(ArrayList<State> substate){
            ArrayList<State> returnsubstates=new ArrayList<State>();
            int i=0, count=0;
            
            for(i=0;i<substate.size();i++){
            //    System.out.println("size :"+(substate.get(i)).states.size());
    
                if((substate.get(i)).states.size()!=0)
                    returnsubstates.add(i,(substate.get(i)).states.get(0));
                else
                    returnsubstates.add(i,substate.get(i));
            }
            //System.out.println("returnsubstates :"+returnsubstates);
            
            if(returnsubstates.equals(substate))
                return returnsubstates;
            else
                return getDefaultAtomicSubState(returnsubstates);

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