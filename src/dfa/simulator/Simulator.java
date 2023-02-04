package simulator;

import java.util.ArrayList;

import ast.*;
//

public class Simulator {

  // necessary for simulator
  private Statechart statechart = null;
  public static ExecutionState eState;
  public SimulationMode simulationMode;

  private static ArrayList<Transition> validTransitions = new ArrayList<Transition>();

  public Simulator(Statechart statechart) throws Exception
  {
    try 
    {
      this.statechart = statechart;

      eState = new ExecutionState(statechart); 
      for (String e : this.statechart.events)
        eState.addEvent(e);
        
        // this.simulationMode = new PreEventInSteps(this.statechart);
        this.simulationMode = new UserEventInSteps(this.statechart);
  
        new SymbolicEngine(this.statechart);

        this.simulationMode.simulate(eState);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  

  // takes a state and returns the atomic state for it (in the process, it also executes all the entry statements for the states lying in the path)
  // also takes care of whether the state has a history tag or not
  public static State get_atomic_state(State state, int tag) throws Exception
  {
    State init = state;
    try 
    {
      if(tag == 1)
      {
        eState.enterState(init);                       // enters the state
        ExecuteStatement.executeStatement(init.entry); // execute the entry statement
      }
      else
      {
        tag = 1;
      }
      if(init.states.isEmpty())                      // if the state is atomic
      {
        return init;
      }
      else                                           // if the state is heirarchial
      {
        if((init.maintainsHisotry()).value)          // if the state maintains history
        {
          return get_atomic_state(eState.getHistoryState(init), tag);
        }
        else                                         // if the state does not maintain history
        {
          return get_atomic_state(init.states.get(0), tag);
        }
      }
    }
    catch (Exception e)                              // if the executeStatement failed
    {
      e.printStackTrace();
    }

    return init;                                    // execution never actually reaches here
  }

  // performing a transition, does the following :
  // a) gets the lower upper bound
  // b) bubbles up to the lower bound while executing all the exist statements
  // c) when it gets to the lowest upper bound, it executes the action associated with the transition
  // d) bubbles down to the destination state from the lowest upper bound
  // e) while bubbling down, it executes all the entry statements for the states in the path
  public static void performTransition(Transition t, State curr, Statechart statechart) throws Exception
  {
    try
    {
      State source = t.getSource();
      State destination = t.getDestination();
      State lub = statechart.lub(source, destination);
      State current = curr;
      while(!current.equals(lub)) // bubbles up to lowest upper bound
      {
        ExecuteStatement.executeStatement(current.exit);
        eState.exitState(current);
        current = current.getSuperstate();
      }
      
      ExecuteStatement.executeStatement(t.action); // executes the transition action
    
      ArrayList<State> path = new ArrayList<State>();
      current = destination;
      while(!current.equals(lub)) // bubbles down to the destnation state
      {
        path.add(current);
        current = current.getSuperstate();
      }
      int i = path.size() - 1;
      while(i != 0)
      {
        eState.enterState(path.get(i));
        ExecuteStatement.executeStatement(path.get(i).entry);
        i --;
      }
      if(i == 0)
      {
        eState.enterState(t.getDestination());
        ExecuteStatement.executeStatement(t.getDestination().entry);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  // takes a state as the input and returns a valid transition (if available) with the input state as the source state
  public static Transition get_valid_transition(State curr, State fix, int i, Transition output, String event, Statechart statechart) throws Exception 
  {
    try
    {
      for(Transition t : curr.transitions)
      {
        try
        {
            if(((BooleanConstant)EvaluateExpression.evaluate(t.guard)).value && eState.presentInConfiguration(t.getSource()) && t.trigger.equals(event))
            {
              output = t;
              i++;
              if(i == 1)
              {
                validTransitions = new ArrayList<Transition>();
              }
              validTransitions.add(t);
            }
        }
        catch (NullPointerException e) // if one of the above expressions evaluate to null, that transition is not considered viable.
        {
          continue;
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      if(!curr.equals(statechart))
      {
        return get_valid_transition(curr.getSuperstate(), fix, i, output, event, statechart);
      }
      else
      {
        if(i == 0)
        {
          if(event.equals("null")) // if no viable transition is found
          {
            System.out.println("No Viable Transition! Halting Simulation...");
            ExecuteStatement.executeStatement(new HaltStatement());
          }
          else                    // if no viable transition is found for the current event, we re-reun the entire search with null event
          {
            return get_valid_transition(fix, fix, i, output, "null", statechart);
          }
        }
        else if(i > 1)
        {
          System.out.println("Non Determinism Detected At State: " + curr.name);
          System.out.println("Halting Simulation...");
          System.out.println("List of valid transitions are: ");
          for(Transition t : validTransitions)
            System.out.println("->\t" + t.name); 
          ExecuteStatement.executeStatement(new HaltStatement());
        }
        else
        {
          return output;
        }
      }
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return output;
  }
}

//https://github.com/AmoghJohri/StatechartSimulator.git

