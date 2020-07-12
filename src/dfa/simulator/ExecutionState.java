package simulator;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

import ast.*;

public class ExecutionState
{
    private Map<Declaration, Expression> valueEnvironment         = new HashMap<Declaration, Expression>();
    private List<State> configuration                             = new ArrayList<State>();
    private Queue<String> eventQueue                              = new LinkedList<String>();
    private Map<State, State> stateHistory                        = new HashMap<State, State>();
    private Map<State, Map<Declaration, Expression>> valueHistory = new HashMap<State, Map<Declaration, Expression>>();

    public ExecutionState(Statechart st)
    {
        populate_state(st);

        if(!st.states.isEmpty())
        {
          for(State s : st.states)
          {
            populate_state(s);
          }
        }

        for(String e : st.events)
        {
            this.eventQueue.add(e);
        }
    }

    public void addEvent(String e)
    {
        this.eventQueue.add(e);
    }

    public String getEvent() throws NoSuchElementException
    {
        try
        {
            return this.eventQueue.remove();
        }
        catch (NoSuchElementException e)
        {
            return ("null");
        }
    }

    public void emptyEventQueue()
    {
        for(String s : this.eventQueue)
        {
            this.eventQueue.remove();
        }
    }

    public void setValue(Declaration d, Expression e)
    {
        this.valueEnvironment.put(d, e);
    }

    public Expression getValue(Declaration d)
    {
        return EvaluateExpression.evaluate(this.valueEnvironment.get(d));
    }

    public void setHistoryState(State s, State c)
    {
        this.stateHistory.put(s, c);
    }

    public State getHistoryState(State s)
    {
        if(this.stateHistory.get(s) != null)
            return this.stateHistory.get(s);
        else
            return s.states.get(0);
    }   

    public Boolean presentInConfiguration(State s)
    {
        for(State st : this.configuration)
        {
            if(st.equals(s))
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Declaration> getInputVariables(State s)
    {
        ArrayList<Declaration> output = new ArrayList<Declaration>();
        for(Declaration d : s.declarations)
        {
            if(d.input)
                output.add(d);
        }
        return output;
    }

    // everytime a state is entered,
    // the configuration list adds that state to the far end,
    // the local variables are initialized to null in the valueEnvironment,
    // the static and parameter variables' values are copied from valueHistory to the valueEnvironment
    public void enterState(State s)
    {
        this.configuration.add(s);
        for(Declaration d : s.declarations)
        {
            //if(d.isInput()){
            //    System.out.println("Enter the value: " + d.getFullVName());
            //    Scanner sc = new Scanner(System.in);
            //    if(d.getType().toString().equals("Variable type: basic type 'int'")){
            //        int temp = sc.nextInt();
            //    }
            //    //if(d.getType().equals("int")){
            //    //    int temp = sc.nextLine();
            //    //}
            //}
            if(d.getScope().name.equals("local"))
            {
                this.valueEnvironment.put(d, null);
            }
            else if(d.getScope().name.equals("static"))
            {
                this.valueEnvironment.put(d, (this.valueHistory.get(s)).get(d));
            }
        }
    }

    // everytime a state is exised, 
    // the parent state's history-state is set,
    // the state is removed from configuration list,
    // all the local variables are cleared and all the static and parameter variables are stored in valueHistory
    public void exitState(State s)
    {
        this.setHistoryState(s.getSuperstate(), s);
        this.configuration.remove(s);
        Map<Declaration, Expression> temp = new HashMap<Declaration, Expression>();
        for(Declaration d : s.declarations)
        {
            temp.put(d, this.valueEnvironment.get(d));
            this.valueHistory.put(s, temp);
            if(d.getScope().name.equals("local"))
            {
                valueEnvironment.remove(d);
            }
        }
    }


    private void populate_state(State s)
    {
        for(Declaration d : s.declarations)
        {
            if(!d.getScope().name.equals("local")) // maintaining the value history for static and parameter variables
            {
                this.valueEnvironment.put(d, null); // adding only those variables which do not have a local-scope

                Map<Declaration, Expression> temp = new HashMap<Declaration, Expression>();
                temp.put(d, null);
                this.valueHistory.put(s, temp);
            }
        }
    }

    // need to have an individual toString() function for all the environment variables
    public String generate_summary()
    {
        String s = "";
        s += "Value Environment: \n";
        for(Declaration d : this.valueEnvironment.keySet())
        {
            s += d.getFullVName() + " : " + this.valueEnvironment.get(d) + "\n";
        }
        s += "Configuration: ";
        for(State st : this.configuration)
        {
            s += st.name + " ";
        }
        s += "\n";
        s += "State History: ";
        for(State st : this.stateHistory.keySet())
        {
            s += "\n" + st.name + " : " + this.stateHistory.get(st).name;
        }
        // not adding the value history for now as it will get way too cluttered
        return s; 
    }
    
}
