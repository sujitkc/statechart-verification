package constabl.consimulator;
import java.util.*;
public class EventQueue{

   public static ArrayList<String> eventQueue=new ArrayList<String>();
   public static final String tinit="tinit";
   
    public EventQueue(){
        
         eventQueue.add(tinit);
         eventQueue.add("e1");
         eventQueue.add("e2");
        
    }
}