package consimulator;
import ast.*;
import java.util.*;

public class ConStaBLSimulator{
     private Statechart statechart = null;
     private Configuration activeConfiguration;
     private Configuration sourceConfiguration, destConfiguration;
     private Queue<String> eventQueue                              = new LinkedList<String>();

      public ConStaBLSimulator(Statechart statechart) throws Exception
        {
            
            this.statechart=statechart;
            
            /* adding events into the queue */
            this.eventQueue.add("init");
            this.eventQueue.add("e1");
            this.eventQueue.add("e2");
            

            System.out.println("------ Simulating ConStaBL Simulator --------\n");
            System.out.println("Event Queue : "+this.eventQueue.toString());
        }
        public void addEvent(String e)
        {
            this.eventQueue.add(e);
        }
}