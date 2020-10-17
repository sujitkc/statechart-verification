package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;

import java.util.*;
import solver.*;

public class SymbolicExecutionEngine{
    
    private Statechart statechart;
    private int depth=0;
	private int maxdepth=10;
	public Set<String> symvars;
	public Set<Name> symvarsname;
	
    public SymbolicExecutionEngine(Statechart statechart) throws Exception{
        try{
			this.symvars=new HashSet<String>();
			this.symvarsname=new HashSet<Name>();

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
		if(sym.live.size()>0){	
		 
		 SymbolicExecutionResult enterStateResult=enterSuperState(currstate,sym);
		 result.done.addAll(enterStateResult.done);
		 result.live.addAll(enterStateResult.live);
		 System.out.println("Live : "+result.live.size()+" Done : "+result.done.size());
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
		 else{
			 System.out.println("Max depth reached..");
			 System.exit(0);
			 return null;
		 }
			 
	}
    // TODO
	
	public SymbolicExecutionResult callTransitionAction(State currstate, Transition t, SymbolicExecutionResult sym) // need to implement this
    {
		
		SymbolicExecutionResult result=new SymbolicExecutionResult();
		//Exit the state now
		 //for each node in result.live exit state transitively till destination
        State source = currstate;
        State destination = t.getDestination();
		System.out.println("Executing the transition between source: "+(t.getSource()).getFullName()+ " destination :"+destination.getFullName()+" with current state : "+currstate.getFullName());
		SymbolicExecutionResult res=newExitState(source, destination, sym);
		
		SymbolicExecutionResult computeResultAfterTransition=new SymbolicExecutionResult();
		//Call the transition action
		//for(SETNode exitleaf:res.live){
			System.out.println("Calling transition action .. "+(t.action).getClass());
			//List<Statement> stmtlist=((StatementList)t.action).getStatements();
			SymbolicExecutionResult transresult=newExecuteBlock(t.action,res);
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
		//Get all the current live nodes
		ArrayList<SETNode> liveNodes=new ArrayList<SETNode>();
		liveNodes.addAll(sym.live);
		//for each leaf in the live nodes
		StatementList entryBlock=new StatementList();
		entryBlock.add(st.entry);
        for(SETNode currleaf:liveNodes){
			entryBlock.makeEmpty();
			entryBlock.add(st.entry);
			if(currleaf.depth<maxdepth){ // if depth of leaf is less than maxdepth
				SETNode leaf = new StateEntryNode(st, currleaf);
				Environment e = st.getEnvironment();
				//get the declarations in the state
				DeclarationList dl = e.getDeclarations();
				ArrayList<SETNode> leaves=new ArrayList<SETNode>();
				//for each declaration create instruction node - add the last declaration as live
				//if there is no declaration, state entry node becomes the leaf
				for(int i=0;i<dl.size();i++){
					
					Declaration d=dl.get(i);
					String s = d.getScope().name;
				if(leaf.depth<maxdepth){
						if(s.equals("local")){
							leaf = new InstructionNode(d, leaf, null); 						
						}
						else if(s.equals("static")){
							leaf = new InstructionNode(d, leaf, null);
						}
						else if(s.equals("parameter")){
							leaf = new InstructionNode(d, leaf, null); 
						}
					}
					else{
						sym.done.add(leaf);
						break;
					}
					System.out.println("Instruction node created : Live: "+leaves.size() +" Done : "+sym.done.size());
				}
				
				leaves.add(leaf);
				//if there are no declarations, the state entry node becomes the leaf or after declaration there is only one leaf
				//System.out.println("*Live : "+leaves.size() +" Done : "+sym.done.size());
				System.out.println("Calling execute block at st.entry : ");
				SymbolicExecutionResult inexec=new SymbolicExecutionResult();
				inexec.live.addAll(leaves);
					SymbolicExecutionResult executeBlockResult = newExecuteBlock(entryBlock,inexec); 
					sym.done.addAll(executeBlockResult.done);
					sym.live.addAll(executeBlockResult.live);
					sym.live.removeAll(executeBlockResult.done);

					//System.out.println("**Live : "+sym.live.size() +" Done : "+sym.done.size());

			}
			else{
				sym.done.add(currleaf);
				sym.live.remove(currleaf);
			}
		}
		sym.live.removeAll(liveNodes);
		//sym.done.addAll(liveNodes);
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
		StatementList exitBlock=new StatementList();
		exitBlock.add(src.exit);
        for(SETNode currleaf:liveNodes){
			exitBlock.makeEmpty();
			exitBlock.add(src.exit);

			if(currleaf.depth+1<maxdepth){
				SETNode leaf = new StateExitNode(src, currleaf);
				ArrayList<SETNode> leaves=new ArrayList<SETNode>();
				leaves.add(leaf);
				System.out.println("Calling execute block at st.exit : " );
				SymbolicExecutionResult inexec=new SymbolicExecutionResult();
				inexec.live.addAll(leaves);
				//List<Statement> stmtlist=((StatementList)src.exit).getStatements(); //remove static cast
				SymbolicExecutionResult executeBlockResult = newExecuteBlock(exitBlock,inexec); 
				sym.done.addAll(executeBlockResult.done);
				sym.live.addAll(executeBlockResult.live);
				}
				else{
					sym.done.add(currleaf);
				}
		}
		sym.live.removeAll(liveNodes);
		
		if(a1.size()>0&&!a2.contains(a1.get(a1.size()-1))){
			return newExitState(a1.get(a1.size()-1),dest,sym);
		}
		else return sym;
	} 


   


  
	
	public SymbolicExecutionResult newExecuteBlock(Statement blockstmt, SymbolicExecutionResult sym){
		System.out.println("Executing Block...");
		List<Statement> stmtlist;
		if(blockstmt instanceof StatementList){
			stmtlist=((StatementList)blockstmt).getStatements();
			//System.out.println("The number of statements in the block is : "+stmtlist.size());
		}
		else stmtlist=new ArrayList<Statement>();
		if(stmtlist.size()>0){
			Statement stmt=stmtlist.get(0);
			//Get the current live nodes, iterate over and create new set of live nodes
			ArrayList<SETNode> liveNodes=new ArrayList<SETNode>();
			liveNodes.addAll(sym.live);
			//System.out.println("The leaf nodes from prev level is : "+sym.live.size());
			//ArrayList<SETNode> newLiveNodes=new ArrayList<SETNode>();
			//System.out.println("New set of leaves is being created with statement : "+stmt);
			//System.out.println("Number of live nodes "+liveNodes.size());
			//System.out.println("******** Statement "+stmtlist.size());

			SymbolicExecutionResult res=new SymbolicExecutionResult();
			res.done.addAll(sym.done);
			res.live.addAll(sym.live);
			for(SETNode leaf: liveNodes){
				/*if(leaf instanceof InstructionNode)
				System.out.println("inside next iteration of the leaf : " +((InstructionNode)leaf).statement);
				else if(leaf instanceof DecisionNode)
				System.out.println("inside next iteration of the leaf : " +((DecisionNode)leaf).expression);*/
				System.out.println("Statement found is instance of "+stmt.getClass());
				if(leaf.depth<maxdepth){
					if(stmt instanceof InstructionStatement)
					{
						sym.live.add(newExecuteInstruction(stmt,leaf));
						sym.live.remove(leaf);
						System.out.println("I*Live : "+sym.live.size()+" Done : "+sym.done.size());
					}
					else if(stmt instanceof IfStatement){
						res=newExecuteIf(stmt,leaf);
						sym.live.remove(leaf);
						sym.live.addAll(res.live);
						sym.done.addAll(res.done);
						//newLiveNodes.addAll(res.live);
						System.out.println("D*Live : "+sym.live.size()+" Done : "+sym.done.size());
					}
					else{
						System.out.println("Unhandled Instruction");
					}
				}
				else{
					 sym.done.add(leaf);
					 sym.live.remove(leaf);
				}
			}
			//add the livenodes to done
			sym.live.removeAll(liveNodes); //remove the livenodes from live list
			//sym.live.addAll(newLiveNodes); //add new live nodes to live list
			//remove the current statement from statement list and call execute block for rest of the statements
			stmtlist.remove(stmt);
			StatementList returnList =new StatementList();
			returnList.addAll(stmtlist);
			if(stmtlist.size()>0)
				return newExecuteBlock(returnList,sym);
			else
				return sym;
		}
		else {
			//System.out.println("Live nodes after execute block : "+(sym.live).size());
			return sym;
			}
	}

  

	public SymbolicExecutionResult newExecuteIf(Statement stmt, SETNode leaf){
		System.out.println("newExecuteIf : ");
		//ArrayList<SETNode> currentLiveNodes=new ArrayList<SETNode>();
		//currentLiveNodes.addAll(sym.live);
		SymbolicExecutionResult sym=new SymbolicExecutionResult();

		SymbolicExecutionResult res1=new SymbolicExecutionResult();
		SymbolicExecutionResult res2=new SymbolicExecutionResult();
		//System.out.println("*******************");
		//System.out.println("Decision node found : "+ifStatement);
		//System.out.println("Current live nodes : "+currentLiveNodes.size());
		int i=0;
		StatementList ifStatement_then=new StatementList();
		//ifStatement_then.add(((IfStatement)stmt).then_body);
		StatementList ifStatement_else=new StatementList();
		//ifStatement_else.add(((IfStatement)stmt).else_body);
		Expression cond=((IfStatement)stmt).condition;
		
		//for(SETNode leaf:currentLiveNodes){
			//System.out.println(i+++" ========"+currentLiveNodes.size()+"\n======"+stmt);
			ifStatement_then.makeEmpty();
			ifStatement_else.makeEmpty();
			ifStatement_then.add(((IfStatement)stmt).then_body);
			ifStatement_else.add(((IfStatement)stmt).else_body);
		Expression c=sym_eval(cond,leaf);
		DecisionNode decisionNode=new DecisionNode(cond, leaf, null);
		if(satisfiable(c)){
					res1.live.clear();
					res1.live.add(decisionNode);
					res1= newExecuteBlock(ifStatement_then, res1);
					res1.live.remove(decisionNode);
			}
		if(satisfiable(negation(c))){	
					 res2.live.clear();
					 res2.live.add(decisionNode);
					 //System.out.println("\n ****** Then body ********\n"+ifStatement_else);
					 res2 = newExecuteBlock(ifStatement_else, res2);
					 res2.live.remove(decisionNode);
			}
			sym.done.addAll(res1.done);
			sym.live.addAll(res1.live);
			sym.done.addAll(res2.done);
			sym.live.addAll(res2.live);
		//}
		//System.out.println("result1 live :"+(res1.live).size());
		//System.out.println("result2 live :"+(res2.live).size());

		//sym.done.addAll(currentLiveNodes);
		//sym.live.removeAll(currentLiveNodes);
		System.out.println("Sym live after if block :"+sym.live.size());
		return sym;
	}
 	
	
public InstructionNode newExecuteInstruction(Statement instructionStatement,SETNode leaf){
	if(instructionStatement instanceof AssignmentStatement &&(((AssignmentStatement)instructionStatement).rhs instanceof FunctionCall) && ((((FunctionCall)((AssignmentStatement)instructionStatement).rhs).name.getName()).equalsIgnoreCase("input")))
				{	
					
					InstructionNode in = new InstructionNode(instructionStatement,leaf,null,symvars);
					symvars.add(in.symval);
					symvarsname.addAll(in.newNames);
					return in;
				}
	else 
		return new InstructionNode(instructionStatement,leaf,null,symvars);
}
	public Expression sym_eval(Expression e, SETNode leaf){
		
		return e;
		
	}
	public Expression negation(Expression e){
		
		return e;
	}
   

    public boolean satisfiable(Expression e) // need to implement this
    {
		// Using SMT Solver
		Z3Solver solver = new Z3Solver(symvarsname, e);
//		ISolver solver = new DRealSolver(symVars, exp);
		SolverResult solution = solver.solve();
		System.out.println("Solution is :"+solution);
        return true;
    }
	/*private void computeExpression(SETDecisionNode node) throws Exception {
		SETExpressionVisitor visitor = new SETExpressionVisitor(node,
				"boolean");
		CFGDecisionNode cfgNode = (CFGDecisionNode) node.getCFGNode();
		if (node.getCondition() == null) {
			throw new Exception("Null Expression");
		} else {
			visitor.visit(cfgNode.getCondition());
			IExpression value = visitor.getValue();
			node.setCondition(value);
		}
	}*/

}
