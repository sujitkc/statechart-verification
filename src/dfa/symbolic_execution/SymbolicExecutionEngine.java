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
            this.execute((State)statechart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    } 
	public ArrayList<State> getConfiguration(State curr){
		ArrayList<State> conf = new ArrayList<State>();
        
		conf.add(curr);
        while(!curr.states.isEmpty())
        {
            curr = curr.states.get(0);
            conf.add(curr);
        }
		return conf;
	}
    // TODO
    public ArrayList<SETNode> execute(State statechart)
    { 
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        leaves.add(new SETNode(null));
        ArrayList<State> conf = new ArrayList<State>();
        
		State curr = statechart;
        conf=getConfiguration(curr);
		
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
					int depth=0;

        while(!leaves.isEmpty())
        {
           // ArrayList<Transition> out_ts = new ArrayList<Transition>(conf.get(conf.size() - 1).transitions);
			/*if(depth<10){  
			System.out.println("==========In depth :"+depth+"==============");*/
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
					// else - add the leaf node to done
					
                }
                //leaves = leaves_; /* not to use this type of assignment*/
				//leaves.clear();
				leaves.addAll(leaves_);
				leaves.remove(leaf);
            }
			/*}
			else{break;}
			depth++;*/
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

        
		
        // Exit state
        leaves_.clear();
		for(SETNode l : leaves){
            ArrayList<SETNode> d = exitState(source, l).get(0);
            ArrayList<SETNode> l_ = exitState(source, l).get(1);
            done.addAll(d);
            leaves_.addAll(l_);
        }
        //leaves = leaves_;
		leaves.clear();
		leaves.addAll(leaves_);

		// Execute t.action block
        leaves_.clear();
        for(SETNode l : leaves){
            ArrayList<ArrayList<SETNode>> r = new ArrayList<ArrayList<SETNode>>();
            ArrayList<SETNode> l__ = new ArrayList<SETNode>();
            l__.add(l);
			System.out.println("Calling execute block of transition");
            
			r = executeBlock(t.action, l__);
			if(r.size()==2){
            ArrayList<SETNode> d = r.get(0);
            ArrayList<SETNode> l_ = r.get(1);
            done.addAll(d);
            leaves_.addAll(l_);
			}
        }
        //leaves = leaves_;  /*Not to use this assignmenet - This is creating a reference of leaves_ to leaves - so further when leaves_ is cleared, leaves also gets cleared */
		
		leaves.clear();
		leaves.addAll(leaves_);


        // Enter state
        /*leaves_.clear();
        for(SETNode l : leaves){
            ArrayList<SETNode> d = enterState(destination, l).get(0);
            ArrayList<SETNode> l_ = enterState(destination, l).get(1);
            done.addAll(d);
            leaves_.addAll(l_);
        }
        //leaves = leaves_;      
		leaves.clear();
		leaves.addAll(leaves_);
		*/
		// New Enter State
        for(SETNode l : leaves){
			execute(destination);
		}
		
        // Return done and leaves
        result.add(0,done);
        result.add(1,leaves);
        return result;
    }

    /* Enter State */
    public ArrayList<ArrayList<SETNode>> enterState(State st, SETNode l)
    {
		System.out.println("Entering state : "+st.getFullName());
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<ArrayList<SETNode>> result_ = new ArrayList<ArrayList<SETNode>>();
        
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
          /*List<Statement> st_list = ((StatementList)st.entry).getStatements();
          for(Statement stmt : st_list)
            executeStatement(stmt,"",leaves);*/
			result_=executeBlock(st.entry,leaves);
        }
        /*else 
        {
          try 
          {
            executeStatement(st.entry,"",leaves); 
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }*/
		}catch(Exception ex){
			System.out.println("Exception occured : "+ex);
		}
		if(result_.size()==2){
		done=result_.get(0);
		leaves=result_.get(1);
		}
		
        result.add(done);
        result.add(leaves);
        return result;
    }

    public ArrayList<ArrayList<SETNode>> exitState(State s, SETNode leaf) // need to implement this
    {
		System.out.println("Exit state : "+s.getFullName());
        ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        ArrayList<ArrayList<SETNode>> result_ = new ArrayList<ArrayList<SETNode>>();
        
		ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
		leaves.add(leaf);
        try{
		if(s.exit instanceof StatementList)
        {
          //List<Statement> st_list = ((StatementList)st.exit).getStatements();
          //for(Statement stmt : st_list)
            result_=executeBlock(s.exit,leaves);
        }
        /*else 
        {
          try 
          {
            executeStatement(st.entry,"",leaves); 
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }*/
		}catch(Exception ex){
			System.out.println("Exception occured : "+ex);
		}
		if(result_.size()==2){
		done=result_.get(0);
		leaves=result_.get(1);
		}
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
			if(result_.size()==2){
				/* Code by Advait/Amogh commented - have to recheck the code here is it result.get(0).addAll()? - as it results in IndexOutOfBounds*/
            //result.get(0).addAll(result_.get(0));
			//result.get(1).addAll(result_.get(1));
			result.add(result_.get(0));
			result.add(result_.get(1));
			
			}
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
			System.out.println("inside executeBlock-InstructionStatement");
            result = executeStatement(a, "executeInstruction", leaves);
        }
        else if(a instanceof IfStatement)
        {
			System.out.println("inside executeBlock-IfStatement");
            result = executeStatement(a, "executeIf", leaves);
        }
        else if(a instanceof StatementList)
        {
			System.out.println("inside executeBlock-StatementList :" +((StatementList)a).getStatements());
			for(Statement s:((StatementList)a).getStatements()){
					result = executeBlock(s, leaves);
					//result = executeStatement(s, "executeInstruction", leaves);
			}
        }
		if(result.size()==2){
        done.addAll(result.get(0));
        leaves = result.get(1);}
		//System.out.println("Leaves : "+ leaves);
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
