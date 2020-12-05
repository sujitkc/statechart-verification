package see;

import program.Program;
import ast.*;
import set.*;
import java.util.ArrayList;

public class Engine {
	final private Program _program;
	final private int _max_depth;
	private SEResult _res;

	Engine (Program program, int max_depth) throws Exception {
		this._program = program;
		this._max_depth = max_depth;

		DeclarationList decls = new DeclarationList();
		decls.addAll(this._program.declarations);
		decls.addAll(this._program.other_declarations);
		SEResult result = executeDeclarations(decls);
		SETNode leaf = result.getLastestLeaf();
		_res = this.executeStatements (this._program.statements, leaf);
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
			return getVarValue (name, leaf);
		}

		throw new Exception ("Unexpected expression");
	}

	Expression getVarValue (Name name, SETNode leaf) throws Exception {
		if (leaf instanceof SETDecisionNode) {
			return getVarValue(name, leaf.getParent());
		} else if (leaf instanceof SETInstructionNode) {
			SETInstructionNode inode = (SETInstructionNode)leaf;
			if (inode.getName().getDeclaration() == name.getDeclaration()) {
				return inode.getExpression();
			} else {
				return getVarValue(name, leaf.getParent());
			}
		} else {
			throw new Exception("Variable not found");
		}
	}

	SEResult executeStatements (StatementList stmts, SETNode leaf) throws Exception {
		ArrayList<SETNode> live = new ArrayList<>();
		ArrayList<SETNode> done = new ArrayList<>();
		for (Statement stmt: stmts.getStatements()) {
			leaf = executeStatement(stmt, leaf);
			if (leaf.getDepth() >= this._max_depth) {
				done.add(leaf);
				break;
			} else {
				live.add(leaf);
			}
		}
		return new SEResult(live, done);
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

		throw Exception ("Unknown statement");
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


	boolean isSat (Expression expr) {
		return true;
	}
}
