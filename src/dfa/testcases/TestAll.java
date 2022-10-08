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

import ast.State;
import ast.Statechart;
import ast.Transition;
import ast.Environment;
import ast.Declaration;
import ast.Name;

import translation.Flattener;
import metric.Metric;

public class TestAll {
  String[] inputs = { 
    "s2",
    "s3",  // error - assignment type mismatch
    "s4",  // error - misplaced transition
    "s5",  // error - unknown state
    "s6",  // error - non boolean guard
    "s7",  // parenthesised expression, Binary expressions (logical, relational, arithmetic), boolean constant, integer constant, statement list
    "s8",  // user defined type (struct) based on earlier user defined type.
    "s9",  // error - type error in while condition
    "s10", // error - type error in if condition
    "s11", // input variables
    "s12", // error - duplicate names
    "s13", // function declaration
    "s14", // error - duplicate names in function declaration
    "s15", // error - function declaration unknown return type
    "s16", // error - function declaration unknown argument type
    "s17", // Function call 
    "s18", // error - Error - Function call: undeclared function 
    "s19", // error - Error - Function call: incorrect number of arguments 
    "s20", // error - Error - Function call: incorrect number of arguments 
    "s21", // error - Error - Function call: argument type mismatch 
    "s22", // error - Error - Function call: argument type mismatch
    "s34", // function call as argument to function call.
    "s35", // error - function call as argument to function call. Incorrect return type  
    "s23", // Name in expression
    "s24", // Name in expression - all correct usages
    "s25", // error -fully qualified name expression but not in scope 
    "s26",
    "s27",
    "s28",
    "s29",
    "s30",
    "s31",
    "s32",
    "s33",
    "s34",
    "s35",
    "s36",
    "s37",
    "s38",
    "s39",
    "s40",
    "s41",
    "s42",
    "s43",
    "s44",
    "s45",
    "s46",
  };

  @Test
  public void testall() {

    Typechecker typechecker;

    Statechart statechart = null;
    for(String input : inputs) {
      try {
        Parser parser = new FrontEnd("data/" + input + ".txt").getParser();    
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
    }
  }
}
