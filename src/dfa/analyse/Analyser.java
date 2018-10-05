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
      System.out.println("Printing analysed Statechart ...");
      System.out.println(statechart);
      System.out.println("Printing analysed Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't analyse '" + args[0] + "' : " + e.getMessage()); 
      e.printStackTrace();
    }

    System.out.println("statechart has " + Metric.getNumberOfStates(statechart) + " states.");
    System.out.println("statechart has " + Metric.getNumberOfTransitions(statechart) + " transitions.");
    Set<State> totalStateRegions=new HashSet<State>();
    totalStateRegions=Metric.getAllStates(statechart);
    Set<Transition> totalTransitionRegions=new HashSet<Transition>();
    totalTransitionRegions=Metric.getAllTransitions(statechart);
    System.out.println("Printing all states");
    for(State s : totalStateRegions){
	System.out.println("**"+s.getFullName());
    }
    System.out.println("Printing all transitions");
    for(Transition t : totalTransitionRegions){
	System.out.println("**"+t.toString());
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
    Metric.ActualWscope(statechart, WscopeActual, allTransitions);
    
    float WscopeTotal=0;
    float ActualWscopeTotal=0;
    float WStablscopescore=0;
    double WStateChartscopescore=0;
    System.out.println("\n\nWscopeActual -- Begin");
    for(String vname : Wscope.keySet()) {
      System.out.println(vname + " : " + WscopeActual.get(vname));
      WscopeTotal+=Wscope.get(vname);
      ActualWscopeTotal+=WscopeActual.get(vname);
      WStablscopescore+=WscopeActual.get(vname)/Wscope.get(vname);
    }
    WStateChartscopescore=ActualWscopeTotal/WscopeTotal;
    System.out.println("Wscope Total is "+WscopeTotal+" ActualWscope total is "+ActualWscopeTotal +" and scope score is Stabl:" + WStablscopescore + " Statechart : "+WStateChartscopescore+" -- End\n\n");


    Map<String, Integer> Rscope = new HashMap<String, Integer>();
    Map<String, Integer> RscopeActual = new HashMap<String, Integer>();
    Metric.Rscope(statechart, Rscope, allTransitions);
    Metric.ActualRscope(statechart, RscopeActual, allTransitions);
    float RscopeTotal=0;
    float ActualRscopeTotal=0;
    float RStablscopescore=0;
    float RStateChartscopescore=0.000000f;
    System.out.println("\n\nRscopeActual -- Begin");
    for(String vname : Rscope.keySet()) {
      System.out.println(vname + " : " + RscopeActual.get(vname));
      RscopeTotal+=Rscope.get(vname);
      ActualRscopeTotal+=RscopeActual.get(vname);
      RStablscopescore+=RscopeActual.get(vname)/Rscope.get(vname);
    }
    RStateChartscopescore=ActualRscopeTotal/RscopeTotal;
    System.out.println("Rscope Total is "+RscopeTotal+" ActualRscope total is "+ActualRscopeTotal +" and scope score is Stabl: " + RStablscopescore + " Statechart : "+String.format("%.5f", RStateChartscopescore)+" -- End\n\n");
    System.out.println("Total Score of specification :");
    System.out.println("StaBL :"+ (WStablscopescore+RStablscopescore));
    System.out.println("Statechart :"+ (WStateChartscopescore+RStateChartscopescore));
    System.out.println(Metric.randomNumberInRange(0,5));
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

  public Analyser(Statechart statechart) {
    this.statechart = statechart;
    this.typechecker = new Typechecker(statechart);
  }

  public void analyse() throws Exception {
    this.typechecker.typecheck();
  }
}
