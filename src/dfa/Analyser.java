package analyse;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import java.io.FileNotFoundException;

import java_cup.runtime.Symbol;

import ast.State;
import ast.Statechart;
import ast.Transition;
import ast.Environment;
import ast.Declaration;
import ast.Name;

import frontend.FrontEnd;
import frontend.Parser;
import frontend.Typechecker;
import translation.*;
import metric.Metric;
import program.Program;

public class Analyser {

  private final Statechart statechart;
  private final Typechecker typechecker;

  public static void main(String[] args) {
    Statechart statechart = null;
    try {
      FrontEnd frontend = new FrontEnd(args[0]);
      Parser parser = frontend.getParser();    
      Symbol result = parser.parse();
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
    Set<Transition> allTransitions = new HashSet<Transition>();
    Metric.allTransitions(statechart, allTransitions);
    Metric.Wscope(statechart, Wscope, allTransitions);
    System.out.println("\n\nWscope -- Begin");
    for(String vname : Wscope.keySet()) {
      System.out.println(vname + " : " + Wscope.get(vname));
    }
    System.out.println("Wscope -- End\n\n");


    Map<String, Integer> Rscope = new HashMap<String, Integer>();
    Metric.Rscope(statechart, Rscope, allTransitions);
    System.out.println("\n\nRscope -- Begin");
    for(String vname : Rscope.keySet()) {
      System.out.println(vname + " : " + Rscope.get(vname));
    }
    System.out.println("Rscope -- End\n\n");


    Statechart flattenedSC = null;
    try {
      Flattener flattener = new Flattener();
      flattenedSC = flattener.translate(statechart);
      System.out.println("Printing flattened Statechart ...");
      System.out.println(flattenedSC);
      System.out.println("Printing flattened Statechart ... done!");

      StatechartToProgramTranslator translator = new StatechartToProgramTranslator (flattenedSC);
      System.out.println("Printing flattened program ...");
      Program program = translator.translate();
      System.out.println(program.toString());
      System.out.println("Printing flattened program ... done!");

      (new ProgramToCpp(program)).translate();
    }
    catch(Exception e) {
      System.out.println("Couldn't flatten '" + args[0] + "' : " + e.getMessage()); 
      e.printStackTrace();
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
