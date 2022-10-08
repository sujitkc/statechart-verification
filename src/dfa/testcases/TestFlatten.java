package testcases;

import java.io.BufferedReader;
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

import ast.State;
import ast.Statechart;
import ast.Transition;
import ast.Environment;
import ast.Declaration;
import ast.Name;

import translation.Flattener;
import metric.Metric;

public class TestFlatten {

  String inputfile="";
  public TestFlatten(){
    

    try{
      inputfile=(new BufferedReader(new FileReader("data/inputfile.txt"))).readLine();
    }catch(Exception e){

    }
    
  }
  @Test
  public void testFlatten() {

    Typechecker typechecker;
    //String input = "data/curfew1.txt";
    String input="data/"+inputfile;
    Statechart statechart = null;
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
    Statechart flattenedSC = null;
    try {
      Flattener flattener = new Flattener();
      flattenedSC = flattener.translate(statechart);
      System.out.println("Printing flattened Statechart ...");
      System.out.println(flattenedSC);
      System.out.println("Printing flattened Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't flatten '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }

  }
}
