import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;

import java_cup.runtime.Symbol;

import ast.State;
import ast.Environment;
import ast.Declaration;

public class Analyser {

  public static void main(String[] args) {
    try {
      Parser parser = new Parser(new Lexer(new FileReader(args[0])));    
      Symbol result = parser.parse();
      ast.Statechart statechart = (ast.Statechart)result.value;
      System.out.println("Printing parsed Statechart ...");
      System.out.println(statechart.toString());
      System.out.println("Printing parsed Statechart ... done!");
      (new Analyser()).analyse(statechart);
      System.out.println("Printing analysed Statechart ...");
      System.out.println(statechart);
      System.out.println("Printing analysed Statechart ... done!");
    }
    catch(FileNotFoundException e) {
      System.out.println("Couldn't open file '" + args[0] + "'"); 
    }
    catch(Exception e) {
      System.out.println("Couldn't parse '" + args[0] + "' : " + e.getMessage()); 
      e.printStackTrace();
    }
  }

  /*
    Constructs an environment consisting of all declarations of the lower State
    all the way upto the upper State. upper has to be an ancestor state of lower.
  */
  private Environment getEnvironmentExcluded(State lower, State upper) throws Exception {
    if(!upper.isAncestor(lower)) {
      throw new Exception("Analyser.getEnvironmentExcluded : upper should be an ancestor of lower.");
    }
    if(upper != lower.getSuperstate()) {
      return new Environment(lower.declarations, getEnvironmentExcluded(lower.getSuperstate(), upper));
    }
    else {
      return new Environment(lower.declarations, null);
    }
  }

  public void analyse(ast.Statechart statechart) {
  }
}
