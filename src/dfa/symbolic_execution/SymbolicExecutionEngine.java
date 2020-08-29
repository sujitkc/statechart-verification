package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;

import java.util.ArrayList;

public class SymbolicExecutionEngine{
    
    private Statechart statechart;
    public static ExecutionState eState;

    public SymbolicExecutionEngine(Statechart statechart) throws Exception{
        try{
            this.statechart = statechart;
            this.execute(statechart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    } 

    // TODO
    public ArrayList<SETNode> execute(Statechart statechart)
    { 
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        leaves.add(new SETNode(null));
        ArrayList<State> conf = new ArrayList<State>();
        State curr = (State)statechart;
        conf.add(curr);
        while(!curr.states.isEmpty())
        {
            curr = curr.states.get(0);
            conf.add(curr);
        }
        // here, conf gives us the initial configuration
        for(State s : conf)
        {
            for(SETNode leaf : leaves)
            {
                ArrayList<ArrayList<SETNode>> result = this.enterState(s, leaf);
                ArrayList<SETNode> d = result.get(0);
                ArrayList<SETNode> l = result.get(1);
                done.addAll(d);
                if(l.isEmpty())
                {
                    break;
                }
                else
                {
                    leaves = l;
                }
            }
        }
        while(!leaves.isEmpty())
        {
            ArrayList<Transition> out_ts = new ArrayList<Transition>(conf.get(conf.size() - 1).transitions);
            for(SETNode leaf : leaves)
            {
                ArrayList<SETNode> leaves_ = new ArrayList<SETNode>();
                for(Transition t : out_ts)
                {
                    if(satisfiable(t.guard))
                    {
                        ArrayList<ArrayList<SETNode>> result = this.takeTransition(t, leaf);
                        ArrayList<SETNode> d = result.get(0);
                        ArrayList<SETNode> l = result.get(1);
                        done.addAll(d);
                        leaves_.addAll(l);
                    }
                }
                leaves = leaves_;
            }
        }
        return done;
    }

    public ArrayList<ArrayList<SETNode>> enterState(State s, SETNode leaf) // need to implement this
    {
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        result.add(done);
        result.add(leaves);
        return result;
    }

    public ArrayList<ArrayList<SETNode>> exitState(State s, SETNode leaf) // need to implement this
    {
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        result.add(done);
        result.add(leaves);
        return result;
    }

    public ArrayList<ArrayList<SETNode>> takeTransition(Transition t, SETNode leaf) // need to implement this
    {
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        result.add(done);
        result.add(leaves);
        return result;
    }

    public boolean satisfiable(Expression e) // need to implement this
    {
        return true;
    }

    /*
    // Functions need implementa    tion
    public static SymbolicExecutionResult executeStatement();
    */

}
