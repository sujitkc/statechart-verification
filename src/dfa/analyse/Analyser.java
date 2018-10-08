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
    Set<State> totalStateRegions=new HashSet<State>();
    totalStateRegions.add(statechart);
    Metric.getAllStates(statechart,totalStateRegions);
    Set<Transition> totalTransitionRegions=new HashSet<Transition>();
    totalTransitionRegions=Metric.getAllTransitions(statechart);
    System.out.println("Printing all states");
    for(State s : totalStateRegions){
	for(Declaration d:s.declarations)
	{
	if(d.input) s.setWriteVariable(d);
	}
	analyseStatements((StatementList)s.entry,s);
	analyseStatements((StatementList)s.exit,s);
	System.out.println("**"+s.getFullName());
	System.out.println("Actual write variables: "+ s.writeVariables);
	System.out.println("Actual read variables: "+ s.readVariables);
    }
    
    //System.out.println("Printing all transitions");
    for(Transition t : totalTransitionRegions){
	analyseStatements(t);
	
	if(t.guard instanceof ast.BinaryExpression)
		t.setReadVariable(((BinaryExpression)t.guard).getVariables());
	else if(t.guard instanceof ast.FunctionCall)
		t.setReadVariable(((FunctionCall)t.guard).getVariables());
	System.out.println("**Transition : "+t.name);
	System.out.println("Actual write variables: "+ t.writeVariables);
	System.out.println("Actual read variables: "+ t.readVariables);
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
    Metric.allTransitions(statechart, allTransitions);
    
    
    Metric.Wscope(statechart, Wscope, allTransitions);
	
   // Metric.ActualWscope(statechart, WscopeActual, allTransitions);
    
    float WscopeTotal=0;
    float ActualWscopeTotal=0;
    double WStablscopescore=0.000000f;
    double WStateChartscopescore=0.0000000f;
    System.out.println("\n\nWscopeActual -- Begin");
    for(String vname : Wscope.keySet()) {
      WscopeActual.put(vname,Metric.ActualWscope(vname,totalStateRegions,totalTransitionRegions));
	  System.out.println(vname + " : " + WscopeActual.get(vname) +" / "+Wscope.get(vname));
      WscopeTotal+=Wscope.get(vname);
      ActualWscopeTotal+=WscopeActual.get(vname);
      WStablscopescore+=(float)WscopeActual.get(vname)/(float)Wscope.get(vname);
    }
    WStateChartscopescore=ActualWscopeTotal/WscopeTotal;
    System.out.println("Wscope Total is "+WscopeTotal+"\nActualWscope total is "+ActualWscopeTotal +" \nWrite scope score is Stabl:" + WStablscopescore + " Statechart : "+WStateChartscopescore+"\n\n");


    Map<String, Integer> Rscope = new HashMap<String, Integer>();
    Map<String, Integer> RscopeActual = new HashMap<String, Integer>();
    Metric.Rscope(statechart, Rscope, allTransitions);
	
    //Metric.ActualRscope(statechart, RscopeActual, allTransitions);
	
    float RscopeTotal=0.00000f;
    float ActualRscopeTotal=0.0000f;
    double RStablscopescore=0.00000f;
    double RStateChartscopescore=0.000000f;
    System.out.println("\n\nRscopeActual -- Begin");
	
    for(String vname : Rscope.keySet()) {
	
	  RscopeActual.put(vname,Metric.ActualRscope(vname,totalStateRegions,totalTransitionRegions));
      System.out.println(vname + " : " + RscopeActual.get(vname) +" / "+Rscope.get(vname));
    
	  RscopeTotal+=Rscope.get(vname);
      ActualRscopeTotal+=RscopeActual.get(vname);
	  
      RStablscopescore+=(float)RscopeActual.get(vname)/(float)Rscope.get(vname);
	 
    }
    RStateChartscopescore=ActualRscopeTotal/RscopeTotal;
    System.out.println("Rscope Total is "+RscopeTotal+"\nActualRscope total is "+ActualRscopeTotal +"\nRead scope score is Stabl: " + RStablscopescore + " Statechart : "+ RStateChartscopescore+"\n \n\n");
    System.out.println("Total Score of specification :");
	
    System.out.println("StaBL :"+ (WStablscopescore+RStablscopescore));
    System.out.println("Statechart :"+ (WStateChartscopescore+RStateChartscopescore));
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
  
  
  public static void analyseStatements(StatementList stmtlist,State s){
  for(Statement stmt : stmtlist.getStatements()){
	if(stmt instanceof ast.AssignmentStatement)
		{
		AssignmentStatement as=(AssignmentStatement)(stmt);
		s.setWriteVariable(as.getLHS());
		if(as.getRHS() instanceof ast.FunctionCall){
			s.setReadVariable((as.getRHS()).getVariables());
			if(((FunctionCall)as.getRHS()).name.name.startsWith("put"))
				s.setWriteVariable((as.getRHS()).getVariables().get(0));
		}
		else if(as.getRHS() instanceof ast.BinaryExpression || as.getRHS() instanceof ast.Name){
			s.setReadVariable((as.getRHS()).getVariables());
		}
		else s.setReadVariable((as.getRHS()).getVariables());
		}
	else if(stmt instanceof ast.ExpressionStatement){
			Expression e=(Expression)((ExpressionStatement)stmt).expression;
			//UnaryExpression ue=(UnaryExpression)((ExpressionStatement)stmt).expression;
			if( e instanceof ast.UnaryExpression){
			UnaryExpression ue=(UnaryExpression)((ExpressionStatement)stmt).expression;
			s.setReadVariable(ue.getVariables());
			s.setWriteVariable(ue.getVariables());
			}
			//FunctionCall fc=(FunctionCall)((ExpressionStatement)stmt).expression;
			else if(e instanceof ast.FunctionCall){
			FunctionCall fc=(FunctionCall)((ExpressionStatement)stmt).expression;
			s.setReadVariable(fc.getVariables());
			if(fc.name.name.startsWith("put"))
				s.setWriteVariable(fc.getVariables().get(0));
				}
		}
	}
  
  }
  
  public static void analyseStatements(Transition t){
  	if(t.action instanceof ast.AssignmentStatement){
		AssignmentStatement as=(AssignmentStatement)t.action;
		t.setWriteVariable((as.getLHS()));
		if(as.getRHS() instanceof ast.FunctionCall){
			t.setReadVariable((as.getRHS()).getVariables());
			if(((FunctionCall)as.getRHS()).name.name.startsWith("put"))
				t.setWriteVariable((as.getRHS()).getVariables().get(0));
		}
		if( as.getRHS() instanceof ast.UnaryExpression || as.getRHS() instanceof ast.BinaryExpression || as.getRHS() instanceof ast.Name)
			t.setReadVariable((as.getRHS()).getVariables());
		
	}
	else if (t.action instanceof ast.ExpressionStatement){
		//ExpressionStatement es=(ExpressionStatement)t.action;
		//t.setWriteVariable((es.getLHS()).getName());
	}
	else 
	{
		StatementList taction=(StatementList)t.action;
		for(Statement stmt : taction.getStatements()){
		if(stmt instanceof ast.AssignmentStatement){
			AssignmentStatement as;
			as=(AssignmentStatement)stmt;
			t.setWriteVariable(as.getLHS());
			if(as.getRHS() instanceof ast.FunctionCall){
			t.setReadVariable((as.getRHS()).getVariables());
			if(((FunctionCall)as.getRHS()).name.name.startsWith("put"))
				t.setWriteVariable((as.getRHS()).getVariables().get(0));
			
		}
		if( as.getRHS() instanceof ast.UnaryExpression || as.getRHS() instanceof ast.BinaryExpression || as.getRHS() instanceof ast.Name)
			t.setReadVariable((as.getRHS()).getVariables());
		
		}
		else if(stmt instanceof ast.ExpressionStatement){
			ExpressionStatement as;
			as=(ExpressionStatement)stmt;
			// Not yet complete
			//System.out.println("Expression statement: "+as);
			//t.setWriteVariable((as.getLHS()).getName());
			}
		}
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
