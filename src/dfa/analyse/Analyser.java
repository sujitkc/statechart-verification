package analyse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import java_cup.runtime.Symbol;

import ast.State;
import ast.Statechart;
import ast.Transition;
import ast.Environment;
import ast.Declaration;
import ast.Name;
import ast.*;
import flatten.Flattener;
import metric.Metric;

public class Analyser {

  private final Statechart statechart;
  private final Typechecker typechecker;

  public static void main(String[] args) {
    Statechart statechart = null;
    try {
      parser par = new parser(new Lexer(new FileReader(args[0])));    
      Symbol result = par.parse();
      statechart = (Statechart)result.value;
      System.out.println("Printing parsed Statechart ...");
      System.out.println(statechart.toString());
      System.out.println("Printing parsed Statechart ... done!");
    }
    catch(FileNotFoundException e) {
      System.out.println("Couldn't open file '" + args[0] + "'"); 
    }
    catch(Exception e) {
      System.out.println("Couldn't parse '" + args[0] + "' : " + e.getMessage()); 
      e.printStackTrace();
      System.exit(1);
    }
    try {
      (new Analyser(statechart)).analyse();
      //System.out.println("Printing analysed Statechart ...");
      //System.out.println(statechart);
      //System.out.println("Printing analysed Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't analyse '" + args[0] + "' : " + e.getMessage()); 
      e.printStackTrace();
    }

    System.out.println("statechart has " + Metric.getNumberOfStates(statechart) + " states.");
    System.out.println("statechart has " + Metric.getNumberOfCompositeStates(statechart) + " composite states.");
    
    System.out.println("statechart has " + Metric.getNumberOfTransitions(statechart) + " transitions.");
	/*
	* Variables are defined/used in entry/exit of states
	* Variables are defined/used in transitions
	* 1. Identify all state regions
	**** 1.1 For each state region
	*********1.1.1 add Input variables to writescope
	*********1.1.2 take the statementList in state.entry/exit/t.action
	***************identify all the LHS variables and add them to writescope
	***************identify all RHS variables and add them to read scope
	*********************** RHS variables can be BinaryStatement,Function Calls, constants, Name - handle each of them accordingly
	* 2. Identify all transition regions
	**** 2.1 for each transition region
	********* 2.1.1 take the Statement or StatementList in transition.action
	***************identify all the LHS variables and add them to writescope
	***************identify all RHS variables and add them to read scope
	*********************** RHS variables can be BinaryStatement,Function Calls, constants, Name - handle each of them accordingly
	********* 2.1.1 take the BinaryExpression/FunctionCall in transition.guard - identify variables and add to readscope

	* 
	*/
	//begin- identify all state regions
    Set<State> totalStateRegions=new HashSet<State>();
    totalStateRegions.add(statechart);
    Metric.getAllStates(statechart,totalStateRegions);
	//end - identify all state regions
	//begin - identify all transition regions
    Set<Transition> totalTransitionRegions=new HashSet<Transition>();
    totalTransitionRegions=Metric.getAllTransitions(statechart);
	//end - identify all transition regions
    System.out.println("Printing all states");
	//**** 1.1 For each state region
    for(State s : totalStateRegions){
		for(Declaration d:s.declarations)
		{
			if(d.input) s.setWriteVariable(d); //1.1.1 add Input variables to writescope - all variables added are of Object/Declaration type
		}
	analyseStatementsForRegion((StatementList)s.entry,s); //analyse statements in state.entry
	analyseStatementsForRegion((StatementList)s.exit,s);  //analyse statements in state.exit
	//System.out.println("**State : "+s.getFullName());
	//System.out.println("Actual write variables: "+ s.writeVariables); //Printing all variables written inside the state
	//System.out.println("Actual read variables: "+ s.readVariables);	  //Printing all variables read inside the state
    }
    
    //System.out.println("Printing all transitions");
    for(Transition t : totalTransitionRegions){
		analyseStatementsForRegion(t.action,t); //analyses the action statement
		analyseExpression(t.guard,t); // analyse guard Expression
	
	//System.out.println("**Transition : "+t.name);
	//System.out.println("Actual write variables: "+ t.writeVariables);
	//System.out.println("Actual read variables: "+ t.readVariables);
   }
    
    
    
    Map<String, Integer> scope_S = new HashMap<String, Integer>();
    Metric.scope_S(statechart, scope_S);
    System.out.println("\n\nScope_S -- Begin");
    for(String vname : scope_S.keySet()) {
      System.out.println(vname + " : " + scope_S.get(vname));
    }
    System.out.println("Scope_S -- End\n\n");

    Map<String, Integer> scope_T = new HashMap<String, Integer>();
    Metric.scope_T(statechart, scope_T);
    System.out.println("\n\nScope_T -- Begin");
    for(String vname : scope_T.keySet()) {
      System.out.println(vname + " : " + scope_T.get(vname));
    }
    System.out.println("Scope_T -- End\n\n");

    Map<String, Integer> Wscope = new HashMap<String, Integer>();
    Map<String, Integer> WscopeActual = new HashMap<String, Integer>();
    Set<Transition> allTransitions = new HashSet<Transition>();
    Metric.allTransitions(statechart, allTransitions); //find all transitions
 
    
    Metric.Wscope(statechart, Wscope, allTransitions); //Calculate the WScope for all variables
    float WscopeTotal=0;
    float ActualWscopeTotal=0;
    double WStablscopescore=0.000000f;
    double WStateChartscopescore=0.0000000f;
    System.out.println("\n\nWscopeActual -- Begin"); //Calculate Actual Wscope for each variable
    for(String vname : Wscope.keySet()) {
      WscopeActual.put(vname,Metric.ActualWscope(vname,totalStateRegions,totalTransitionRegions)); //Metric.ActualWscope is to calculate the actual write scope for each variable
	  System.out.println(vname + " : " + WscopeActual.get(vname) +" / "+Wscope.get(vname));
      WscopeTotal+=Wscope.get(vname);
      ActualWscopeTotal+=WscopeActual.get(vname); //StaBL write score
      WStablscopescore+=(float)WscopeActual.get(vname)/(float)Wscope.get(vname); //  Nw/NwTotal
    }
    WStateChartscopescore=ActualWscopeTotal/WscopeTotal; //Statechart write score
    System.out.println("Wscope Total is "+WscopeTotal+"\nActualWscope total is "+ActualWscopeTotal +" \nWrite scope score is Stabl:" + WStablscopescore + " Statechart : "+WStateChartscopescore+"\n\n");


    Map<String, Integer> Rscope = new HashMap<String, Integer>();
    Map<String, Integer> RscopeActual = new HashMap<String, Integer>();
    Metric.Rscope(statechart, Rscope, allTransitions); //Calculate the Rscope for all variables
	
    //Metric.ActualRscope(statechart, RscopeActual, allTransitions);
	
    float RscopeTotal=0.00000f;
    float ActualRscopeTotal=0.0000f;
    double RStablscopescore=0.00000f;
    double RStateChartscopescore=0.000000f;
    System.out.println("\n\nRscopeActual -- Begin");
	
    for(String vname : Rscope.keySet()) {
	
	RscopeActual.put(vname,Metric.ActualRscope(vname,totalStateRegions,totalTransitionRegions)); //Calculate Actual Rscope for each variable
	System.out.println(vname + " : " + RscopeActual.get(vname) +" / "+Rscope.get(vname));
	RscopeTotal+=Rscope.get(vname);
	ActualRscopeTotal+=RscopeActual.get(vname); //StaBL read score  
	RStablscopescore+=(float)RscopeActual.get(vname)/(float)Rscope.get(vname); //Nr/NrTotal
	 
    }
    RStateChartscopescore=ActualRscopeTotal/RscopeTotal; //Statechart read score
    System.out.println("Rscope Total is "+RscopeTotal+"\nActualRscope total is "+ActualRscopeTotal +"\nRead scope score is Stabl: " + RStablscopescore + " Statechart : "+ RStateChartscopescore+"\n \n\n");
    System.out.println("Total Score of specification :");
	
    System.out.println("StaBL :"+ (WStablscopescore+RStablscopescore));
    System.out.println("Statechart :"+ (WStateChartscopescore+RStateChartscopescore));
	Metric.printRWVariables();
/*
    Statechart flattenedSC = null;
    try {
      Flattener flattener = new Flattener();
      flattenedSC = flattener.translate(statechart);
      System.out.println("Printing flattened Statechart ...");
      System.out.println(flattenedSC);
      System.out.println("Printing flattened Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't flatten '" + args[0] + "' : " + e.getMessage()); 
      e.printStackTrace();
    }
*/
  }
  
  
 
