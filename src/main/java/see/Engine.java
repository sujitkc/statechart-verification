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
		SETNode leaf = result.getLastestLeaf();
		_res = this.executeStatements (this._program.statements, leaf);
		return _res;
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

	SEResult executeStatements (StatementList stmts, SETNode leaf) throws Exception {
		ArrayList<SETNode> live = new ArrayList<>();
		ArrayList<SETNode> done = new ArrayList<>();
		SEResult res = new SEResult(live, done);
		for (Statement stmt: stmts.getStatements()) {
			res = res.merge (executeStatement(stmt, leaf));
		}
		return res;
	}

	SEResult executeStatement (Statement statement, SETNode leaf) throws Exception {

		if (statement instanceof InstructionStatement) {
			AssignmentStatement astmt = (AssignmentStatement) statement;
			Expression rhs = executeExpression(astmt.rhs, leaf);
			SETNode node = new SETInstructionNode(leaf, astmt.lhs, rhs);
			ArrayList<SETNode> live = new ArrayList<>();
			ArrayList<SETNode> done = new ArrayList<>();
			if (node.getDepth() >= _max_depth) {
				done.add(node);
			} else {
				live.add(node);
			}
			return new SEResult(live, done);
		} else if (statement instanceof IfStatement) {
			SEResult res = new SEResult(new ArrayList<>(), new ArrayList<>());
			IfStatement if_statement = (IfStatement)statement;
			Expression e = this.executeExpression(if_statement.condition, leaf);
			if (isSat(e)) {
				SETDecisionNode node = new SETDecisionNode(leaf, e);
				StatementList then_body;
				if (!(if_statement.then_body instanceof StatementList)) {
					then_body = new StatementList();
					then_body.add(if_statement.then_body);
				} else {
					then_body = (StatementList)if_statement.then_body;
				}
				SEResult then_res = executeStatements(then_body, node);
				res = res.merge(then_res);
			}

			Expression fe = new UnaryExpression(e, UnaryExpression.Operator.NOT);
			if (isSat(fe)) {
				SETDecisionNode node = new SETDecisionNode(leaf, fe);
				StatementList else_body;
				if (!(if_statement.then_body instanceof StatementList)) {
					else_body = new StatementList();
					else_body.add(if_statement.else_body);
				} else {
					else_body = (StatementList)if_statement.else_body;
				}
				SEResult else_res = executeStatements(else_body, node);
				res = res.merge(else_res);
			}
			return res;
		} else if (statement instanceof StatementList) {
			return executeStatements ((StatementList)statement, leaf);
		}

		throw new Exception ("Unknown statement");
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
			if (leaf.getDepth() >= _max_depth) {
				done.add(leaf);
			} else {
				live.add(leaf);
			}
		}

		return new SEResult(live, done);
	}

	SETInstructionNode executeDeclaration (Declaration decl, SETNode leaf) throws Exception {
		Name name = new Name(decl.getFullVName());
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
		ExpressionFormulaGenerator fg = new ExpressionFormulaGenerator(context);
		ProverEnvironment prover = context.newProverEnvironment(ProverOptions.GENERATE_MODELS);
		prover.addConstraint(fg.generate(expr));
		return !prover.isUnsat();
	}
}
