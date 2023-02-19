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

public class TestSimulator2 {
  @Test
  public void testSimulator_1() {
    System.out.println("Running testSimulator_1 ...");

    Typechecker typechecker;

    Statechart statechart = null;
    String input="data/c10-sim2.stb";
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
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

  @Test
  public void test_1() {
    Statechart statechart = this.test_template("test_1", "data/constabl/simulator2/t1.stbl");
    try
    {
      State A = statechart.findSubstateByName("A");
      System.out.println(A);
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

  @Test
  public void test_2() {
    Statechart statechart = this.test_template("test_2", "data/constabl/simulator2/t2.stbl");
    try
    {
      State A = statechart.findSubstateByName("A");
      System.out.println(A);
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

  @Test
  public void test_3() {
    Statechart statechart = this.test_template("test_3", "data/constabl/simulator2/t3.stbl");

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      State A = simulator.getSubstateByName("AA", statechart);
      System.out.println(A);
      configuration.add(A);
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      System.exit(1);
    }
  }

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
