package frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.util.Scanner;

import ast.*;
//

public class FrontEnd {

  private final Parser parser;

  // necessary for simulator
  private Statechart statechart = null;

  public static Map<Declaration, Expression> map        = new HashMap<Declaration, Expression>(); //Creating a HashMap for the variable bindings, every variable is identified with its fullVName which is unique to it
  public static List<Transition>      transitions       = new ArrayList<Transition>();
  public static Map<State, Integer>   history_map       = new HashMap<State, Integer>();

  // constructors
  public FrontEnd(String input) throws FileNotFoundException {
    this.parser = new Parser(new Lexer(new FileReader(input)));
  }

  public FrontEnd(Statechart statechart) throws Exception
  {
    this.parser = null; 
    try 
    {
      this.statechart = statechart;
      simulation();
    }
    catch (Exception e)
    {
      System.out.println("Simulation Failed! Returning from FrontEnd\n");
    }

  }
  // pasrser
  public Parser getParser() 
  {
    return this.parser;
  }
  
  // populate map, all variables are by default bound to null
  private void populate(State s)
  {
    populate_state(s);      // populating the map - for all the declaration,value mapping
    populate_transition(s); // populating the list - to contain all the transitions
    
    if(!s.states.isEmpty())
    {
      for(State st : s.states)
        populate(st);
    }
  }
  
  // takes a state and populates the <declaration, value> map with all the declarations made in that state
  private void populate_state(State s) 
  {
    for(Declaration d : s.declarations)
    {
      map.put(d, null);
    }
  }

  // takes a state and populates the transition list with all the transitions declared in that state
  private void populate_transition(State s)
  {
    for(Transition t : s.transitions)
    {
      transitions.add(t);
    }
  }

  // takes a state and returns the atomic state for it (in the process, it also executes all the entry statements for the states lying in the path)
  // also takes care of whether the state has a history tag or not
  private State getAtomicState(State state) throws Exception
  {
    State init = state;
    try 
    {
      ExecuteStatement.executeStatement(init.entry);

      if(init.states.isEmpty()) // if the state is atomic, return
        return init;

      int index = 0; // if the state is not atomic
      if(history_map.containsKey(init))
        index = history_map.get(init);
      else
        index = 0;
      return getAtomicState(state.states.get(index));
    }
    catch (Exception e)
    {
      System.out.println("getAtomicState() Failed for: " + state.getFullName());
    }

    return init;
  }
  
  // displays all the variables and their bindings for a current time-stamp
  private void displayMap()
  {
    for(Map.Entry<Declaration, Expression> m : map.entrySet()){    
      if(m.getKey().getFullVName().equals(m.getKey().getState().getFullName() + ".VARIABLE"))
        continue;
      else if(m.getKey().getFullVName().equals(m.getKey().getState().getFullName() + ".HISTORY"))
        continue;
      System.out.println(m.getKey().getFullVName()+": "+m.getValue());    
     }  
  }

  // checking whether a state has a history tag or not (at the moment, this has been implemented in terms of a variable )
  private boolean hasHistory(State s)
  {
    for(Declaration d : s.declarations)
    {
      if(d.getFullVName().equals(s.getFullName() + ".HISTORY")) // if the HISTORY variable exists
      {
        if(map.get(d) instanceof BooleanConstant) // if the history variable is a boolean constant
        {
          if(((BooleanConstant)map.get(d)).value) // if the history variable is true
            return true;
        }
      }
    }
    return false;
  }

  // checking whether a state has VAR (very rudimentary methodology for an interactive experience)
  private boolean hasVAR(State s)
  {
    for(Declaration d : s.declarations)
    {
      if(d.getFullVName().equals(s.getFullName() + ".VARIABLE")) // if the HISTORY variable exists
      {
        return true;
      }
    }
    return false;
  }

  // sets the initial state for a parent state which maintains history
  private void setCurrent(State parent, State current)
  {
    int i = 0;
    for(State s : parent.states) // getting the index of the current state
    {
      if(s.equals(current))
        break;
      else
        i ++;
    }
    history_map.put(parent, i);
  }


  // performing a transition, does the following :
  // a) gets the lower upper bound
  // b) bubbles up to the lower bound while executing all the exist statements
  // c) when it gets to the lowest upper bound, it executes the action associated with the transition
  // d) bubbles down to the destination state from the lowest upper bound
  // e) while bubbling down, it executes all the entry statements for the states in the path
  private void performTransition(Transition t) throws Exception
  {
    try
    {
      State source = t.getSource();
      State destination = t.getDestination();
      State lub = statechart.lub(source, destination);
      State current = source;

      while(!current.equals(lub)) // bubbles up to lowest upper bound
      {
        ExecuteStatement.executeStatement(current.exit);
        State currParent = current.getSuperstate();
        if(hasHistory(currParent))
        {
          setCurrent(currParent, current);
        }
        current = currParent;
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
      while(i > 1)
      {
        ExecuteStatement.executeStatement(path.get(i).entry);
        i ++;
      }
    }
    catch (Exception e)
    {
      System.out.println("performTransition() failed for transition: " + t);
    }
  }

  // this is the main function corresponding to the simulation
  public void simulation() throws Exception 
  {
    try 
    {
      System.out.println("\n\n\nSimulation...\n\n\n");

      // populating the map and transitions list to contain all the variables and transitions
      this.populate(statechart);

      System.out.println("Initial Statechart Map: ");
      displayMap(); // displays the initial map

      
      Scanner input = new Scanner(System.in);  // to check the sequential simulation

      int counter = 0; // to label the number of transitions performed

      State curr = (State)statechart; // curr represents the current state

      //Main-loop
      while(true)
      {
        curr = getAtomicState(curr); // gets to the state where from where the execution begins
        
        /* Very Basic as of now (For an Interactive System)*/
        if(hasVAR(curr)) // state takes an input - as of now, only integer
        {
          System.out.println("\n ENTER YOUR INPUT: ");
          int user_input = input.nextInt();
          for(Declaration d : curr.declarations)
          {
            if(d.getFullVName().equals(curr.getFullName() + ".VARIABLE"))
            {
              map.put(d, new IntegerConstant(user_input));
            }
          }
        }

        System.out.println("+--------------------------------------------------+");
        System.out.println("Initial State Map: ");
        displayMap();
        System.out.println();

        // System.out.println("Static Analysis: ");
        // new ZonotopeAbstractDomain(curr, map); // - uncomment this to run the numerical static analyzer

        input.nextLine(); // to break the flow into key-strokes

        System.out.println("After " + counter + " transition/transitions :-");
        System.out.println("State: " + curr.getFullName());

        for(Transition t : transitions)
        {
          if(t.getSource().equals(curr))
          {
            if(((BooleanConstant)EvaluateExpression.evaluate(t.guard)).value)
            {
              performTransition(t);
              System.out.println("Final State Map: ");
              displayMap();
              curr = t.getDestination();
              System.out.println("+--------------------------------------------------+");
              break;
            }
          }
        }
        counter ++;
      }
    }
    catch (Exception e)
    {
      System.out.println("Simulation Failed!\n"); 
    }
  }
}
//https://github.com/AmoghJohri/StatechartSimulator.git

