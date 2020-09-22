package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;

import java.util.ArrayList;
import java.util.List;

public class SymbolicExecutionEngine{
    
    private Statechart statechart;
    private int depth=0;
	private int maxdepth=2;
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
        conf.addAll(curr.getAllSuperstates());
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
		

		for(State s : conf)
			{
				for(SETNode leaf : leaves)
				//while(!leaves.isEmpty())
				{
					//ArrayList<ArrayList<SETNode>> result = this.enterState(s, leaf);
					//SETNode leaf=leaves.get(0);
					if(leaf.depth<maxdepth){
					System.out.println("Calling enterState : "+s.getFullName());
					SymbolicExecutionResult result = this.enterState(s, leaf);
					
					//ArrayList<SETNode> d = result.get(0);
					//ArrayList<SETNode> l = result.get(1);
					ArrayList<SETNode> d = result.getDoneNodes();
					ArrayList<SETNode> l = result.getLiveNodes();
					System.out.println("new set of leaves created : at depth : "+l);
					if(d!=null) done.addAll(d);
					if(l.isEmpty())
					{
						break;
					}
					else
					{
						leaves = l;
						//for(SETNode lf:leaves){
						//	System.out.println("new set of leaves created : at depth : "+lf.depth);
							
						//}
						//leaves.addAll(l);
					}
					}
				}
			}
			
