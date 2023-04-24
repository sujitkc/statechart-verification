package Solver;

import expression.*;

import java.io.*;
import java.util.*;

public class DRealSolver implements ISolver {

  IExpression mExpression;
  Set<Variable> mVariables;

  public DRealSolver(Set<Variable> symVars, IExpression exp) {
    this.mVariables = symVars;
    this.mExpression = exp;
  }

  public SolverResult solve() throws Exception {
    String drealInput = DRealSolver.makeDRealInput(this.mVariables,
        this.mExpression);

     System.out.println("yices input :\n" + drealInput);

    FileWriter outFile;

    outFile = new FileWriter("resources/input.smt2");
    PrintWriter out = new PrintWriter(outFile);
    out.println(drealInput);
    out.close();
    String command = "dReal resources/input.smt2";
    String output = DRealSolver.cmdExec(command);
     System.out.println("yices output :\n" + output);

    SolverResult result = this.parseDRealOutput(output);
    System.out.print("solver result = " + result.toString());
    return result;

  }

  /**
   * Uses Yices 2 Solver
   * @param symVars
   * @param expression
   * @return
   * @throws Exception
   */
  private static String makeDRealInput(Set<Variable> symVars,
      IExpression expression) throws Exception {
    

    ExpressionPreorderToStringVisitor preVisitor = new ExpressionPreorderToStringVisitor();

    preVisitor.visit(expression);
    String formula = preVisitor.getValue();

    String s = "(set-logic QF_NRA)\n";
    for (Variable v : symVars) {
      s = s + "(declare-fun " + v.getName() + " () "
          + DRealSolver.getVariableTypeString(v) + ")" + "\n";
    }
    s = s + "(assert " + formula + ")\n";
    s = s + "(check-sat)\n";
    s = s + "(exit)";
    return s;

  }

  private static String getVariableTypeString(Variable var) throws Exception {
    String type = var.getType();
    if (type.equals(Type.BOOLEAN)) {
      return "bool";
    } else if (type.equals(Type.INT)) {
      return "int";
    } else {
      Exception e = new Exception(
          "DRealSolver.getVariableTypeString : type of variable '"
              + var.getName() + "' not handled.");
      throw e;
    }
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

  private SolverResult parseDRealOutput(String output) throws Exception {
    StringTokenizer tokeniser = new StringTokenizer(output, " )\n", false);
    List<String> tokens = new ArrayList<String>();

    while (tokeniser.hasMoreTokens()) {
      tokens.add(tokeniser.nextToken());
    }
    Boolean isSat = false;
    /*
     * System.out.print("tokens = "); for(String t : tokens) {
     * System.out.print(t + " "); }
     */
    Map<IIdentifier, Object> map = new HashMap<IIdentifier, Object>();
    if (tokens.get(0).equals("sat")) {
      isSat = true;

      for (int i = 2; i < tokens.size(); i = i + 3) {
        String varName = tokens.get(i);
        Variable var = this.getVariableByName(varName);
        if (var == null) {
          Exception e = new Exception(
              "DRealSolver.parseDrealOutput : variable '"
                	+ varName + "' not found.");
          throw e;
        }
        Object value = DRealSolver.parseVariableValue(var,
            tokens.get(i + 1));
        map.put(var, value);
      }
    }
    return new SolverResult(isSat, map);
  }

  private static Object parseVariableValue(Variable var, String value)
      throws Exception {
    if (var.getType().equals(Type.INT)) {
      return Integer.parseInt(value);
    } else if (var.getType().equals(Type.BOOLEAN)) {
      return Boolean.parseBoolean(value);
    } else {
      Exception e = new Exception(
          "YicesSolver.parseVariableValue : type of variable '"
              + var.getName() + "' not handled.");
      throw e;
    }
  }

  private Variable getVariableByName(String name) {
    for (Variable v : this.mVariables) {
      if (v.getName().equals(name)) {
        return v;
      }
    }
    return null;
  }
}
