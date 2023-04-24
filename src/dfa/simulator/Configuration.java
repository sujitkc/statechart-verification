package simulator;

import java.util.*;

import ast.*;

public class Configuration{
    private ArrayList<State> config = new ArrayList<State>();
    private ArrayList<ArrayList<State>> concurrentConfig = new ArrayList<ArrayList<State>>();
    private boolean concurrentExecution = false;

    public ArrayList<State> getConfig()
    {
        return this.config;
    }

    public ArrayList<ArrayList<State>> getConcurrentConfig()
    {
        return this.concurrentConfig;
    }

    public void addToConfiguration(State state)
    {
        if(!this.concurrentExecution)
        {
            this.config.add(state);
        }
        else
        {
            State parent = state.getSuperstate(); 
            for(ArrayList<State> array : this.concurrentConfig)
            {
                if(array.get(array.size() - 1).getFullName().equals(parent.getFullName()))
                {
                    array.add(state);
                    break;
                }
            }
        }
    }

    public void addToConfiguration(ArrayList<State> states)
    {
        if(!this.concurrentExecution)
        {
            this.concurrentExecution = true;
            ArrayList<State> allSuperStates = new ArrayList<State>();
            allSuperStates.addAll(states.get(0).getAllSuperstates());
            for(State s : states)
            {
                this.concurrentConfig.add(allSuperStates);
                this.concurrentConfig.get(this.concurrentConfig.size() - 1).add(s);
            } 
        }
        else
        {
            List<State> allSuperStates = states.get(0).getAllSuperstates();
            
        }

    }
}