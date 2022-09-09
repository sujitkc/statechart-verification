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
            this.activeConfiguration=new Configuration();
            this.sourceConfiguration=new Configuration();
            this.destConfiguration=new Configuration();
            
            /* adding events into the queue */
            this.eventQueue.add("init");
            this.eventQueue.add("e1");
            this.eventQueue.add("e2");
            
            System.out.println("------ Simulating ConStaBL Simulator --------\n");
            //System.out.println("Augumenting statechart with dummy state and init event : ");
            //this.statechart=augumentStatechart();   
            // I will work on augumenting later...

            System.out.println(this.statechart);         
            System.out.println("Event Queue : "+this.eventQueue.toString());
            //String e;
            for(String e : eventQueue){
                takeTransition(e);
            }
            
        
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
                BooleanConstant   region=new BooleanConstant(false);
                BooleanConstant   shell=new BooleanConstant(false);
                BooleanConstant   fin=new BooleanConstant(false);
                State dummy=new State(
                    name,
                    declarations,
                    entry,
                    exit,
                    states,
                    transitions,
                    history,
                    region,
                    shell,
                    fin);
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