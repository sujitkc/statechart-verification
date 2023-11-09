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
		System.out.println("get solver name"+context.getSolverName());
		this._imgr = context.getFormulaManager().getIntegerFormulaManager();
		this._bmgr = context.getFormulaManager().getBooleanFormulaManager();
	}
	public BooleanFormula testFormula(){
		BooleanFormula b1=this._imgr.equal((NumeralFormula.IntegerFormula)this._imgr.makeVariable("one"),(NumeralFormula.IntegerFormula)this._imgr.makeNumber(10));
		
		return b1;
	}
	public BooleanFormula generate(Expression expr) throws Exception {
		expr.visit(this);
		
		System.out.println("printing the stack::::::::::::::::::::::");
		
		System.out.println(_stack);
		System.out.println("ending printing the stack::::::::::::::::::::::");
		
		Formula f=_stack.pop();
		BooleanFormula form = (BooleanFormula)f;
		////System.out.println(">>>>>>>>afdhsfjhdsjf>>>>>>>");
		//System.out.println("expression :"+expr+"\n : inside generate formula: "+f+"\n : boolean formula :"+form+"\n: bgmr equivalence :"+_bmgr.equivalence(form, _bmgr.makeTrue()));
		//System.out.println("<<<<<<<<<<<<<<<<<<<<<");
		// return _bmgr.equivalence(form, _bmgr.makeTrue());
		return form;
	}

	public void visitBinaryExpression(BinaryExpression expr) throws Exception {
		expr.left.visit(this);
		expr.right.visit(this);

		Formula rhs = _stack.pop();
		Formula lhs = _stack.pop();
		//System.out.println("i popped ::::: LHS::::"+lhs+"::::RHS::::"+rhs);
		//System.out.println(expr + ": am i atleast here? : "+expr.operator);
		if (expr.operator.equals("&&")) {
			if (rhs instanceof BooleanFormula) {
				_stack.push (_bmgr.and((BooleanFormula)lhs, (BooleanFormula)rhs));
			} else { // IntegerFormula
				
				_stack.push (_imgr.equal ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
			}
		} else if (expr.operator.equals("=")) {
			//System.out.println("I am here:::"+expr);
			if (rhs instanceof BooleanFormula) {
				_stack.push (_bmgr.equivalence ((BooleanFormula)lhs, (BooleanFormula)rhs));
			} else { // IntegerFormula
			
			//System.out.println("integer formula : "+(NumeralFormula.IntegerFormula)lhs+" : "+(NumeralFormula.IntegerFormula)rhs + ":: "+_imgr.equal ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
				_stack.push (_imgr.equal ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
				
			}
		} else if (expr.operator.equals("||")) {
			if (rhs instanceof BooleanFormula) {
				_stack.push (_bmgr.or((BooleanFormula)lhs, (BooleanFormula)rhs));
			} else { // IntegerFormula
				// TODO: check
				_stack.push (_imgr.equal((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
			}
		} else if (expr.operator.equals ("+")){
			if (lhs instanceof BooleanFormula) {
				lhs = _imgr.makeNumber (_bmgr.isTrue((BooleanFormula)lhs) ? 1 : 0);
			}
			if (rhs instanceof BooleanFormula) {
				//System.out.println("I came till here BGMR::"+(BooleanFormula)rhs +_bmgr.isTrue((BooleanFormula)rhs));
				rhs = _imgr.makeNumber (_bmgr.isTrue((BooleanFormula)rhs) ? 1 : 0);
				//rhs = _imgr.makeNumber (1);
			}
			//System.out.println("LHS::"+lhs+"::RHS::"+rhs+"+++++++++++++++++++");

			_stack.push(_imgr.add ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
			//_stack.push(_imgr.add (lhs, rhs));
		} else if (expr.operator.equals (">")) {
			_stack.push (_imgr.greaterThan ((NumeralFormula.IntegerFormula)lhs, (NumeralFormula.IntegerFormula)rhs));
		} else {
			throw new Exception("Unknown binary operator: " + expr.operator);
		}
		System.out.println("Stack here..... "+_stack);
	}

	public void visitBooleanConstant(BooleanConstant expr) throws Exception {
		_stack.push(expr.value ? _bmgr.makeTrue() : _bmgr.makeFalse());
	}

	public void visitFunctionCall(FunctionCall expr) throws Exception {
		 System.out.println("Call: " + expr + expr.name.name);
		if (expr.name.name.equals ("input")) {
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
		System.out.println("variable created ::::"+res);
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
