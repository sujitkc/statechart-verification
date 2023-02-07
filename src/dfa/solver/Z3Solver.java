package Solver;

import ast.ExpressionPreorderToStringVisitor;
import ast.Expression;
import ast.Name;
import ast.Type;
import set.SETExpressionVisitor;
import ast.IntegerConstant;
import ast.BooleanConstant;
import java.io.*;
import java.util.*;


public class Z3Solver implements ISolver {

    Expression mExpression;
    //  Expression mExpression;
  Set<Name> mVariables;

  public Z3Solver(Set<Name> symVars, Expression exp) {
    this.mVariables = symVars;
    this.mExpression = exp;
  }

  public SolverResult solve() throws Exception {
    String z3Input = Z3Solver.makeZ3Input(this.mVariables, this.mExpression);

     System.out.println("z3 input :\n" + z3Input);

    FileWriter outFile;

    outFile = new FileWriter("resources/input.smt2");
    PrintWriter out = new PrintWriter(outFile);
    out.println(z3Input);
    out.close();
    String command = "z3 resources/input.smt2";
    String output = Z3Solver.cmdExec(command);
     System.out.println("z3 output :\n" + output);

    SolverResult result = this.parseZ3Output(output);
    System.out.print("solver result = " + result.toString());
    return result;

  }

  /**
   * Uses Z3 Solver
   * @param symVars
   * @param expression
     * @return
   * @throws Exception
   */
  private static String makeZ3Input(Set<Name> symVars, Expression expression) throws Exception {
    

//    SETExpressionVisitor preVisitor = new SETExpressionVisitor();
        ExpressionPreorderToStringVisitor preVisitor = new ExpressionPreorderToStringVisitor();

    preVisitor.visit(expression);
    String formula = preVisitor.getValue();

//        String formula = preVisitor.getValue().toString();
    String s = "";
    for (Name v : symVars) {
      s = s + "(declare-fun " + v.getName() + " () "
          + Z3Solver.getVariableTypeString(v) + ")" + "\n";
    }
    s = s + "(assert " + formula + ")\n";
    s = s + "(check-sat)\n";
    s = s + "(get-model)\n";
    s = s + "(exit)";
    return s;

  }

  private static String getVariableTypeString(Name var) throws Exception {
    /*String type = var.getType();
    if (type.equals(Type.BOOLEAN)) {
      return "Bool";
    } else if (type.equals(Type.INT)) {
      return "Int";
    } else {
      Exception e = new Exception(
          "Z3Solver.getVariableTypeString : type of variable '"
              + var.getName() + "' not handled.");
      throw e;
    }*/
    return var.getDeclaration().getType().toString();
  }

  private static String cmdExec(String cmdLine) throws IOException {
    String line;
    String output = "";

    Process p = Runtime.getRuntime().exec(cmdLine);
    BufferedReader input = new BufferedReader(new InputStreamReader(
        p.getInputStream()));
    line = input.readLine();
    while (line != null) {
      output += (line + '\n');
      line = input.readLine();
    }
    input.close();

    return output;
  }

  private SolverResult parseZ3Output(String output) throws Exception {
    StringTokenizer tokeniser = new StringTokenizer(output, " )\n", false);
    List<String> tokens = new ArrayList<String>();

    while (tokeniser.hasMoreTokens()) {
      tokens.add(tokeniser.nextToken());
    }
    Boolean isSat = false;
  /*  int m = 0;
      System.out.print("tokens = "); for(String t : tokens) {
      System.out.print(m + t + " "); m++;}
  */ 
    Map<Name, Object> map = new HashMap<Name, Object>();
    if (tokens.get(0).equals("sat")) {
      isSat = true;

      for (int i = 3; i < tokens.size(); i = i + 5) {
        String varName = tokens.get(i);
        Name var = this.getVariableByName(varName);
        if (var == null) {
          Exception e = new Exception(
              "Z3Solver.parseZ3Output : variable '"
                	+ varName + "' not found.");
          throw e;
        }
        Object value = Z3Solver.parseVariableValue(var,
            tokens.get(i + 3));
        map.put(var, value);
      }
    }
    return new SolverResult(isSat, map);
  }

  private static Object parseVariableValue(Name var, String value)
      throws Exception {
    if (var.getDeclaration().getType()==(new IntegerConstant(0)).getType()) {
      return Integer.parseInt(value);
    } else if (var.getDeclaration().getType()==(new BooleanConstant(true)).getType()) {
      return Boolean.parseBoolean(value);
    } else {
      Exception e = new Exception(
          "Z3Solver.parseVariableValue : type of variable '"
              + var.getName() + "' not handled.");
      throw e;
    }
  }

  private Name getVariableByName(String name) {
    for (Name v : this.mVariables) {
      if (v.getName().equals(name)) {
        return v;
      }
    }
    return null;
  }
}

