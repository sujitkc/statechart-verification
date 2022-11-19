package constablsim;
import ast.*;
import java.util.*;
import constablsim.ast.*;
public class StatechartSimulator extends Simulator {
    private Statechart statechart = null;
    private List<String> eventQueue=null;
    private Configuration activeconfig=null;
    EventQueue eq=new EventQueue();
    //private final String tinit=EventQueue.tinit;
    public void simulate(){
        for(String ev : eq.eventQueue){
            consumeEvent(ev);   
        }
    }
    public void consumeEvent(String event){
        //detectNonDeterminism
        //computeCode
        
    }
}
