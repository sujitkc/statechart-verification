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

    public ArrayList<ArrayList<SETNode>> takeTransition(Transition t, SETNode leaf) // need to implement this
    {
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        
        leaves.add(leaf);

        // Compute the transition source and destination as per the StaBL semantics.
        State source = t.getSource();
        State destination = t.getDestination();

        // Exit state
        ArrayList<SETNode> leaves_ = new ArrayList<SETNode>();
        for(SETNode l : leaves){
            ArrayList<SETNode> d = exitState(source, l).get(0);
            ArrayList<SETNode> l_ = exitState(source, l).get(1);
            done.addAll(d);
            leaves_.addAll(l_);
        }
        leaves = leaves_;

        // Execute block
        ArrayList<SETNode> leaves_ = new ArrayList<SETNode>();
        for(SETNode l : leaves){
            ArrayList<SETNode> d = executeBlock(t.getAction()).get(0);
            ArrayList<SETNode> l_ = executeBlock(t.getAction()).get(1);
            done.addAll(d);
            leaves_.addAll(l_);
        }
        leaves = leaves_;

        // Enter state
        ArrayList<SETNode> leaves_ = new ArrayList<SETNode>();
        for(SETNode l : leaves){
            ArrayList<SETNode> d = enterState(destination, l).get(0);
            ArrayList<SETNode> l_ = exitState(destination, l).get(1);
            done.addAll(d);
            leaves_.addAll(l_);
        }
        leaves = leaves_;      

        // Return done and leaves
        result.add(done);
        result.add(leaves);
        return result;
    }

    /* Enter State */
    public ArrayList<ArrayList<SETNode>> enterState(State s, SETNode leaf)
    {
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();

        // New enterState node
        SETNode leaf = new StateEntryNode(s, leaf);

        // Get all the variables in the state s - this is done by getting the environment for the state and hence getting its declaration list
        Environment e = s.getEnvironment();
        DeclarationList dl = e.getDeclarations();

        // Set new variable nodes for all the variables in the declaration list
        for(Declaration d : dl){
            String s = dl.getScope().name;
            
            /* SETBBNode is not clear. Idk what to do */

            if(s.equals("local")){
                leaf = new VariableNode();
            }
        }



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


    public boolean satisfiable(Expression e) // need to implement this
    {
        return true;
    }

    /*
    // Functions need implementation
    public static SymbolicExecutionResult executeStatement();
    */

}
