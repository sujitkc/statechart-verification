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

import frontend.FrontEnd;
import frontend.Parser;
import frontend.Typechecker;

import ast.*;
import java.io.*;
import simulator2.*;
import simulator2.cfg.*;
import simulator2.tree.*;
import simulator2.simulator.*;
public class TestCaseStudyFuzzer {
  static List<TestCase> testcaselist=new ArrayList<TestCase>();
  
  static List<String> filenameList=new ArrayList<String>();
  static List<String[]> sourceList=new ArrayList<String[]>();
  static List<String[]> destList=new ArrayList<String[]>();
  static int i=0;
  static int noofevents=300000;
  public static void populate(){
  		//makeTestCase("data/problem.stb",new String[]{},new String[]{});
  		//makeTestCase("data/input3.stb",new String[]{},new String[]{});

		//casestudy - all together
  		//makeTestCase("data/uwfms/allcomponents.stb", new String[] {}, new String[]{});
		 //casestudy - 1 
  		//makeTestCase("data/uwfms/capa.stb", new String[] {}, new String[]{}); 
		//casestudy - 2 
  	        //makeTestCase("data/uwfms/ccpa.stb", new String[] {}, new String[]{});
		 //casestudy - 3 
  		 // makeTestCase("data/uwfms/lgpa.stb", new String[] {}, new String[]{});
		//casestudy - 4 --not working
  		//  makeTestCase("data/uwfms/evacc.stb", new String[] {}, new String[]{});
		//casestudy - 5 
  		 // makeTestCase("data/uwfms/caeva.stb", new String[] {"caDisabled","HoldSpeed","sense","evaSlow"}, new String[]{"caMitigate","IncSpeed","sense","evaSlow"});
		//casestudy - 6 
  		 // makeTestCase("data/uwfms/paeva.stb", new String[] {}, new String[]{});
		//casestudy - 7 
  		// makeTestCase("data/uwfms/lgeva.stb", new String[] {}, new String[]{});
		//casestudy - 8 
  		//  makeTestCase("data/uwfms/raeva.stb", new String[] {}, new String[]{});
		//casestudy - 9 
  		//  makeTestCase("data/uwfms/raca.stb", new String[] {}, new String[]{});
		//casestudy - 10 
  		//  makeTestCase("data/uwfms/rapa.stb", new String[] {}, new String[]{});
		makeTestCase("data/uwfms/lgsense.stb", new String[] {}, new String[]{});   
  
  	 }
  public static void makeTestCase(String filename, String[] sourceConfig, String[] destConfig){
  	TestCase tc=new TestCase(filename,sourceConfig, destConfig);
  	testcaselist.add(tc);
  	filenameList.add(filename);
  	sourceList.add(sourceConfig);
  	destList.add(destConfig);
  }
  public static void eventFuzz(FuzzedDataProvider data){
	int num;
	for(int i=0;i<testcaselist.size();i++){
  	  String inputfile=testcaselist.get(i).filename;
	  	 String[] destConfig=testcaselist.get(i).destConfig;

  		List<String> events=new ArrayList<String>();
		events.add("e1");
		events.add("e2");
		events.add("e");
		//events.add("e2");
		events.add("No_event");
		events.add("SetAccelIn");
		events.add("ResumeCoastIn");
		events.add("ResumeCoastOut");
		events.add("Cancel");
		events.add("Error");
		events.add("Next");
		events.add("Cancel");
		List<String> list=new ArrayList<String>();
		Random r=new Random();
		for(int j=0;j<noofevents;j++){
			//num=data.consumeInt​(0,2);
			num=r.nextInt(10);
			list.add(events.get(num));

		}
			
	
	  //int eventlength=data.consumeInt​(3,100);
	  //List<String> list=data.pickValues(events,400);
	  System.out.println("events : "+ list);
	  String[] eventsarray = list.toArray(new String[list.size()]);
	  Set<State> newconfig=runSimulateTest("test"+i, inputfile, eventsarray);
  	  String[] output=new String[newconfig.size()];
  	  int j=0;
	  for(State s:newconfig) {
	   		output[j++]=s.name;
	   		}
	  Arrays.sort(output);
	  // System.out.println("Configuration is : "+ output);
	   Arrays.sort(destConfig);
	  //UNCOMMENT THIS FOR UNDESIRABLE CONFIGURATION
	  // System.out.println("******** Testing Assertion ******** comparing ******"+Arrays.toString(destConfig)+"==="+Arrays.toString(output));
         // assertFalse(Arrays.equals(destConfig,output));
  	  
  	  }
	System.exit(0);

  }
  public static void fuzzerInitialize() {
    // Optional initialization to be run before the first call to fuzzerTestOneInput.
  }

