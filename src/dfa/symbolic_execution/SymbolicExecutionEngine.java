package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;

import java.util.ArrayList;
import java.util.List;

public class SymbolicExecutionEngine{
    
    private Statechart statechart;
    private int depth=0;
	private int maxdepth=100;
    public SymbolicExecutionEngine(Statechart statechart) throws Exception{
        try{
            this.statechart = statechart;
			//ArrayList<SETNode> done = new ArrayList<SETNode>();
			ArrayList<SETNode> leaves = new ArrayList<SETNode>();
			leaves.add(new SETNode(null));
			SymbolicExecutionResult sym=new SymbolicExecutionResult();
			sym.live.add(new SETNode(null));
			this.executeasSym((State)statechart,sym);
            //this.execute("init",(State)statechart,leaves);
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
	public ArrayList<Transition> getOutgoingTransitions(State curr){
		ArrayList<State> conf=new ArrayList<State>();
		conf.addAll(curr.getAllSuperstates());
		conf.addAll(getConfiguration(curr));
		int level=0;
			ArrayList<Transition> all_ts=new ArrayList<Transition>();
			ArrayList<Transition> out_ts=new ArrayList<Transition>(); 
			/*System.out.println("current configuration");
		    for(State s:conf)
			System.out.print(s.getFullName()+", ");*/
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
			return out_ts;
	}
	public SymbolicExecutionResult executeasSym(State currstate, SymbolicExecutionResult sym){
			
		 SymbolicExecutionResult result=new SymbolicExecutionResult();
		 
		 SymbolicExecutionResult enterStateResult=enterSuperState(currstate,sym);
		 result.done.addAll(enterStateResult.done);
		 result.live.addAll(enterStateResult.live);
		 System.out.println("Live nodes after entering the states :"+result.live.size());
		 //Get all the outgoing transitions of the configuration
		 ArrayList<Transition> out_ts=new ArrayList<Transition>();
		 ArrayList<State> conf=getConfiguration(currstate);
		 State s=conf.get(conf.size()-1);
		 if(s!=null) out_ts.addAll(getOutgoingTransitions(s));
		 
		 //for each outgoing transition from the current state, take all leaves and create new SETNodes
		 SymbolicExecutionResult liveNodes=new SymbolicExecutionResult();
		 liveNodes.live.addAll(result.live); //only the live nodes after enter state
		 SymbolicExecutionResult liveNodesAfterTransitionAction=new SymbolicExecutionResult();
		 //for(SETNode leaf: liveNodes.live){
		for(Transition t:out_ts){
			SymbolicExecutionResult transitionResult = this.callTransitionAction(s, t, liveNodes);
			result.done.addAll(transitionResult.done);
			liveNodesAfterTransitionAction.live.addAll(transitionResult.live);
		}
		 //}
		 
		 //removing enterstate live nodes and adding transition action live nodes - this should happen only of there were outgoing transitions otherwise livenodes remains the same
		 if(out_ts.size()>0){
			 result.live.removeAll(liveNodes.live);
			 result.live.addAll(liveNodesAfterTransitionAction.live);
		 }
		 
		 
		 
		 return result;
	}
    // TODO
	
	public SymbolicExecutionResult callTransitionAction(State currstate, Transition t, SymbolicExecutionResult sym) // need to implement this
    {
		
		SymbolicExecutionResult result=new SymbolicExecutionResult();
		//Exit the state now
		 //for each node in result.live exit state transitively till destination
        State source = currstate;
        State destination = t.getDestination();
		System.out.println("Executing the transition between source: "+(t.getSource()).getFullName()+ " destination :"+destination.getFullName()+"with current state : "+currstate.getFullName());
		SymbolicExecutionResult res=newExitState(source, destination, sym);
		
		SymbolicExecutionResult computeResultAfterTransition=new SymbolicExecutionResult();
		//Call the transition action
		//for(SETNode exitleaf:res.live){
			System.out.println("Calling transition action .. "+(t.action).getClass());
			List<Statement> stmtlist=((StatementList)t.action).getStatements();
			SymbolicExecutionResult transresult=newExecuteBlock(stmtlist,res);
			computeResultAfterTransition.done.addAll(transresult.done);
			computeResultAfterTransition.live.addAll(transresult.live);
		//}
		
		//call executeasSym for the new state
		//for(SETNode leaf:computeResultAfterTransition.live){
			executeasSym(destination,computeResultAfterTransition);
		//}
		
		return result;
    }



	/*Enter Super State */
	
	  public SymbolicExecutionResult enterSuperState(State st, SymbolicExecutionResult sym)
    {
		System.out.println("Entering state : "+st.getFullName());
		ArrayList<SETNode> liveNodes=new ArrayList<SETNode>();
		liveNodes.addAll(sym.live);
        for(SETNode currleaf:liveNodes){
			if(currleaf.depth+1<maxdepth){
				SETNode leaf = new StateEntryNode(st, currleaf);
				
				Environment e = st.getEnvironment();
				DeclarationList dl = e.getDeclarations();
				
				for(int i=0;i<dl.size();i++){
					sym.done.add(leaf); // Initially it adds stateEntryNode to done, in further iterations it adds the node that was created in previous iteration
											// Instruction node created in last iteration won't get added to done list where as it will get added to leaves to call execute block
					Declaration d=dl.get(i);
					String s = d.getScope().name;
					if(s.equals("local")){
						System.out.println("local variable found: "+s);
						//3rd parameter to be worked out
						
						leaf = new InstructionNode(d, leaf, null); 
						// Type ast needs to have an initial value 
					}
					else if(s.equals("static")){
						System.out.println("static variable found: "+s);
						// First entry to the state
						leaf = new InstructionNode(d, leaf, null); 
						// Need to address 'not the first entry to the state'
					}
					else if(s.equals("parameter")){
						System.out.println("Parameter variable found : "+s);
						leaf = new InstructionNode(d, leaf, null); 
					}
				}
				ArrayList<SETNode> leaves=new ArrayList<SETNode>();
				leaves.add(leaf); //if there are no declarations, the state entry node becomes the leaf or after declaration there is only one leaf
				System.out.println("Calling execute block at st.entry : with " + (st.entry).getClass());
				SymbolicExecutionResult inexec=new SymbolicExecutionResult();
				inexec.live.addAll(leaves);
				List<Statement> stmtlist=((StatementList)st.entry).getStatements();
				SymbolicExecutionResult executeBlockResult = newExecuteBlock(stmtlist,inexec); 
				sym.done.addAll(executeBlockResult.getDoneNodes());
				sym.live.addAll(executeBlockResult.getLiveNodes());
			}
		}
		sym.live.removeAll(liveNodes);
		sym.done.addAll(liveNodes);
		if(!st.states.isEmpty()) {
				return enterSuperState(st.states.get(0),sym);
			}
		else {
				return sym;
			}
    }


	/*Enter State - Symbolic Execution Result*/
	
	

	public SymbolicExecutionResult newExitState(State src, State dest, SymbolicExecutionResult sym){
		System.out.println("Exit state called for source :" +src.getFullName());
		State lub=statechart.lub(src,dest);
		List<State> a1 = src.getAllSuperstates();
		List<State> a2 = dest.getAllSuperstates();
		ArrayList<SETNode> liveNodes=new ArrayList<SETNode>();
		liveNodes.addAll(sym.live);
		System.out.println("Livenodes size : "+ liveNodes.size());
        for(SETNode currleaf:liveNodes){
			if(currleaf.depth+1<maxdepth){
				SETNode leaf = new StateExitNode(src, currleaf);
				ArrayList<SETNode> leaves=new ArrayList<SETNode>();
				leaves.add(leaf);
				System.out.println("Calling execute block at st.exit : with " + (src.exit).getClass());
				SymbolicExecutionResult inexec=new SymbolicExecutionResult();
				inexec.live.addAll(leaves);
				List<Statement> stmtlist=((StatementList)src.exit).getStatements();
				SymbolicExecutionResult executeBlockResult = newExecuteBlock(stmtlist,inexec); 
				sym.done.addAll(executeBlockResult.done);
				sym.live.addAll(executeBlockResult.live);
				}
		}
		sym.live.removeAll(liveNodes);
		
		
		if(a1.size()>0&&!a2.contains(a1.get(a1.size()-1))){
			return newExitState(a1.get(a1.size()-1),dest,sym);
		}
		else return sym;
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
	
	
	public SymbolicExecutionResult newExecuteBlock(List<Statement> stmtlist, SymbolicExecutionResult sym){
		
		
		//List<Statement> stmtlist=((StatementList)a).getStatements();
		if(stmtlist.size()>0){
			Statement stmt=stmtlist.get(0);
			//Get the current live nodes, iterate over and create new set of live nodes
			ArrayList<SETNode> liveNodes=new ArrayList<SETNode>();
			liveNodes.addAll(sym.live);
			ArrayList<SETNode> newLiveNodes=new ArrayList<SETNode>();
			System.out.println("New set of leaves is being created with statement : "+stmt);
			for(SETNode leaf: liveNodes){
				if(stmt instanceof InstructionStatement)
				{
					newLiveNodes.add(newExecuteInstruction(stmt,leaf));
				}
				else if(stmt instanceof IfStatement){
					newExecuteIf(stmt,sym);
				}
				else{
					System.out.println("Unhandled Instruction");
				}
			}
			//add the livenodes to done
			sym.done.addAll(liveNodes);
			sym.live.removeAll(liveNodes); //remove the livenodes from live list
			sym.live.addAll(newLiveNodes); //add new live nodes to live list
			//remove the current statement from statement list and call execute block for rest of the statements
			stmtlist.remove(stmt);
			return newExecuteBlock(stmtlist,sym);
		}
		else 
			return sym;
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
			SymbolicExecutionResult result_=new SymbolicExecutionResult();
			if((((StatementList)a).getStatements()).size()>0){
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
			else{
				result.setLiveNodes(leaves);
			}
        }
       return result;
    }


	public SymbolicExecutionResult newExecuteIf(Statement ifStatement, SymbolicExecutionResult sym){
		ArrayList<SETNode> currentLiveNodes=new ArrayList<SETNode>();
		currentLiveNodes.addAll(sym.live);
		for(SETNode leaf:currentLiveNodes){
		Expression c=sym_eval(((IfStatement)ifStatement).condition,leaf);
		DecisionNode decisionNode=new DecisionNode(((IfStatement)ifStatement).condition, leaf, null);
		sym.live.add(decisionNode);
		if(satisfiable(c)){
					SymbolicExecutionResult res = newExecuteBlock(((StatementList)((IfStatement)ifStatement).then_body).getStatements(), sym);
					sym.done.addAll(res.done);
					sym.live.addAll(res.live);
					
			}
		if(satisfiable(negation(c))){			
					SymbolicExecutionResult res = newExecuteBlock(((StatementList)((IfStatement)ifStatement).else_body).getStatements(), sym);
					sym.done.addAll(res.done);
					sym.live.addAll(res.live);
			}
		}
		sym.done.addAll(currentLiveNodes);
		sym.live.removeAll(currentLiveNodes);
		return sym;
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
	
public InstructionNode newExecuteInstruction(Statement instructionStatement,SETNode leaf){
	return new InstructionNode(instructionStatement,leaf,null);
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
