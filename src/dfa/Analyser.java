import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;

import java_cup.runtime.Symbol;

import ast.State;
import ast.Statechart;
import ast.Environment;
import ast.Declaration;

import flt.Flatten;

public class Analyser {

  private final Statechart statechart;
  private final Typechecker typechecker;

  public static void main(String[] args) {
    Statechart statechart = null;
    try {
      Parser parser = new Parser(new Lexer(new FileReader(args[0])));
      Symbol result = parser.parse();
      statechart = (Statechart)result.value;
      System.out.println("Printing parsed Statechart ...");
      System.out.println(statechart.toString());
      System.out.println("Printing parsed Statechart ... done!");

      Flatten f=new Flatten();
      Statechart flat=f.flatten(statechart);
      System.out.println("Printing flat Statechart ...");
      System.out.println(flat.toString());
      System.out.println("Printing flat Statechart ... done!\n\n");
    }
    catch(FileNotFoundException e) {
      System.out.println("Couldn't open file '" + args[0] + "'");
    }
    catch(Exception e) {
      System.out.println("Couldn't parse '" + args[0] + "' : " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    try {
      (new Analyser(statechart)).analyse();
      System.out.println("Printing analysed Statechart ...");
      System.out.println(statechart);
      System.out.println("Printing analysed Statechart ... done!");
    }
    catch(Exception e) {
      System.out.println("Couldn't analyse '" + args[0] + "' : " + e.getMessage());
      e.printStackTrace();
    }
  }

  public Analyser(Statechart statechart) {
    this.statechart = statechart;
    this.typechecker = new Typechecker(statechart);
  }

  public void analyse() throws Exception {
    this.typechecker.typecheck();
  }
}