  public static void analyseStatementsForRegion(Statement stmt, Region t){
  	if(stmt instanceof ast.AssignmentStatement){
		//analyseAssignmentStatement(stmt,t);
			AssignmentStatement as=(AssignmentStatement)stmt;
			t.setWriteVariable(as.getLHS()); //add LHS of statement to writeVariables
			analyseExpression(as.getRHS(),t);
	}
	else if (stmt instanceof ast.ExpressionStatement){	
			Expression e=(Expression)((ExpressionStatement)stmt).expression;
			analyseExpression(e,t);
	}
	else if(stmt instanceof ast.IfStatement){
			//System.out.println("If statement type :" +stmt.getClass());
			Expression e=(Expression)((IfStatement)stmt).condition;
			analyseExpression(e,t);
			analyseStatementsForRegion(((IfStatement)stmt).then_body,t);
			analyseStatementsForRegion(((IfStatement)stmt).else_body,t);
			
		}
	else if(stmt instanceof ast.WhileStatement){
			//System.out.println("while statement type :" +((WhileStatement)stmt).body.getClass());
			Expression e=(Expression)((WhileStatement)stmt).condition;
			analyseExpression(e,t);
			analyseStatementsForRegion(((WhileStatement)stmt).body,t);
		}
	else if(stmt instanceof ast.StatementList)
	{
		StatementList stmtlist=(StatementList)stmt;
		for(Statement st : stmtlist.getStatements()){
			analyseStatementsForRegion(st,t);
		
		}
	}
	

  
  }
   public static void analyseExpression(Expression e,Region t){
			
			
			if( e instanceof ast.UnaryExpression){ //if its UnaryExpression it is Written and Read
					UnaryExpression ue=(UnaryExpression)e;
					t.setReadVariable(ue.getVariables());
					t.setWriteVariable(ue.getVariables());
			}
			else if(e instanceof ast.FunctionCall){ //if its FunctionCall - first parameter(read and write), all other parameters(read) 
					FunctionCall fc=(FunctionCall)e;
					t.setReadVariable(fc.getVariables());
					if(fc.name.name.startsWith("put"))
						t.setWriteVariable(fc.getVariables().get(0));
			}
			else if( e instanceof ast.BinaryExpression || e instanceof ast.Name){
					t.setReadVariable(e.getVariables());
			}
  }
  public Analyser(Statechart statechart) {
    this.statechart = statechart;
    this.typechecker = new Typechecker(statechart);
  }

  public void analyse() throws Exception {
    this.typechecker.typecheck();
  }
}
