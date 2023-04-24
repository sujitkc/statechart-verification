package testcases;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

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
import ast.*;
import constabl.*;
import constabl.simulator.StatechartSimulator;
public class TestConStaBLSimulator {
      String inputfile="";
 Typechecker typechecker;

    Statechart statechart = null;
  public TestConStaBLSimulator(){
    try{
      inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    }catch(Exception e){

    }
    
  }
  @Test
  public void testSimulator() {

    
    System.out.println("Hello front end.");

   
    //String input = "data/curfew_structs_minimal.txt";
     //String input="data/c5.stb";
    if(!inputfile.contains("*.stbl")){
    	runSimulator(inputfile);
    }
    else{
    	try{
    		String dirpath=inputfile.replace("*.stbl","");
    	      File directoryPath = new File("data/"+dirpath);
	      //List of all files and directories
	      String contents[] = directoryPath.list();
	      System.out.println("List of files and directories in the specified directory:");
	      for(int i=0; i<contents.length; i++) {
		 System.out.println(dirpath+contents[i]);
		 runSimulator(dirpath+contents[i]);
	      }
    	}
    	catch(Exception e){
    		System.out.println("Folder path exception");
    		e.printStackTrace();
    		System.exit(1);
    	}
    }
    
  }
  public void runSimulator(String inputfile){
  
  String input="data/"+inputfile;
    System.out.println("Reading file : "+ input);
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
      //new ConStaBLSimulator(statechart);
      //new StatechartSimulator(statechart);
      new StatechartSimulator(statechart);
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n"+e);
      e.printStackTrace();
     
      System.exit(1);
    }
 } 
}
