package visitor;

import ast.*;
import java.util.Stack;
import org.sosy_lab.java_smt.api.*;

public class ExpressionFormulaGenerator implements ExpressionVisitor {
	private Stack<Formula> _stack;
	private IntegerFormulaManager _imgr;
	private BooleanFormulaManager _bmgr;

	public ExpressionFormulaGenerator (SolverContext context) {
		this._stack = new Stack<>();
		this._imgr = context.getFormulaManager().getIntegerFormulaManager();
		this._bmgr = context.getFormulaManager().getBooleanFormulaManager();
	}

	public BooleanFormula generate(Expression expr) throws Exception {
		expr.visit(this);
		BooleanFormula form = (BooleanFormula)_stack.pop();
		// return _bmgr.equivalence(form, _bmgr.makeTrue());
		return form;
	}

	public void visitBinaryExpression(BinaryExpression expr) throws Exception {
		expr.left.visit(this);
		expr.right.visit(this);

		Formula rhs = _stack.pop();
		Formula lhs = _stack.pop();

		if (expr.operator.equals("&&")) {
			if (rhs instanceof BooleanFormula) {
				_stack.push (_bmgr.and((BooleanFormula)lhs, (BooleanFormula)rhs));
			} else { // IntegerFormula
				_stack.push (_imgr.equal ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
			}
		} else if (expr.operator.equals("=")) {
			if (rhs instanceof BooleanFormula) {
				_stack.push (_bmgr.equivalence ((BooleanFormula)lhs, (BooleanFormula)rhs));
			} else { // IntegerFormula
				_stack.push (_imgr.equal ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
			}
		} else if (expr.operator.equals("||")) {
			if (rhs instanceof BooleanFormula) {
				_stack.push (_bmgr.or((BooleanFormula)lhs, (BooleanFormula)rhs));
			} else { // IntegerFormula
				// TODO: check
				_stack.push (_imgr.equal ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
			}
		} else if (expr.operator.equals ("+")){
			if (lhs instanceof BooleanFormula) {
				lhs = _imgr.makeNumber (_bmgr.isTrue((BooleanFormula)lhs) ? 1 : 0);
			}
			if (rhs instanceof BooleanFormula) {
				rhs = _imgr.makeNumber (_bmgr.isTrue((BooleanFormula)rhs) ? 1 : 0);
			}

			_stack.push(_imgr.add ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
		} else if (expr.operator.equals (">")) {
			_stack.push (_imgr.greaterThan ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
		} else {
			throw new Exception("Unknown binary operator: " + expr.operator);
		}
	}

	public void visitBooleanConstant(BooleanConstant expr) throws Exception {
		_stack.push(expr.value ? _bmgr.makeTrue() : _bmgr.makeFalse());
	}

	public void visitFunctionCall(FunctionCall expr) throws Exception {
		// System.out.println("Call: " + expr.name.name);
		if (expr.name.name.equals ("input")) {
			// System.out.println("HERE!");
			_stack.push (_imgr.makeVariable("event"));
		}
	}

	public void visitIntegerConstant(IntegerConstant expr) throws Exception {
		_stack.push (_imgr.makeNumber (expr.value));
	}

	public void visitName(Name expr) throws Exception {
		Formula res;
		if (expr.getType().name.equals("int")) {
			res = _imgr.makeVariable(expr.name.get(0));
		} else {
			res = _bmgr.makeVariable(expr.name.get(0));
		}
		_stack.push (res);
	}

	public void visitStringLiteral(StringLiteral expr) throws Exception {}

	public void visitUnaryExpression(UnaryExpression expr) throws Exception {
		expr.expression.visit(this);
		// _stack.push (_bmgr.not ((BooleanFormula)_stack.pop()));
		if (expr.operator == UnaryExpression.Operator.NOT) {
			_stack.push (_bmgr.not ((BooleanFormula)_stack.pop()));
		}
		// TODO : Other unaries
	}
}
