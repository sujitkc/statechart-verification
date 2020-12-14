package app;

import ast.*;
import java.util.List;
import java.util.ArrayList;
import visitor.ExpressionFormulaGenerator;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.common.configuration.Configuration; import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;

public class App {
	private Type _intType, _boolType;


	public static void main (String[] args) {
		App app = new App();
	}

	public App() {
		try {
			run();
		} catch (Exception e) {
			System.out.println (e.getMessage());
		}
	}

	private SolverContext _context;
	private ProverEnvironment _prover;

	void run () throws Exception {
		setUpJavaSMT();
		IntegerFormulaManager imgr = _context.getFormulaManager().getIntegerFormulaManager();
		BooleanFormulaManager bmgr = _context.getFormulaManager().getBooleanFormulaManager();

		// Create formula "a = b" with two integer variables
		IntegerFormula v0 = imgr.makeNumber(0);
		IntegerFormula v1 = imgr.makeNumber(1);
		IntegerFormula v2 = imgr.makeNumber(2);
		// BooleanFormula f1 = (imgr.equal (v0, v2));
		// f1 = bmgr.not(f1);
		// BooleanFormula f2 = (imgr.equal (v0, v1));
		// f2 = bmgr.not(f2);

		// 
		// BooleanFormula f3 = (imgr.equal (v0, v0));
		// f3 = bmgr.equivalence(f3, bmgr.equivalence(f1, f2));
		
		BooleanFormula f = imgr.equal(v0, v2);

		System.out.println(f);
		_prover.addConstraint(f);
		boolean res = !_prover.isUnsat();
		System.out.println("res="+res);
		try {
			Model model = _prover.getModel();
			// System.out.printf("SAT with a = %s, b = %s", model.evaluate(a), model.evaluate(b));
		} catch (Exception e) {
			System.out.println (e);
		}
	}

	void setUpJavaSMT () throws InvalidConfigurationException {
		String [] args = {};
		Configuration config = Configuration.fromCmdLineArguments(args);
		LogManager logger = BasicLogManager.create(config);
		ShutdownManager shutdown = ShutdownManager.create();
		this._context = SolverContextFactory.createSolverContext(config, logger, shutdown.getNotifier(), Solvers.SMTINTERPOL);
		this._prover = this._context.newProverEnvironment(ProverOptions.GENERATE_MODELS);
	}

	boolean isSat (Expression expr) {
		ExpressionFormulaGenerator fg = new ExpressionFormulaGenerator(this._context);
		try {
			// TODO: Exception check
			_prover.push();
			this._prover.addConstraint(fg.generate(expr));
			// this._prover.pop();
			boolean res = this._prover.isUnsat();
			_prover.pop();
			return !res;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