        while(!leaves.isEmpty())
        {
			
			// here, conf gives us the initial configuration
			
			
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
	ArrayList<SETNode> leaves_ = new ArrayList<SETNode>();
          
	//for(SETNode leaf : leaves)
      for(int k=0;k<leaves.size();k++)      {
                SETNode leaf=leaves.get(k);
				for(Transition t : out_ts)
                {
                    if(satisfiable(t.guard))
                    {
                        /*ArrayList<ArrayList<SETNode>> result = this.takeTransition(t, leaf);
                        ArrayList<SETNode> d = result.get(0);
                        ArrayList<SETNode> l = result.get(1);*/
						SymbolicExecutionResult result = this.takeTransition(t, leaf);
						ArrayList<SETNode> d = result.getDoneNodes();
                        ArrayList<SETNode> l = result.getLiveNodes();
						
                        done.addAll(d);
                        leaves_.addAll(l);
                    }
					// else - add the leaf node to done
					else{
						done.add(leaf);
					}
                }
                //leaves = leaves_; /* not to use this type of assignment*/
				//leaves.clear();
				//leaves.addAll(leaves_);
				leaves.remove(leaf);
            }
			/*}
			else{break;}
			depth++;*/
			leaves.addAll(leaves_);
        }
        return done;
    }

    public SymbolicExecutionResult takeTransition(Transition t, SETNode leaf) // need to implement this
    {
		System.out.println("inside take transition : " + t.name);
        SymbolicExecutionResult result = new SymbolicExecutionResult();
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
            //ArrayList<SETNode> d = exitState(source, l).get(0);
            
			//ArrayList<SETNode> l_ = exitState(source, l).get(1);
			SymbolicExecutionResult res=exitState(source, destination, l);
            ArrayList<SETNode> d = res.getDoneNodes();
            ArrayList<SETNode> l_ = res.getLiveNodes();

			
			done.addAll(d);
            leaves_.addAll(l_);
        }
        //leaves = leaves_;
		leaves.clear();
		leaves.addAll(leaves_);

		// Execute t.action block
        leaves_.clear();
        for(SETNode l : leaves){
            //ArrayList<ArrayList<SETNode>> r = new ArrayList<ArrayList<SETNode>>();
            SymbolicExecutionResult r=new SymbolicExecutionResult();
			ArrayList<SETNode> l__ = new ArrayList<SETNode>();
            l__.add(l);
			System.out.println("Calling execute block of transition");
            
			r = executeBlock(t.action, l__);
			
            //ArrayList<SETNode> d = r.get(0);
            //ArrayList<SETNode> l_ = r.get(1);
			ArrayList<SETNode> d = r.getDoneNodes();
            ArrayList<SETNode> l_ = r.getLiveNodes();
			
            done.addAll(d);
            leaves_.addAll(l_);
			
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
			System.out.println("Inside new enter state : "+destination);
			execute(destination);
		}
		
        // Return done and leaves
        result.setDoneNodes(done);
        result.setLiveNodes(leaves);
        return result;
    }


	/*Enter State - Symbolic Execution Result*/
	
	  public SymbolicExecutionResult enterState(State st, SETNode l)
    {
		System.out.println("Entering state : "+st.getFullName());
        //ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        //ArrayList<ArrayList<SETNode>> result_ = new ArrayList<ArrayList<SETNode>>();
        SymbolicExecutionResult result=new SymbolicExecutionResult();
		SymbolicExecutionResult result_=new SymbolicExecutionResult();
		
		ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();

        // New enterState node
        SETNode leaf = new StateEntryNode(st, l);
		done.add(leaf);
        // Get all the variables in the state s - this is done by getting the environment for the state and hence getting its declaration list
        Environment e = st.getEnvironment();
        DeclarationList dl = e.getDeclarations();

        // Set new variable nodes for all the variables in the declaration list
        for(Declaration d : dl){
            String s = d.getScope().name;
            
            /* SETBBNode is not clear. Idk what to do */

            if(s.equals("local")){
				System.out.println("local variable found: "+s);
				//3rd parameter to be worked out
                leaf = new VariableNode(leaf, d, null); 
                // Type ast needs to have an initial value 
            }
            else if(s.equals("static")){
				System.out.println("static variable found: "+s);
                // First entry to the state
                leaf = new VariableNode(leaf, d, null);
                // Need to address 'not the first entry to the state'
            }
			else if(s.equals("parameter")){
				System.out.println("Parameter variable found : "+s);
				leaf = new VariableNode(leaf, d, null);
			}
			done.add(leaf);
        }
		leaves.add(leaf);
		result_=executeBlock(st.entry,leaves);
		leaves.clear();
		result.setDoneNodes(done);
		result.setDoneNodes(result_.getDoneNodes());
		result.setLiveNodes(result_.getLiveNodes());
		//System.out.println("Set of done nodes at the end of entry block is :"+result.getDoneNodes());
		//System.out.println("Set of leaf nodes at the end of entry block is :"+result.getLiveNodes());
		
        return result;
    }




    public SymbolicExecutionResult exitState(State src, State dest, SETNode leaf) // need to implement this
    {
		System.out.println("Exit state called");
        //ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        //ArrayList<ArrayList<SETNode>> result_ = new ArrayList<ArrayList<SETNode>>();
        SymbolicExecutionResult result=new SymbolicExecutionResult();
		SymbolicExecutionResult result_=new SymbolicExecutionResult();
		
		ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
		leaves.add(leaf);
		ArrayList<State> conf=getConfiguration(src);
		State lub=statechart.lub(src,dest);
		
		for(int i=conf.size()-1;i>=0&&conf.get(i)!=lub;i--)
        {
			State s=conf.get(i);
		System.out.println("Exit state : "+s.getFullName());
            for(SETNode leaf1 : leaves)
            {
                //ArrayList<ArrayList<SETNode>> result = this.enterState(s, leaf);
         //       SymbolicExecutionResult result = this.exitState(s1, leaf);
				try{
		if(s.exit instanceof StatementList)
        {
          //List<Statement> st_list = ((StatementList)st.exit).getStatements();
          //for(Statement stmt : st_list)
            result_=executeBlock(s.exit,leaves);
        }
		}catch(Exception ex){
			System.out.println("Exception occured : "+ex);
		}
				//ArrayList<SETNode> d = result.get(0);
                //ArrayList<SETNode> l = result.get(1);
				ArrayList<SETNode> d = result.getDoneNodes();
				ArrayList<SETNode> l = result.getLiveNodes();
                if(d!=null) done.addAll(d);
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
        
		/*if(result_.size()==2){
		done=result_.get(0);
		leaves=result_.get(1);
		}
		result.add(done);
        result.add(leaves);*/
        return result_;
    }



    public SymbolicExecutionResult executeStatement(Statement s, String f, ArrayList<SETNode> leaves)
    {
        //ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
        //ArrayList<ArrayList<SETNode>> result_ = new ArrayList<ArrayList<SETNode>>();
        SymbolicExecutionResult result = new SymbolicExecutionResult();
        SymbolicExecutionResult result_ = new SymbolicExecutionResult();
       
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
				/* Code by Advait/Amogh commented - have to recheck the code here is it result.get(0).addAll()? - as it results in IndexOutOfBounds*/
            //result.get(0).addAll(result_.get(0));
			//result.get(1).addAll(result_.get(1));
			//result.add(result_.get(0));
			//result.add(result_.get(1));
			
			
        }
        return result_;
    }


   public SymbolicExecutionResult executeBlock(Statement a, ArrayList<SETNode> l)
    {
        //ArrayList<ArrayList<SETNode>> result = new ArrayList<ArrayList<SETNode>>();
		
		SymbolicExecutionResult result=new SymbolicExecutionResult();
        
		//SymbolicExecutionResult result_to_return=new SymbolicExecutionResult();
        ArrayList<SETNode> done = new ArrayList<SETNode>();
        ArrayList<SETNode> leaves = new ArrayList<SETNode>();
        leaves.addAll(l);
        if(a instanceof InstructionStatement)
        {
			//System.out.println("inside executeBlock-InstructionStatement");
            result = executeStatement(a, "executeInstruction", leaves);
        }
        else if(a instanceof IfStatement)
        {
			//System.out.println("inside executeBlock-IfStatement");
            result = executeStatement(a, "executeIf", leaves);
        }
        else if(a instanceof StatementList)
        {
			System.out.println("inside executeBlock-StatementList :" +((StatementList)a).getStatements());
			SymbolicExecutionResult result_=new SymbolicExecutionResult();
			for(Statement s:((StatementList)a).getStatements()){
					//result=executeBlock(s, leaves);
					result_=executeBlock(s,leaves);
					
					result.setDoneNodes(result_.getDoneNodes());
					leaves.clear();
					leaves.addAll(result_.getLiveNodes());
					//result = executeStatement(s, "executeInstruction", leaves);
			}
			result.setLiveNodes(result_.getLiveNodes());
        }
       return result;
    }



 	public SymbolicExecutionResult executeIf(Statement ifStatement, SETNode leaf)
    {
		SymbolicExecutionResult result=new SymbolicExecutionResult();
		Expression c=sym_eval(((IfStatement)ifStatement).condition,leaf);
		ArrayList<SETNode> leaves=new ArrayList<SETNode>();
		if(satisfiable(c)){
			// need to work on 3rd parameter of decision node
			DecisionNode decisionNode=new DecisionNode(((IfStatement)ifStatement).condition, leaf, null);
			leaves.add(decisionNode);
			SymbolicExecutionResult result_=executeBlock(((IfStatement)ifStatement).then_body, leaves);
			result.setDoneNodes(result_.getDoneNodes());
			result.setLiveNodes(result_.getLiveNodes());
		}
		else{
			leaves.add(leaf);
			result.setDoneNodes(leaves);
			}
		leaves.clear();
		if(satisfiable(negation(c)))
		{
			//need to work on 3rd parameter of decision node
			DecisionNode decisionNode=new DecisionNode(((IfStatement)ifStatement).condition, leaf, null);
			leaves.add(decisionNode);
			SymbolicExecutionResult result_=executeBlock(((IfStatement)ifStatement).else_body, leaves);
			result.setDoneNodes(result_.getDoneNodes());
			result.setLiveNodes(result_.getLiveNodes());
		}
		else{
			leaves.add(leaf);
			result.setDoneNodes(leaves);
		}
		return result;
	}
public SymbolicExecutionResult executeInstruction(Statement instructionStatement, SETNode leaf)
    {
		SymbolicExecutionResult result=new SymbolicExecutionResult();
		//Single step
		InstructionNode in=new InstructionNode(instructionStatement, leaf, null);
		ArrayList<SETNode> done=new ArrayList<SETNode>();
		ArrayList<SETNode> leaves=new ArrayList<SETNode>();
				
		if(leaf.depth+1>maxdepth){
			System.out.println("Symbolic execution stops in this path as creating a node at depth "+ (leaf.depth+1) +" is greater than "+maxdepth);
			// Should we create a node here or not?			
			done.add(in);
			result.setDoneNodes(done);
			result.setLiveNodes(leaves);
		}
		else{
			System.out.println("Creating a instruction statement at depth : "+(leaf.depth+1));
			//InstructionNode in=new InstructionNode(instructionStatement, leaf, null);
			leaves.add(in);
			result.setDoneNodes(done);
			result.setLiveNodes(leaves);
		}
		return result;
	}
	public Expression sym_eval(Expression e, SETNode leaf){
		
		return e;
		
	}
	public Expression negation(Expression e){
		
		return e;
	}
   

    public boolean satisfiable(Expression e) // need to implement this
    {
		
        return true;
    }


}
