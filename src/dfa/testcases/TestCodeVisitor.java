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

public class TestCodeVisitor {
  @Test
  public void testSimulator_1() {
    System.out.println("Running testSimulator_1 ...");

    Typechecker typechecker;

    Statechart statechart = null;
    String input="data/constabl/simulator2/c10-sim2.stb";
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
    }
    catch(Exception e) {
      System.out.println("Couldn't typecheck '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }

    try
    {
      Simulator simulator = new Simulator(statechart);
      TreeMap<State, String> map = new TreeMap<>();
      Function<State, String> function = new Function<>() {
        public String apply(State state) {
          return state.name;
        }
      };
      System.out.println(map.map(function, simulator.stateTree));
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n" +e );
      System.exit(1);
    }
  }
/*
  // The event enables a transition emenating from an 
  // active atomic state (A) and lands on an atomic state (B).
  // Expected result: Resultant configuration = {B}.
  @Test
  public void test_1() {
    Statechart statechart = this.test_template("test_1", "data/constabl/simulator2/t1.stbl");
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
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  // The event enables a transition emenating from an 
  // active atomic state (A) and lands on an atomic state (B).
  // The second event enables a reverse transition.
  // Expected result: Resultant configuration = {A}.
  @Test
  public void test_2() {
    Statechart statechart = this.test_template("test_2", "data/constabl/simulator2/t2.stbl");
    try
    {
      State A = statechart.findSubstateByName("A");
      System.out.println(A.name);
      Set<State> configuration = new HashSet<>();
      configuration.add(A);
      Simulator simulator = new Simulator(statechart, configuration);
      simulator.simulationStep("e1");
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  // The event enables a transition emenating from an 
  // active atomic state (AA) with an ancestor (A) and lands on an atomic state (B).
  // Expected result: Resultant configuration = {B}.
  @Test
  public void test_3() {
    Statechart statechart = this.test_template("test_3", "data/constabl/simulator2/t3.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("AA", statechart);
      System.out.println(A.name);
      configuration.add(A);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  // The event enables a transition emenating from an ancestor(A) of 
  // the active atomic state (AA) and lands on an atomic state (B).
  // Expected result: Resultant configuration = {B}.
  @Test
  public void test_4() {
    Statechart statechart = this.test_template("test_4", "data/constabl/simulator2/t4.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("AA", statechart);
      System.out.println(A.name);
      configuration.add(A);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  // The event enables a transition emenating from 
  // the active atomic state (AA) and lands on a atomic state (BB).
  // Expected result: Resultant configuration = {BB}.
  @Test
  public void test_5() {
    Statechart statechart = this.test_template("test_5", "data/constabl/simulator2/t5.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("AA", statechart);
      System.out.println(A.name);
      configuration.add(A);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  // The event enables a transition emenating from 
  // the active atomic state (AA) and lands on a composite state (B).
  // Expected result: Resultant configuration = {BB}.
  @Test
  public void test_6() {
    Statechart statechart = this.test_template("test_6", "data/constabl/simulator2/t6.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("AA", statechart);
      System.out.println(A.name);
      configuration.add(A);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  // The event enables a transition emenating from an ancestor (A) of
  // the active atomic state (AA) and lands on a composite state (B).
  // Expected result: Resultant configuration = {BB}.
  @Test
  public void test_7() {
    Statechart statechart = this.test_template("test_7", "data/constabl/simulator2/t7.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("AA", statechart);
      System.out.println(A.name);
      configuration.add(A);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  // The event doesn't enable a transition emenating from an ancestor of
  // the active atomic state.
  // Expected result: No transition should happen.
  @Test
  public void test_8() {
    Statechart statechart = this.test_template("test_8", "data/constabl/simulator2/t7.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("BB", statechart);
      System.out.println(A.name);
      configuration.add(A);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }
*/

  /// CONCURRENT CONFIGURATION AND TRANSITION - BEGIN /////////////////////////

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
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n"+e);
      System.exit(1);
    }
  }

//  // from atomic two atomic states to two atomic states within two AND siblings.
//  @Test
//  public void test_10() {
//    Statechart statechart = this.test_template("test_10", "data/constabl/simulator2/t10.stbl");

//    try
//    {
//      Set<State> configuration = new HashSet<>();
//      Simulator simulator = new Simulator(statechart, configuration);
//      State A = simulator.getSubstateByName("A", statechart);
//      System.out.println(A.name);
//      configuration.add(A);
//      simulator.simulationStep("e1");
//      simulator.simulationStep("e1");
//    }
 //   catch(Exception e) {
//      System.out.println("Something Went Wrong!\n");
//      System.exit(1);
//    }
//  }
  // From composite state to shell state.
  // From shell state to composite state.
  @Test
  public void test_11() {
    Statechart statechart = this.test_template("test_11", "data/constabl/simulator2/t11.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State s1 = simulator.getSubstateByName("AB", statechart);
      System.out.println(s1.name);
      configuration.add(s1);
      simulator.simulationStep("e1");
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n"+e);
      System.exit(1);
    }
  }
  /// CONCURRENT CONFIGURATION AND TRANSITION - END ///////////////////////////
/*
  // CODE SIMULATION AND INTERPRETATION - BEGIN ///////////////////////////////
  // The event enables a transition emenating from an 
  // active atomic state (A) and lands on an atomic state (B).
  // Expected result: Resultant configuration = {B}.
  @Test
  public void test_1_1() {
    Statechart statechart = this.test_template("test_1_1", "data/constabl/simulator2/t1-1.stbl");
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
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }


  // CODE SIMULATION AND INTERPRETATION - END    //////////////////////////////
*/
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
