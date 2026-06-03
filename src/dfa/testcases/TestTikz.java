package testcases;

import java.io.FileNotFoundException;
import java_cup.runtime.Symbol;
import transpiler.TikzGenerator;
import org.junit.*;
import ast.*;
import frontend.*;

public class TestTikz {

    @Test
    public void transpile() {

        String input = "data/c5.stb";
        Statechart statechart = null;

        try {

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

        System.out.println("\n=== TIKZ CODE ===\n");
        System.out.println(tikzCode);
    }
}






