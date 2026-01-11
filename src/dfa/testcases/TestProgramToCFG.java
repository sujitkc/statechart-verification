package testcases;

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

import org.junit.*;

import java_cup.runtime.Symbol;

import frontend.FrontEnd;
import frontend.Parser;
import frontend.Typechecker;

import ast.State;
import ast.Statechart;
import ast.Transition;
import ast.Environment;
import ast.Declaration;
import ast.Name;

import translation.Flattener;
import metric.Metric;
import translation.StatechartToProgramTranslator;
import translation.ProgramToCFG;
import program.Program;
import stablcfg.*;
public class TestProgramToCFG {

  @Test
  public void testProgramToCFG() {

    Typechecker typechecker;
    String input = "data/curfew1.txt";
    Statechart statechart = null;
    try {
      Parser parser = new FrontEnd(input).getParser();    
      Symbol result = parser.parse();
      statechart = (Statechart)result.value;
      System.out.println("Printing parsed Statechart ...");
      System.out.println(statechart.toString());
      System.out.println("Printing parsed Statechart ... done!");
    }
    catch(FileNotFoundException e) {
      System.out.println("Couldn't open file '" + input + "'"); 
    }
    catch(Exception e) {
      System.out.println("Couldn't parse '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }
    try {
      new Typechecker(statechart).typecheck();
      System.out.println(input + ": Printing typechecked Statechart ...");
      System.out.println(statechart.toString());
      System.out.println(input + ": Printing typechecked Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't typecheck '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }
    Statechart flattenedSC = null;
    try {
      Flattener flattener = new Flattener();
      flattenedSC = flattener.translate(statechart);
      System.out.println("Printing flattened Statechart ...");
      System.out.println(flattenedSC);
      System.out.println("Printing flattened Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't flatten '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }

    Program program = null;
    try {
      StatechartToProgramTranslator sctoprog = new StatechartToProgramTranslator(flattenedSC);
      program = sctoprog.translate();
      System.out.println("Printing Program ...");
      System.out.println(program);
      System.out.println("Printing Program ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't translate flattened statechart " + e.getMessage()); 
      e.printStackTrace();
    }

    try {
      ProgramToCFG programToCFG = new ProgramToCFG();
      CFG cfg = programToCFG.translate(program);
//      System.out.println("Printing flattened Statechart ...");
//      System.out.println(flattenedSC);
//      System.out.println("Printing flattened Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't flatten '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }


  }
}
