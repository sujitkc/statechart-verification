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
public class TestActions {
      String inputfile="";
 Typechecker typechecker;

    Statechart statechart = null;
    //@Test
    public void testAll_1_source_atomic(){
      try{
        //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
        //inputfile="data/constabl_actions/1_source_atomic/t1_1.stbl";    //PASS
        //inputfile="data/constabl_actions/1_source_atomic/t1_2#1.stbl";  //PASS
        // inputfile="data/constabl_actions/1_source_atomic/t1_2#2.stbl"; //PASS
        // inputfile="data/constabl_actions/1_source_atomic/t1_2#3.stbl";   //PASS
        // inputfile="data/constabl_actions/1_source_atomic/t1_3#1.stbl";     //PASS
        // inputfile="data/constabl_actions/1_source_atomic/t1_3#2.stbl";     //PASS
        // inputfile="data/constabl_actions/1_source_atomic/t1_3#3.stbl";   //PASS
        // inputfile="data/constabl_actions/1_source_atomic/t1_4.stbl";     //PASS
        // inputfile="data/constabl_actions/1_source_atomic/t1_5#1.stbl";     //FAIL
        // inputfile="data/constabl_actions/1_source_atomic/t1_5#2.stbl";     //FAIL
        // inputfile="data/constabl_actions/1_source_atomic/t1_5#3.stbl";     //partial PASS - R1's parent is a shell - which is not considered to create a ConcurrentCode
        // inputfile="data/constabl_actions/1_source_atomic/t1_6#1.stbl";     //FAIL
        // inputfile="data/constabl_actions/1_source_atomic/t1_6#2.stbl";     //FAIL
        // inputfile="data/constabl_actions/1_source_atomic/t1_6#3.stbl";      //Fails
         /*
          * data/constabl_actions/1_source_atomic/t1_6#3.stbl
          Fails when destination is chart.B - it never ends in a state where x value can be 50 or 54
          Fails when destination is chart.B.R1 - it does not create proper final configuration as well as the x value has problem
          Fails when destination is chart.B.R2 - it does not create proper final configuration as well as the x value has problem
          Fails when destination is chart.B.R1 - it does not create proper final configuration as well as the x value has problem
          Fails when destination is chart.B.R1.R1A - it does not create proper final configuration as well as the x value has problem
          Fails when destination is chart.B.R2.R2B - it does not create proper final configuration as well as the x value has problem
          Fails when destination is chart.B.R1.R1A.Sh - it does not create proper final configuration as well as the x value has problem
          Fails when destination is chart.B.R1.R1A.Sh - it does not create proper final configuration as well as the x value has problem
          Fails when destination is chart.B.R1.R1A.Sh.R1 - it does not create proper final configuration as well as the x value has problem

          */
        
        String[] listofActiveAtomicStates={"A"};
            
        if(!inputfile.contains("*.stbl")){
          runTest("test", inputfile,listofActiveAtomicStates); 
        }
        else{
          try{
            String dirpath=inputfile.replace("*.stbl","");
            File directoryPath = new File(dirpath);
            //List of all files and directories
            String contents[] = directoryPath.list();
            //System.out.println("List of files and directories in the specified directory:");
            for(int i=0; i<contents.length; i++) {
            System.out.println(dirpath+contents[i]);
            runTest("test_"+dirpath+contents[i], dirpath+contents[i],listofActiveAtomicStates);
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
    
    //@Test
    public void testAll_2_source_composite_activeAtomicSubState(){
      try{
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_1.stbl"; // PASS
        // inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_2#1.stbl"; //PASS
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_2#2.stbl"; //PASS
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_2#3.stbl"; //PASS
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_3#1.stbl"; //PASS
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_3#2.stbl"; //PASS
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_3#3.stbl"; //PASS
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_4.stbl"; //PASS
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_5#1.stbl"; //FAIL
        //inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_5#2.stbl"; //FAIL
       // inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_5#3.stbl"; //FAIL
      //  inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_6#1.stbl"; //FAIL
      //  inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_6#2.stbl"; //FAIL
      // inputfile="data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_6#3.stbl"; //FAIL
        
        String[] listofActiveAtomicStates={"A1"};

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
   // @Test
    public void testAll_2_source_composite_activeCompositeSubstate(){
      try{
        //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
        //inputfile="data/constabl_transitions/2#2_source_composite(activecompositesubstate)/*.stbl";
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_1.stbl"; //PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_2#1.stbl";//PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_2#2.stbl"; //PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_2#3.stbl"; //PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_3#1.stbl"; //PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_3#2.stbl"; //PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_3#3.stbl"; //PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_4.stbl"; //PASS
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_5#1.stbl"; //FAIL
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_5#2.stbl"; //FAIL
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_5#3.stbl"; //FAIL
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_6#1.stbl"; //FAIL
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_6#2.stbl"; //FAIL
        //inputfile="data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_6#3.stbl"; //FAIL
        
        
        String[] listofActiveAtomicStates={"A11"};

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
    
    //@Test
    public void testAll_2_source_composite_activeShellSubstate(){
      try{
       // inputfile="data/constabl_transitions/2#3(4)source_composite(activeshellsubstate)/*.stbl";
       inputfile="data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_1.stbl"; //FAIL
       
       
       String[] listofActiveAtomicStates={"ShR1A","ShR1A"};

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
  //@Test
  public void testAll_3_source_substate_of_composite_activeAtomicSubState(){
    try{
      //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
      inputfile="data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/*.stbl";
      String[] listofActiveAtomicStates={"A1"};

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
  //@Test
  public void testAll_3_source_substate_of_composite_activeCompositeSubstate(){
    try{
      //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
      inputfile="data/constabl_transitions/3#2_source_substateofcomposite(activecompositesubstate)/*.stbl";
      String[] listofActiveAtomicStates={"A11"};

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
  
  //@Test
  public void testAll_3_source_substate_of_composite_activeShellSubstate(){
    try{
      //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
      inputfile="data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_1.stbl";
      String[] listofActiveAtomicStates={"ShR1A","ShR1A"};

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

//@Test
public void testAll_5_source_region_activeAtomicSubState(){
  try{
    //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    inputfile="data/constabl_transitions/5#1_source_region(activeatomicsubstate)/*.stbl";
    String[] listofActiveAtomicStates={"ShR1A", "ShR2A"};

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
//@Test
public void testAll_5_source_region_activeCompositeSubstate(){
  try{
    //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    inputfile="data/constabl_transitions/5#2_source_region(compositesubstate)/*.stbl";
    String[] listofActiveAtomicStates={"ShR1A", "ShR2A"};

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

//@Test
public void testAll_5_source_region_activeShellSubstate(){
  try{
    //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    inputfile="data/constabl_transitions/5#3_source_region(shellsubstate)/*.stbl";
    String[] listofActiveAtomicStates={"ShR1A","ShR1A"};

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

// tests for the files under folder #6

//@Test
public void testAll_6_source_substate_of_region_activeAtomicSubState(){
  try{
    //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    inputfile="data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/*.stbl";
    String[] listofActiveAtomicStates={"ShR1A", "ShR2A"};

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
//@Test
public void testAll_6_source_substate_of_region_activeCompositeSubstate(){
  try{
    //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    inputfile="data/constabl_transitions/6#2_source_substateofregion(compositesubstate)/*.stbl";
    String[] listofActiveAtomicStates={"ShR1A", "ShR2A"};

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

//@Test
public void testAll_6_source_substate_of_region_activeShellSubstate(){
  try{
    //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    inputfile="data/constabl_transitions/6#3_source_region(shellsubstate)/*.stbl";
    String[] listofActiveAtomicStates={"ShR1A","ShR1A"};

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
public void testAll_7_concurrency(){
  try{
    //inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    inputfile="data/constabl_actions/7_concurrent_transitions/1_1.stbl";
    String[] listofActiveAtomicStates={"R1A","R2A"};

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
        System.out.println("Something Went Wrong!\n"+e);
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
  public TestActions(){
   /* try{
      inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    }catch(Exception e){

    }
     */
  }
//   //@Test
//   public void testSimulator() {

    
//     System.out.println("Hello front end.");

   
//     //String input = "data/curfew_structs_minimal.txt";
//      //String input="data/c5.stb";
//     if(!inputfile.contains("*.stbl")){
//     	runSimulator(inputfile);
//     }
//     else{
//     	try{
//     		String dirpath=inputfile.replace("*.stbl","");
//     	      File directoryPath = new File("data/"+dirpath);
// 	      //List of all files and directories
// 	      String contents[] = directoryPath.list();
// 	      System.out.println("List of files and directories in the specified directory:");
// 	      for(int i=0; i<contents.length; i++) {
// 		 System.out.println(dirpath+contents[i]);
// 		 runSimulator(dirpath+contents[i]);
// 	      }
//     	}
//     	catch(Exception e){
//     		System.out.println("Folder path exception");
//     		e.printStackTrace();
//     		System.exit(1);
//     	}
//     }
    
//   }
//   public void runSimulator(String inputfile){
  
//   String input="data/"+inputfile;
//     System.out.println("Reading file : "+ input);
//     // String input = "data/s24.txt";
//     // String input = "data/curfew1.txt";
//     try {
//       Parser parser = new FrontEnd(input).getParser();    
//       Symbol result = parser.parse();
//       statechart = (Statechart)result.value;
//       System.out.println("Printing parsed Statechart ...");
//       System.out.println(statechart.toString());
//       System.out.println("Printing parsed Statechart ... done!");
//     }
//     catch(FileNotFoundException e) {
//       System.out.println("Couldn't open file '" + input + "'"); 
//     }
//     catch(Exception e) {
//       System.out.println("Couldn't parse '" + input + "' : " + e.getMessage()); 
//       e.printStackTrace();
//       System.exit(1);
//     }
//     try {
//       new Typechecker(statechart).typecheck();
//       System.out.println(input + ": Printing typechecked Statechart ...");
//       System.out.println(statechart.toString());
//       System.out.println(input + ": Printing typechecked Statechart ... done!");
//     }
//     catch(Exception e) {
//       System.out.println("Couldn't typecheck '" + input + "' : " + e.getMessage()); 
//       e.printStackTrace();
//     }

//       // creating a simulator
//     try
//     {
//       //new ConStaBLSimulator(statechart);
//       //new StatechartSimulator(statechart);
//       new StatechartSimulator(statechart);
//     }
//     catch(Exception e) {
//       System.out.println("Something Went Wrong!\n");
//       e.printStackTrace();
     
//       System.exit(1);
//     }
//  } 
}
