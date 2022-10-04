package constabl.consimulator;
import java.util.*;
public class EventQueue{

   public static ArrayList<String> eventQueue=new ArrayList<String>();
   private final String tinit="tinit";
   
    public EventQueue(){
        
         this.eventQueue.add(tinit);
         this.eventQueue.add("e1");
         this.eventQueue.add("e2");
        
    }
}