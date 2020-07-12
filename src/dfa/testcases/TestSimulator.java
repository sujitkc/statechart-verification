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

import simulator.*;

import ast.State;
import ast.Statechart;
import ast.Transition;
import ast.Environment;
import ast.Declaration;
import ast.Name;

public class TestSimulator {

  @Test
  public void testSimulator() {
    System.out.println("Hello front end.");

    Typechecker typechecker;

    Statechart statechart = null;
    String input = "data/curfew_structs_minimal.txt";
    // String input = "data/s24.txt";
    // String input = "data/curfew1.txt";
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
      System.exit(1);
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

      // creating a simulator
    try
    {
      new Simulator(statechart);
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }
}
