package see;

import program.Program;
import ast.*;
import set.*;
import java.util.ArrayList;
import visitor.ExpressionFormulaGenerator;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;

public class Engine {
	private Program _program;
	private int _max_depth;
	private SEResult _res;

	public Engine () {}

	public SEResult getSEResult (Program program, int max_depth) throws Exception {
		this._program = program;
		this._max_depth = max_depth;
		DeclarationList decls = new DeclarationList();
		decls.addAll(this._program.declarations);
		decls.addAll(this._program.other_declarations);
		SEResult result = executeDeclarations(decls);
		StatementList stmts = processProgramStatementList(this._program.statements);
		_res = this.executeStatement(stmts, result.getLive());
		return _res;
	}

	StatementList processProgramStatementList (StatementList stmts) {
		StatementList res = new StatementList();
		for (Statement stmt: stmts.getStatements()) {
			if (!(stmt instanceof WhileStatement)) {
				res.add(stmt);
			} else {
				// TODO: This would depend on the depth
				for (int i = 0; i < 10; i++) {
					res.add(((WhileStatement)stmt).body);
				}
				break;
			}
		}
		return res;
	}

	// sym_eval
	Expression executeExpression (Expression expr, SETNode leaf) throws Exception {
		if (expr instanceof BinaryExpression) {
			BinaryExpression bin_expr = (BinaryExpression)expr;
			return new BinaryExpression(executeExpression(bin_expr.left, leaf), 
					executeExpression(bin_expr.right, leaf),
					bin_expr.operator
					);
		} else if (expr instanceof BooleanConstant || expr instanceof IntegerConstant) {
			return expr;
		} else if (expr instanceof Name) {
			Name name = (Name)expr;
			return leaf.getVarValue(name.getDeclaration());
		}

		throw new Exception ("Unexpected expression");
	}

	SEResult executeInstruction (InstructionStatement istmt, SETNode leaf) {
		if (istmt instanceof HaltStatement || istmt instanceof SkipStatement || istmt instanceof ExpressionStatement) {
			return new SEResult();
		}
		AssignmentStatement stmt = (AssignmentStatement) istmt;
		SETNode node = new SETInstructionNode(leaf, stmt.lhs, stmt.rhs);
		SEResult res = new SEResult();
		if (node.getDepth() > _max_depth) {
			res.addToDone (node);
		} else {
			res.addToLive (node);
		}

		return res;
	}

	SEResult executeDecision (IfStatement stmt, SETNode leaf) throws Exception {
		SEResult res = new SEResult();
		Expression then_cond = executeExpression(stmt.condition, leaf);
		if (isSat(then_cond)) {
			SETDecisionNode node = new SETDecisionNode(leaf, then_cond);
			SEResult then_res = executeBlock (new StatementList(stmt.then_body), node);
			res = res.merge (then_res);
		}

		Expression else_cond = new UnaryExpression(stmt.condition, UnaryExpression.Operator.NOT);
		if (isSat (else_cond)) {
			SETDecisionNode node = new SETDecisionNode(leaf, else_cond);
			SEResult else_res = executeBlock ( new StatementList (stmt.else_body), node);
			res = res.merge (else_res);
		}

		if (res.getLive().size() == 0) {
			res.addToDone (leaf);
		}
		return res;
	}

	SEResult executeStatement (Statement stmt, ArrayList<SETNode> leaves) throws Exception {
		SEResult res = new SEResult();

		for (SETNode leaf: leaves) {
			if (stmt instanceof InstructionStatement) {
				res = res.merge (executeInstruction((InstructionStatement)stmt, leaf));
			} else if (stmt instanceof IfStatement) {
				res = res.merge (executeDecision((IfStatement)stmt, leaf));
			} else if (stmt instanceof StatementList) {
				res = res.merge (executeBlock ((StatementList)stmt, leaf));
			} else {
				throw new Exception ("Unknown statement");
			}
		}

		return res;
	}

	SEResult executeBlock (StatementList stmts, SETNode leaf) throws Exception {
		SEResult res = new SEResult();
		ArrayList<SETNode> leaves = new ArrayList<>();
		leaves.add(leaf);
		for (Statement stmt: stmts.getStatements()) {
			res = res.merge (executeStatement(stmt, leaves));
			leaves = res.getLive();
		}

		return res;
	}


	Expression defaultValue (Type type) throws Exception {
		if (type.name.equals("int")) {
			return new IntegerConstant(0);
		} else if (type.name.equals("boolean")) {
			return new BooleanConstant(false);
		}
		throw new Exception ("Default value for type not known");
	}

	SEResult executeDeclarations (DeclarationList decls) throws Exception {
		SETNode leaf = null;
		ArrayList<SETNode> live = new ArrayList<>();
		ArrayList<SETNode> done = new ArrayList<>();
		for (Declaration decl: decls) {
			leaf = executeDeclaration(decl, leaf);
			done.add(leaf);
		}

		if (done.size() > 0) done.remove (done.size()-1);
		live.add(leaf);
		return new SEResult(live, done);
	}

	SETInstructionNode executeDeclaration (Declaration decl, SETNode leaf) throws Exception {
		Name name = new Name(decl.vname);
		name.setDeclaration(decl);
		SETInstructionNode inode = new SETInstructionNode(leaf, name, defaultValue(decl.getType()));
		return inode;
	}

	boolean isSat (Expression expr) throws InvalidConfigurationException, Exception {
		String [] args = {};
		Configuration config = Configuration.fromCmdLineArguments(args);
		LogManager logger = BasicLogManager.create(config);
		ShutdownManager shutdown = ShutdownManager.create();

		SolverContext context = SolverContextFactory.createSolverContext(config, logger, shutdown.getNotifier(), Solvers.SMTINTERPOL);
		ProverEnvironment prover = context.newProverEnvironment(ProverOptions.GENERATE_MODELS);
		prover.push();
		ExpressionFormulaGenerator fg = new ExpressionFormulaGenerator(context);
		prover.addConstraint(fg.generate(expr));
		prover.pop();
		return true;
		// return !prover.isUnsat();
	}
}
