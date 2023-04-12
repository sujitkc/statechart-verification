package testcases.simulator2;
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
import java.io.*;
import simulator2.*;
import simulator2.cfg.*;
import simulator2.tree.*;
import simulator2.simulator.*;

public class TestFails {

  //@Test
  public void testAll_1_source_atomic(){
    try{
      /* Following input should result in output {R1A,R2A} -- FAILS */
      //String inputfile="data/constabl_actions/1_source_atomic/t1_5#1.stbl";
      
      /* Following input should result in output {R1A1,R2A} -- FAILS */
      //String inputfile="data/constabl_actions/1_source_atomic/t1_5#2.stbl";
      
      /* Following input should result in output {ShR1A,ShR2A, R2A} -- FAILS */
      //String inputfile="data/constabl_actions/1_source_atomic/t1_5#3.stbl";
      
      /* Following input should result in output {R1A, R2A} -- FAILS */
      //String inputfile="data/constabl_actions/1_source_atomic/t1_6#1.stbl";
      
      /* Following input should result in output {R1A1,R2A} -- FAILS */
      //String inputfile="data/constabl_actions/1_source_atomic/t1_6#2.stbl";
      
      /* Following input should result in output {ShR1A,ShR2A, R2A} -- FAILS */
     String inputfile="data/constabl_actions/1_source_atomic/t1_6#3.stbl";
      
      
      String[] listofActiveAtomicStates={"A"};
      if(!inputfile.contains("*.stbl")){
        runTest("test", inputfile, listofActiveAtomicStates);
      }
      else{
        try{
          String dirpath=inputfile.replace("*.stbl","");
              File directoryPath = new File(dirpath);
          //List of all files and directories
          String contents[] = directoryPath.list();
          System.out.println("List of files and directories in the specified directory:");
          for(int i=0; i<contents.length; i++) {
          System.out.println(dirpath+contents[i]);
          runTest("test_"+dirpath+contents[i], dirpath+contents[i], listofActiveAtomicStates);
          }
        }
        catch(Exception e){
          System.out.println("Folder path exception");
          e.printStackTrace();
          System.exit(1);
        }
      }
    
    }
    catch(Exception e){
      System.out.println("Something went wrong! "+e);
      e.printStackTrace();
    }
  }
  @Test
public void testAll_6_source_shell(){
  try{
    /* Result should be {R1A,R2A} */
    String inputfile="data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_6#1.stbl";
    String[] listofActiveAtomicStates={"ShR1A", "ShR2A"};

    /* Result should be {ShR1A,ShR2A,R2A} */
    //String inputfile="data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_6#3.stbl";
    //String[] listofActiveAtomicStates={"ShR1Aa", "ShR2A"};
    
    //Test concurrent transitions - pass for atomic.
  //  String inputfile="data/constabl_actions/7_concurrent_transitions/1_1.stbl";
   // String[] listofActiveAtomicStates={"R1A", "R2A"};

    if(!inputfile.contains("*.stbl")){
      runTest("test", inputfile, listofActiveAtomicStates);
    }
    else{
      try{
        String dirpath=inputfile.replace("*.stbl","");
            File directoryPath = new File(dirpath);
        //List of all files and directories
        String contents[] = directoryPath.list();
        System.out.println("List of files and directories in the specified directory:");
        for(int i=0; i<contents.length; i++) {
        System.out.println(dirpath+contents[i]);
        runTest("test_"+dirpath+contents[i], dirpath+contents[i], listofActiveAtomicStates);
        }
      }
      catch(Exception e){
        System.out.println("Folder path exception");
        e.printStackTrace();
        System.exit(1);
      }
    }
  
  }
  catch(Exception e){
    System.out.println("Something went wrong! "+e);
    e.printStackTrace();
  }
}

  public void runTest(String testname, String filename, String[] statename) {
    Statechart statechart = this.test_template(testname, filename);

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
      simulator.simulationStep("e1");
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      e.printStackTrace();
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

