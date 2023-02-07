package Solver;

import expression.ExpressionPreorderToStringVisitor;
import expression.IExpression;
import expression.IIdentifier;
import expression.Type;

import java.io.*;
import java.util.*;

public class YicesSolver implements ISolver {

  IExpression mExpression;
  Set<IIdentifier> mVariables;

  public YicesSolver(Set<IIdentifier> symVars, IExpression exp) {
    this.mVariables = symVars;
    this.mExpression = exp;
  }

  public SolverResult solve() throws Exception {
    String yicesInput = YicesSolver.makeYicesInput(this.mVariables,
        this.mExpression);

    // System.out.println("/home/sujit/My-Download/yices-2.2.2/bin/yices input :\n" + yicesInput);

    FileWriter outFile;

    outFile = new FileWriter("resources/input.ys");
    PrintWriter out = new PrintWriter(outFile);
    out.println(yicesInput);
    out.close();
    String command = "/home/sujit/My-Downloads/yices-2.2.2/bin/yices resources/input.ys";
    String output = YicesSolver.cmdExec(command);
    // System.out.println("yices output :\n" + output);

    System.out.println("Yices output = " + output);
    SolverResult result = this.parseYicesOutput(output);
    System.out.print("solver result = " + result.toString());
    return result;

  }

  /**
   * Uses Yices 2 Solver
   * @param mVariables2
   * @param expression
   * @return
   * @throws Exception
   */
  private static String makeYicesInput(Set<IIdentifier> mVariables2,
      IExpression expression) throws Exception {
    

    ExpressionPreorderToStringVisitor preVisitor = new ExpressionPreorderToStringVisitor();

    preVisitor.visit(expression);
    String formula = preVisitor.getValue();

    String s = "";
    for (IIdentifier v : mVariables2) {
      s = s + "(define " + v.getName() + "::"
          + YicesSolver.getVariableTypeString(v) + ")" + "\n";
    }
    s = s + "(assert " + formula + ")\n";
    s = s + "(check)\n";
    s = s + "(show-model)";
    return s;

  }

  private static String getVariableTypeString(IIdentifier v) throws Exception {
    String type = v.getType();
    if (type.equals(Type.BOOLEAN)) {
      return "bool";
    } else if (type.equals(Type.INT)) {
      return "int";
    } else {
      Exception e = new Exception(
          "YicesSolver.getVariableTypeString : type of variable '"
              + v.getName() + "' not handled.");
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

  private SolverResult parseYicesOutput(String output) throws Exception {
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
        IIdentifier var = this.getVariableByName(varName);
        if (var == null) {
          Exception e = new Exception(
              "YicesSolver.parseYicesOutput : variable '"
                	+ varName + "' not found.");
          throw e;
        }
        Object value = YicesSolver.parseVariableValue(var,
            tokens.get(i + 1));
        map.put(var, value);
      }
    }
    return new SolverResult(isSat, map);
  }

  private static Object parseVariableValue(IIdentifier var, String value)
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

  private IIdentifier getVariableByName(String name) {
    for (IIdentifier v : this.mVariables) {
      if (v.getName().equals(name)) {
        return v;
      }
    }
    return null;
  }
}
