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
  public void testSimulator1() {
    System.out.println("Hello Simulator2.");
  }

  @Test
  public void testSimulator2() {
    System.out.println("Hello again Simulator2.");
    List<String> strings = new ArrayList<>();
    strings.add("x");
    Name name = new Name(strings);
    IntegerConstant i = new IntegerConstant(10);
    AssignmentStatement s = new AssignmentStatement(name, i);
    ASTToCFG converter = new ASTToCFG(s);
    try {
      CFG cfg = converter.convert();
      System.out.println(cfg);
    }
    catch(Exception e) {}
  }

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
}
