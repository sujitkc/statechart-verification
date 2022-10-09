package solver;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import configuration.SymTestConfiguration;
import visitor.ExpressionPreorderToStringVisitor;
import ast.Expression;
import ast.Name;
import ast.Type;

public class YicesSolver{

	Expression mExpression;
	Set<Name> mVariables;

	public YicesSolver(Set<Name> symVars, Expression exp) {
		this.mVariables = symVars;
		this.mExpression = exp;
	}

	public SolverResult solve() throws Exception {
		String yicesInput = YicesSolver.makeYicesInput(this.mVariables,
				this.mExpression);


		FileWriter outFile;

		outFile = new FileWriter("resources/input.ys");
		PrintWriter out = new PrintWriter(outFile);
		out.println(yicesInput);
		out.close();
		String command = SymTestConfiguration.YICES_PATH + " resources/input.ys";
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
	private static String makeYicesInput(Set<Name> mVariables2,
			Expression expression) throws Exception {
		

		ExpressionPreorderToStringVisitor preVisitor = new ExpressionPreorderToStringVisitor();

		preVisitor.visit(expression);
		String formula = preVisitor.getValue();

		String s = "";
		for (Name v : mVariables2) {
			//s = s + "(define " + v.getName() + "::"
			//		+ YicesSolver.getVariableTypeString(v) + ")" + "\n";
			s = s + "(define " + v + "::"
					+ YicesSolver.getVariableTypeString(v) + ")" + "\n";
		}
		s = s + "(assert " + formula + ")\n";
		s = s + "(check)\n";
		s = s + "(show-model)";
		return s;

	}

	private static String getVariableTypeString(Name v) throws Exception {
		return "int";
		/*String type = v.getType();
		if (type.equals(Type.BOOLEAN)) {
			return "bool";
		} else if (type.equals(Type.INT)) {
			return "int";
		} else {
			Exception e = new Exception(
					"YicesSolver.getVariableTypeString : type of variable '"
							+ v.getName() + "' not handled.");
			throw e;
		}*/
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
		Map<Name, Object> map = new HashMap<Name, Object>();
		if (tokens.get(0).equals("sat")) {
			isSat = true;

			for (int i = 2; i < tokens.size(); i = i + 3) {
				String varName = tokens.get(i);
				Name var = this.getVariableByName(varName);
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

	private static Object parseVariableValue(Name var, String value)
			throws Exception {
				return 1;
		/*if (var.getType().equals(Type.INT)) {
			return Integer.parseInt(value);
		} else if (var.getType().equals(Type.BOOLEAN)) {
			return Boolean.parseBoolean(value);
		} else {
			Exception e = new Exception(
					"YicesSolver.parseVariableValue : type of variable '"
							+ var.getName() + "' not handled.");
			throw e;
		}*/
	}

	private Name getVariableByName(String name) {
		for (Name v : this.mVariables) {
			if (v.equals(name)) {
				return v;
			}
		}
		return null;
	}
}
