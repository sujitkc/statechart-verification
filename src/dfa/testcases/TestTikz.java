package testcases;

import java.io.FileNotFoundException;
import java_cup.runtime.Symbol;
import transpiler.TikzGenerator;
import org.junit.*;
import ast.*;
import frontend.*;
import java.io.FileWriter;

public class TestTikz {

    @Test
    public void transpile() {

        for (int i = 1; i <= 17; i++) {

            String input = "data/c" + i + ".stb";
            Statechart statechart = null;

            try {

                System.out.println("At file: " + input);

                Parser parser = new FrontEnd(input).getParser();
                Symbol ast = parser.parse();
                statechart = (Statechart)ast.value;
                new Typechecker(statechart).typecheck();

            } catch (Exception e) {

                e.printStackTrace();
                return;
            }

            TikzGenerator transpiler = new TikzGenerator(statechart);
            String tikzCode = transpiler.generate();

            try {
        
                FileWriter fileWriter = new FileWriter("tikz_outputs/c" + i + ".tex", false);
                fileWriter.write(tikzCode);
                fileWriter.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}






