import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java_cup.runtime.Symbol;

public class Analyser {

  public static void main(String[] args) {
    try {
      Parser parser = new Parser(new Lexer(new FileReader(args[0])));    
      Symbol result = parser.parse();
      ast.Statechart statechart = (ast.Statechart)result.value;
      System.out.println("Printing parsed Statechart ...");
      System.out.println(statechart.toString());
      System.out.println("Printing parsed Statechart ... done!");
      Analyser.analyse(statechart);
      System.out.println("Printing analysed Statechart ...");
      System.out.println(statechart);
      System.out.println("Printing analysed Statechart ... done!");
    }
    catch(FileNotFoundException e) {
      System.out.println("Couldn't open file '" + args[0] + "'"); 
    }
    catch(Exception e) {
      System.out.println("Couldn't parse '" + args[0] + "' : " + e.getMessage()); 
    }
  }

  public static void analyse(ast.Statechart statechart) {

  }
}
