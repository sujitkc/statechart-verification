package testcases.simulator2;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.api.FuzzerSecurityIssueMedium;

import java.security.SecureRandom;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java_cup.runtime.Symbol;
 import static org.junit.Assert.*;
import org.junit.*;
import frontend.FrontEnd;
import frontend.Parser;
import frontend.Typechecker;

import ast.*;
import java.io.*;
import simulator2.*;
import simulator2.cfg.*;
import simulator2.tree.*;
import simulator2.simulator.*;
public class TestIssue {
  static List<TestCase> testcaselist=new ArrayList<TestCase>();
  
  static List<String> filenameList=new ArrayList<String>();
  static List<String[]> sourceList=new ArrayList<String[]>();
  static List<String[]> destList=new ArrayList<String[]>();
  static int i=0;
  static int noofevents=20000;

  @Test
  public void populate(){
  		makeTestCase("data/problem.stb",new String[]{},new String[]{});
		     
  	 }
  public static void makeTestCase(String filename, String[] sourceConfig, String[] destConfig){
  	TestCase tc=new TestCase(filename,sourceConfig, destConfig);
  	testcaselist.add(tc);
  	filenameList.add(filename);
  	sourceList.add(sourceConfig);
  	destList.add(destConfig);
        String events[]={"e1","e1","e1"};
        runSimulateTest("one",filename,events);    
  }

public static Set<State> runSimulateTest(String testname, String filename, String[] events) {
    
    Statechart statechart = test_template(testname, filename);

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart);
      
      Set<State> newConfiguration = simulator.simulate(Arrays.asList(events));
     return newConfiguration;
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
 
  public static Set<State> runTestSimulationStep(String testname, String filename, String[] statename) {
    Statechart statechart = test_template(testname, filename);

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      for(int i=0;i<statename.length;i++){
        State s1 = simulator.getSubstateByName(statename[i], statechart);
        configuration.add(s1);
        System.out.println(s1.name);
      }
        // State s2 = simulator.getSubstateByName("B2B", statechart);
      
      //configuration.add(s2);
     
      //System.out.println(s2.name);
     Set<State> newConfiguration = simulator.simulationStep("e1");
     return newConfiguration;
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
  public static Statechart test_template(String testName, String inputFileName) {
    System.out.println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>>>>>>>>>>>>>>>>>>>>>\n\n"+testName+"\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

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

