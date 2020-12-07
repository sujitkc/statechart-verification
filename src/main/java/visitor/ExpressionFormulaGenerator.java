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
		return _bmgr.equivalence((BooleanFormula)_stack.pop(), _bmgr.makeTrue());
	}

	public void visitBinaryExpression(BinaryExpression expr) throws Exception {
		expr.left.visit(this);
		expr.right.visit(this);

		Formula rhs = _stack.pop();
		Formula lhs = _stack.pop();

		if (rhs instanceof BooleanFormula) {
			_stack.push (_bmgr.equivalence ((BooleanFormula)rhs, (BooleanFormula)lhs));
		} else { // IntegerFormula
			_stack.push (_imgr.equal ((NumeralFormula.IntegerFormula)rhs, (NumeralFormula.IntegerFormula)lhs));
		}
	}

	public void visitBooleanConstant(BooleanConstant expr) throws Exception {
		_stack.push(expr.value ? _bmgr.makeTrue() : _bmgr.makeFalse());
	}

	public void visitFunctionCall(FunctionCall expr) throws Exception {}

	public void visitIntegerConstant(IntegerConstant expr) throws Exception {
		_stack.push (_imgr.makeNumber (expr.value));
	}

	public void visitName(Name expr) throws Exception {
		Formula res;
		System.out.println ("Name: " + expr);
		System.out.println ("Type: " + expr.getType());
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
		if (expr.operator == UnaryExpression.Operator.NOT) {
			_stack.push (_bmgr.not ((BooleanFormula)_stack.pop()));
		}
	}
}
