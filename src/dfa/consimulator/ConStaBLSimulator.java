package consimulator;
import ast.*;
import java.util.*;

public class ConStaBLSimulator{
     private Statechart statechart = null;
     Configuration activeConfiguration;
     Configuration sourceConfiguration, destConfiguration;
     
      public ConStaBLSimulator(Statechart statechart) throws Exception
        {
            this.statechart=statechart;
            System.out.println("Simulating ConStaBL Simulator");
        }

}