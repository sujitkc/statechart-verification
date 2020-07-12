package simulator;

import java.util.ArrayList;

import ast.*;
//

public class Simulator {

  // necessary for simulator
  private Statechart statechart = null;
  public static ExecutionState eState;
  public SimulationMode simulationMode;

  public Simulator(Statechart statechart) throws Exception
  {
    try 
    {
      this.statechart = statechart;
      eState = new ExecutionState(statechart); 
      this.simulationMode = new UserEventInSteps(this.statechart);
      this.simulationMode.simulate(eState);
    }
    catch (Exception e)
    {
      System.out.println("Simulation Failed! Returning from Simulator\n");
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
      System.out.println("get_atomic_state() Failed for: " + state.getFullName() + " for the entry-statement: " + state.entry);
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
      System.out.println("performTransition() failed for transition: " + t);
    }
  }

  // takes a state as the input and returns a valid transition (if available) with the input state as the source state
  public static Transition get_valid_transition(State curr, State fix, int i, Transition output, String event, Statechart statechart) throws Exception 
  {
    try
    {
      curr = curr.getSuperstate();
      for(Transition t : curr.transitions)
      {
        if(((BooleanConstant)EvaluateExpression.evaluate(t.guard)).value && eState.presentInConfiguration(t.getSource()) && t.trigger.equals(event))
        {
          output = t;
          i++;
        }
      }
      if(!curr.equals(statechart))
      {
        return get_valid_transition(curr, fix, i, output, event, statechart);
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
      System.out.println("this.get_valid_transition failed to execute halt statement!\n");
    }
    return output;
  }
}

//https://github.com/AmoghJohri/StatechartSimulator.git

