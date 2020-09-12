package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;

import java.util.ArrayList;
import java.util.List;

public class SymbolicExecutionEngine{
    
    private Statechart statechart;
    
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
           // ArrayList<Transition> out_ts = new ArrayList<Transition>(conf.get(conf.size() - 1).transitions);
			  
			int level=0;
			ArrayList<Transition> all_ts=new ArrayList<Transition>();
			ArrayList<Transition> out_ts=new ArrayList<Transition>(); 
			while((level+1)<conf.size()){
				//State curr_state=conf.get(level+1);
				State par_state=conf.get(level);
				//System.out.println("current state: "+curr_state.name);
				//System.out.println("parent state: "+par_state.name);

				all_ts= new ArrayList<Transition>(par_state.transitions);
			for(Transition t:all_ts){
					if(conf.contains(t.getSource())){
						System.out.println("Outgoing transition found : "+t.name);
						out_ts.add(t);
						}
				}
			level++;
			}
			
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
		System.out.println("inside take transition : " + t.name);
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        
        leaves.add(leaf);

        // Compute the transition source and destination as per the StaBL semantics.
        State source = t.getSource();
        State destination = t.getDestination();
        ArrayList<SETNode> leaves_ = new ArrayList<SETNode>();

        // Execute block
        leaves_.clear();
        for(SETNode l : leaves){
            ArrayList<ArrayList<SETNode>> r = new ArrayList<ArrayList<SETNode>>();
            ArrayList<SETNode> l__ = new ArrayList<SETNode>();
            l__.add(l);
			System.out.println("Calling execute block of transition");
            /*
			// Results in stack overflow error
			r = executeBlock(t.action, l__);
            ArrayList<SETNode> d = r.get(0);
            ArrayList<SETNode> l_ = r.get(1);
            done.addAll(d);
            leaves_.addAll(l_);*/
        }
        leaves = leaves_;


        // Exit state
        leaves_.clear();
		for(SETNode l : leaves){
            ArrayList<SETNode> d = exitState(source, l).get(0);
            ArrayList<SETNode> l_ = exitState(source, l).get(1);
            done.addAll(d);
            leaves_.addAll(l_);
        }
        leaves = leaves_;


        // Enter state
        leaves_.clear();
        for(SETNode l : leaves){
            ArrayList<SETNode> d = enterState(destination, l).get(0);
            ArrayList<SETNode> l_ = enterState(destination, l).get(1);
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
    public ArrayList<ArrayList<SETNode>> enterState(State st, SETNode l)
    {
		System.out.println("Entering state : "+st.getFullName());
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();

        // New enterState node
        SETNode leaf = new StateEntryNode(st, l);

        // Get all the variables in the state s - this is done by getting the environment for the state and hence getting its declaration list
        Environment e = st.getEnvironment();
        DeclarationList dl = e.getDeclarations();

        // Set new variable nodes for all the variables in the declaration list
        for(Declaration d : dl){
            String s = d.getScope().name;
            
            /* SETBBNode is not clear. Idk what to do */

            if(s.equals("local")){
				System.out.println("local variable found: "+s);
                //leaf = new InstructionNode(leaf, d); 
                // Type ast needs to have an initial value 
            }
            else if(s.equals("static")){
				System.out.println("static variable found: "+s);
                // First entry to the state
                //leaf = new InstructionNode(leaf, d);

                // Need to address 'not the first entry to the state'
            }
        }
		
		System.out.println("Entry statements: "+st.entry);
		try{
		if(st.entry instanceof StatementList)
        {
          List<Statement> st_list = ((StatementList)st.entry).getStatements();
          for(Statement stmt : st_list)
            executeStatement(stmt,"",leaves);
        }
        else 
        {
          try 
          {
            executeStatement(st.entry,"",leaves); 
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
		}catch(Exception ex){
			System.out.println("Exception occured : "+ex);
		}
        result.add(done);
        result.add(leaves);
        return result;
    }

    public ArrayList<ArrayList<SETNode>> exitState(State s, SETNode leaf) // need to implement this
    {
		System.out.println("Exit state : "+s.getFullName());
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        result.add(done);
        result.add(leaves);
        return result;
    }

    public ArrayList<ArrayList<SETNode>> executeStatement(Statement s, String f, ArrayList<SETNode> leaves)
    {
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<ArrayList<SETNode>> result_ = new ArrayList<ArrayList<SETNode>>();
        for(SETNode l : leaves)
        {
            
			if(f.equals("executeInstruction"))
            {
                result_ = executeInstruction(s, l);
            }
            else if(f.equals("executeIf"))
            {
                result_ = executeIf(s, l);
            }
            else if(f.equals("executeBlock"))
            {
                result_ = executeBlock(s, leaves);
            }
            result.get(0).addAll(result_.get(0));
            result.get(1).addAll(result_.get(1));
        }
        return result;
    }

    public ArrayList<ArrayList<SETNode>> executeBlock(Statement a, ArrayList<SETNode> l)
    {
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        leaves.addAll(l);
        if(a instanceof InstructionStatement)
        {
            result = executeStatement(a, "executeInstruction", leaves);
        }
        else if(a instanceof IfStatement)
        {
            result = executeStatement(a, "executeIf", leaves);
        }
        else if(a instanceof StatementList)
        {
            result = executeStatement(a, "executeBlock", leaves);
        }
        done.addAll(result.get(0));
        leaves = result.get(1);
        result.clear();
        result.add(0, done);
        result.add(1, leaves);
        return result;
    }

    public ArrayList<ArrayList<SETNode>> executeIf(Statement ifStatement, SETNode leaf)
    {
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        // need to implement this
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        result.add(done);
        result.add(leaves);
        return result;
    }

    public ArrayList<ArrayList<SETNode>> executeInstruction(Statement i, SETNode leaf)
    {
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        // need to implement this
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        result.add(done);
        result.add(leaves);
        return result;
    }


    public boolean satisfiable(Expression e) // need to implement this
    {
		
        return true;
    }


}
