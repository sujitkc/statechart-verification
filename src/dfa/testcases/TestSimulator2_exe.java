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

import ast.*;

import simulator2.*;
import simulator2.cfg.*;
import simulator2.tree.*;
import simulator2.simulator.*;

public class TestSimulator2_exe {
  // CODE SIMULATION AND INTERPRETATION - BEGIN ///////////////////////////////
  // The event enables a transition emenating from an 
  // active atomic state (A) and lands on an atomic state (B).
  // Expected result: Resultant configuration = {B}.
  @Test
  public void test_1() {
    Statechart statechart = this.test_template("test_1", "data/constabl/simulator2/t1-1.stbl");
    try
    {
      State A = statechart.findSubstateByName("A");
      System.out.println(A.name);
      Set<State> configuration = new HashSet<>();
      configuration.add(A);
      Simulator simulator = new Simulator(statechart, configuration);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      e.printStackTrace();
      System.out.println("Something Went Wrong!\n"+e);
      System.exit(1);
    }
  }


  // From atomic state to shell state.
  // From shell state to atomic state.
  @Test
  public void test_9() {
    Statechart statechart = this.test_template("test_9", "data/constabl/simulator2/t9.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("A", statechart);
      System.out.println(A.name);
      configuration.add(A);
      simulator.simulationStep("e1");
//      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n"+e);
      e.printStackTrace();
      System.exit(1);
    }
  }

  // From shell state to composite state.
  @Test
  public void test_11() {
    Statechart statechart = this.test_template("test_11", "data/constabl/simulator2/t11.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State s1 = simulator.getSubstateByName("B1A", statechart);
      State s2 = simulator.getSubstateByName("B2B", statechart);
      configuration.add(s1);
      configuration.add(s2);
      System.out.println(s1.name);
      System.out.println(s2.name);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n"+e);
      System.exit(1);
    }
  }
 


  // CODE SIMULATION AND INTERPRETATION - END    //////////////////////////////
  public Statechart test_template(String testName, String inputFileName) {
    System.out.println(testName);

    Typechecker typechecker;

    Statechart statechart = null;
    String input = inputFileName;
    try {
      Parser parser = new FrontEnd(input).getParser();    
      Symbol result = parser.parse();
      statechart = (Statechart)result.value;
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
      return statechart;
    }
    catch(Exception e) {
      System.out.println("Couldn't typecheck '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }
    return null;
  }
}
