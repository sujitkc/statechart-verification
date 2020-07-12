package simulator;
import java.util.Scanner;
import ast.*;

public class UserEventInSteps implements SimulationMode
{
    public Statechart statechart;

    public UserEventInSteps(Statechart s)
    {
        this.statechart = s;
    }
    public void simulate(ExecutionState eState)
    {
        try
        {
            System.out.println("\n\n\n Beginning Simulation... \n\n\n");                                     
            System.out.println("Initial Statechart Map: " + eState.generate_summary());   // prints the initial state
            State curr = Simulator.get_atomic_state((State)statechart, 1);                // gets to the atomic-state (i.e., this is like the start state for the statechart)
            Scanner input = new Scanner(System.in); 
            int counter = 0;
            //Main-loop
            while(true)
            {
              String event = eState.getEvent();
              input.nextLine();
              if(event.equals("null"))
              {
                  System.out.println("Event Queue is Empty! Do you want to add an event? (y/n)");
                  String response = input.nextLine();
                  if(response.equals("y"))
                  {
                      System.out.print("Enter the number of events to add: ");
                      int n = input.nextInt();
                      input.nextLine();
                      for(int i = 0; i < n; i++)
                      {
                          System.out.print("Enter event " + (i+1) + ": ");
                          eState.addEvent(input.nextLine());
                      }
                      event = eState.getEvent();
                  }
                  else
                  {
                      System.out.println("Proceeding simulation with null event!");
                  }      
              }
              System.out.println("+--------------------------------------------------+"); 
              System.out.println("After " + (counter+1) + " transition/transitions :-");
              System.out.println("From State: " + curr.getFullName());
              System.out.println("Current Event: " + event);
              Transition t = Simulator.get_valid_transition(curr, curr, 0, null, event, this.statechart);
              System.out.println("Performing transition: " + t.name);
              Simulator.performTransition(t, curr, this.statechart);
              System.out.println("Final State Map: " + eState.generate_summary());
              curr = t.getDestination();
              System.out.println("Reached State: " + curr.getFullName());
              System.out.println("+--------------------------------------------------+");
              curr = Simulator.get_atomic_state(curr, 0);                                 // gets to the state where from where the execution begins
              counter ++;
            }
        }
        catch(Exception e)
        {
            System.out.println("Simulation Failed!\n");
        }
    }
}