  public static void fuzzerTestOneInput(FuzzedDataProvider data) {
try{
	populate();
	eventFuzz(data);
}
catch(Exception e){
	System.out.println("exception : fuzzed data provider");
}
	/*String[] randomSourceState=data.pickValue(sourceList);
	String[] randomDestState=data.pickValue(destList);
	String fname=data.pickValue(filenameList);
	System.out.println(randomSourceState);
	System.out.println(randomDestState);
	System.out.println(fname);
	System.out.println();
	
	
	 Set<State> newconfig=runTest("test"+(i++), fname, randomSourceState);
  	  String[] output=new String[newconfig.size()];
      
          int j=0;
	  for(State s:newconfig) {
	   		output[j++]=s.name;
	   		}
	  Arrays.sort(output);
	  Arrays.sort(randomDestState);
	  System.out.println("******** Testing Assertion ******** comparing ******"+Arrays.toString(randomDestState)+"==="+Arrays.toString(output));
          assertArrayEquals(randomDestState,output);	
	*/
	/*for(int i=0;i<testcaselist.size();i++){
  	  String inputfile=testcaselist.get(i).filename;
  		List<String> events=new ArrayList<String>();
	events.add("e1");
	events.add("e2");
	/*events.add("e3");
	events.add("e4");
	events.add("e5");
	events.add("e6");
	int eventlength=data.consumeInt​(3,10);
	List<String> list=data.pickValues(events,eventlength);
	System.out.println(list);
	String[] eventsarray = list.toArray(new String[list.size()]);
	Set<State> newconfig=runSimulateTest("test"+i, inputfile, eventsarray);
  	  String[] output=new String[newconfig.size()];
  	  int j=0;
	  for(State s:newconfig) {
	   		output[j++]=s.name;
	   		}
	  Arrays.sort(output);
	  System.out.println("Configuration is : "+ output);
	  
  	  
  	  }
	  //Arrays.sort(expected);
	  //System.out.println("******** Testing Assertion ******** comparing ****** (expected)"+Arrays.toString(expected)+"=== (actual)"+Arrays.toString(output));
	
	
    /*String input = data.consumeRemainingAsString();
    // Without the hook in ExampleFuzzerHooks.java, the value of random would change on every
    // invocation, making it almost impossible to guess for the fuzzer.
    long random = new SecureRandom().nextLong();
    if (input.startsWith("magicstring" + random) && input.length() > 30
        && input.charAt(25) == 'C') {
      mustNeverBeCalled();
    }*/
  }
 /* public void callTestCases(){
  try{
  	
  	System.out.println("Test begins : Number of testcases : "+ testcaselist.size());
  	for(int i=0;i<testcaselist.size();i++){
  	  String inputfile=testcaselist.get(i).filename;
  	  String[] listofActiveAtomicStates= testcaselist.get(i).sourceConfig;
  	  String[] expected = testcaselist.get(i).destConfig;
  	  Set<State> newconfig=runTest("test"+i, inputfile, listofActiveAtomicStates);
  	  String[] output=new String[newconfig.size()];
      
          int j=0;
	  for(State s:newconfig) {
	   		output[j++]=s.name;
	   		}
	  Arrays.sort(output);
	  Arrays.sort(expected);
	  System.out.println("******** Testing Assertion ******** comparing ******"+Arrays.toString(expected)+"==="+Arrays.toString(output));
          assertArrayEquals(expected,output);		
  		}
  	}
 	 catch(Exception e){
  		
          e.printStackTrace();
          System.exit(1);
  	}
  }*/
 
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
  private static void mustNeverBeCalled() {
    throw new FuzzerSecurityIssueMedium("mustNeverBeCalled has been called");
  }
}

