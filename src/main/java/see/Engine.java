package see;

import program.Program;
import ast.*;
import set.*;
import java.util.List;
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
	static int exploredNodes=0;

	public enum PropertyOfInterest {
		NON_DETERMINISM,
		STUCK_SPECIFICATION
	}

	private PropertyOfInterest _property;

	public Engine () {
	System.out.println("Engine initialized");
	}

	private static StatementList flattenStatementList (StatementList list) {
		StatementList res = new StatementList();
		for (Statement stmt: list.getStatements()) {
			if (stmt instanceof StatementList) {
				StatementList sl = flattenStatementList((StatementList)stmt);
				for (Statement st: sl.getStatements()) {
					res.add(st);
				}
			} else {
				res.add (stmt);
			}
		}
		return res;
	}

	public SEResult getSEResult (Program program, int max_depth, PropertyOfInterest property) throws Exception {
		this.setUpJavaSMT();
		this._program = program;
		this._max_depth = max_depth;
		this._property = property;
		DeclarationList decls = new DeclarationList();
		decls.addAll(this._program.declarations);
		SEResult result = executeDeclarations(decls);
		StatementList stmts = processProgramStatementList(this._program.statements);
		stmts = flattenStatementList(stmts);
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
			Expression ret = leaf.getVarValue(name.getDeclaration());
			return ret;
		} else if (expr instanceof UnaryExpression) {
			UnaryExpression unary_expr = (UnaryExpression) expr;
			return new UnaryExpression(executeExpression(unary_expr.expression, leaf), unary_expr.operator);
		}

		if (expr instanceof FunctionCall) return expr;
		throw new Exception ("Unexpected expression" + expr);
	}

	SEResult executeInstruction (InstructionStatement istmt, SETNode leaf) {
		if (istmt instanceof HaltStatement || istmt instanceof SkipStatement) {
			ArrayList<SETNode> leaves = new ArrayList<>();
			leaves.add (leaf);
			return new SEResult(leaves, new ArrayList<>());
		}
		if (istmt instanceof ExpressionStatement) {
			Expression expr = ((ExpressionStatement)istmt).expression;
			if (expr instanceof FunctionCall) {
				FunctionCall call = (FunctionCall) expr;
				System.out.println("Property : "+ _property+":" +(_property==PropertyOfInterest.NON_DETERMINISM));
				if (_property == PropertyOfInterest.STUCK_SPECIFICATION) {
					if (call.name.name.equals ("stuck_spec")) {
						this.stuckSpecification (call.argumentList, leaf);
					}
				} else if (_property == PropertyOfInterest.NON_DETERMINISM) {
					if (call.name.name.equals ("non_det")) {
						this.nonDeterminism (call.argumentList, leaf);
					}
				}
			}
			ArrayList<SETNode> leaves = new ArrayList<>();
			leaves.add (leaf);

			return new SEResult(leaves, new ArrayList<>());
		}

		AssignmentStatement stmt = (AssignmentStatement) istmt;
		Expression rhs = stmt.rhs;
		try {
			rhs = executeExpression(rhs, leaf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SETNode node = new SETInstructionNode(leaf, stmt.lhs, rhs);
		SEResult res = new SEResult();
		if (node.getDepth() > _max_depth) {
			res.addToDone (node);
		} else {
			res.addToLive (node);
		}

		return res;
	}

	boolean print_stmts = false;
	SEResult executeDecision (IfStatement stmt, SETNode leaf) throws Exception {
		SEResult res = new SEResult();
		SETDecisionNode then_node = new SETDecisionNode(leaf, stmt.condition);
		// Expression pc = then_node.getPathConstraint();
		Expression pc = stmt.condition;
		// if (pc == null) pc = new BooleanConstant(true);
		pc = executeExpression(pc, leaf);
		if (isSat(pc)) {
			ArrayList<SETNode> leaves = new ArrayList<>();
			leaves.add(then_node);
			SEResult then_res = executeBlock (new StatementList(stmt.then_body), then_node);
			res = res.merge (then_res);
		}

		Expression else_cond = new UnaryExpression(stmt.condition, UnaryExpression.Operator.NOT);
		SETDecisionNode else_node = new SETDecisionNode(leaf, else_cond);
		// if (pc == null) pc = new BooleanConstant(true);
		pc = executeExpression(else_cond, leaf);
		if (isSat (pc)) {
			ArrayList<SETNode> leaves = new ArrayList<> ();
			leaves.add(else_node);
			SEResult else_res = executeBlock ( new StatementList (stmt.else_body), else_node);
			res = res.merge (else_res);
		}

		if (res.getLive().size() == 0) {
			res.addToDone (leaf);
		}
		return res;
	}

	SEResult executeStatement (Statement stmt, ArrayList<SETNode> leaves) throws Exception {
		
		exploredNodes++;
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
			SEResult temp = executeStatement(stmt, leaves);
			leaves = temp.getLive();
			res = res.merge (temp);
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

	SolverContext _context;
	ProverEnvironment _prover;

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
			BooleanFormula form = fg.generate(expr);
			System.out.println("Boolean formula :"+form);
			this._prover.addConstraint(form);
			boolean res = this._prover.isUnsat();
			this._prover.pop();
			System.out.println("result:"+res);
			return !res;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	void stuckSpecification (List<Expression> args, SETNode leaf) {
		// Path constraint would be valid, otherwise we wouldn't have reached this point
		// For stuck stuckSpecification, none of the guards would be true
		// if (!(g1 || g2 || g3)) then stuck

		// Expression expr = new BooleanConstant (false);
		// for (Expression guard: guards) {
		// 	expr = new BinaryExpression(expr, guard, "||");
		// }
		// expr = new UnaryExpression(expr, UnaryExpression.Operator.NOT);
		Expression expr = args.get(0);
		
        	//System.out.println("Expression inside  stuck spec : "+args.get(0) + isSat(expr));
		if (isSat(expr)) {
			System.out.println ("Explored nodes : "+exploredNodes+"---------------------------------------Specification stuck--------------------------------");
			System.exit(0);
		}
	}

	void nonDeterminism (List<Expression> args, SETNode leaf) {
		// 2 or more guards are active simultaneously
		// (g1 ^ g2) V (g2 ^ g3) V (g3 ^ g1)
		// g1 + g2 + g3 ... gn > 1

		// Expression expr = new BooleanConstant (false);
		// for (Expression guard: guards) {
		// 	expr = new BinaryExpression(expr, guard, "+");
		// }

		// expr = new BinaryExpression (expr, new IntegerConstant(1), ">");
		System.out.println("inside nonDeterminism detect");
		Expression expr = args.get(0);
		System.out.println("expression : "+expr);
		if (isSat (expr)) {
			System.out.println ("Non Deterministic state found");
			System.exit(0);
		}
	}
}
