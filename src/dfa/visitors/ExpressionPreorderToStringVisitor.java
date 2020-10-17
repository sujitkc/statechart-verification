package visitors;

import java.util.Stack;

//import visitors.IExprVisitor;
import ast.*;

public class ExpressionPreorderToStringVisitor{ // implements IExprVisitor<String> {

	Stack<String> mStack = new Stack<String>();
	
	public void visit(IntegerConstant exp)  {
		this.mStack.push(exp.toString());
	}

	public void visit(BooleanConstant exp) {
		this.mStack.push(exp.toString());
	}
	public void visit(Name exp) {
		this.mStack.push(exp.toString());
	}

	public void visit(BinaryExpression exp)  {
		//exp.accept(this);
		this.visit(exp.left);
		this.visit(exp.right);
		
		String s = "( "+exp.operator+" " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}

	/*public void visit(LesserThanExpression exp) throws Exception  {
		exp.accept(this);
		String s = "( < " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}

	@Override
	public void visit(GreaterThanEqualToExpression exp) throws Exception  {
		exp.accept(this);
		String s = "( >= " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}

	@Override
	public void visit(LesserThanEqualToExpression exp) throws Exception  {
		exp.accept(this);
		String s = "( <= " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}
	
	@Override
	public void visit(AddExpression exp)  throws Exception {
		exp.accept(this);
		String s = "( + " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}
	

	@Override
	public void visit(SubExpression exp)  throws Exception {
		exp.accept(this);
		String s = "( - " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}

	@Override
	public void visit(MulExpression exp)  throws Exception {
		exp.accept(this);
		String s = "( * " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}

	@Override
	public void visit(DivExpression exp)  throws Exception {
		exp.accept(this);
		String s = "( / " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}


	@Override
	public void visit(AndExpression exp) throws Exception {
		exp.accept(this);
		String s = "( and " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}

	@Override
	public void visit(OrExpression exp) throws Exception {
		exp.accept(this);
		String s = "( or " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}
	
	@Override
	public void visit(True exp) throws Exception {
		this.mStack.push(exp.toString());
	}
	
	@Override
	public void visit(Variable exp) throws Exception {
		this.mStack.push(exp.toString());
	}

	@Override
	public void visit(BooleanVariable exp) throws Exception {
		this.mStack.push(exp.toString());
		
	}
	
	@Override
	public void visit(NotExpression exp) throws Exception {
		exp.accept(this);
		String s = "( not " + this.mStack.pop() + ")";
		this.mStack.push(s);
	}

	*/
	public void visit(UnaryExpression exp) {
		//exp.accept(this);
		String s ="";// "( = " + this.mStack.pop() + " " + this.mStack.pop() + ")";
		
		this.mStack.push(s);
	}
	
	public void visit(Expression exp) {
		if(exp instanceof BinaryExpression) {
			this.visit((BinaryExpression)exp);
		}else if(exp instanceof UnaryExpression) {
			this.visit((UnaryExpression)exp);
		}
		/*
		else if(exp instanceof Name) {
			this.visit((Name)exp);
		}
		else if(exp instanceof Variable) {
			this.visit((Variable)exp);
		}
		else if(exp instanceof BooleanVariable) {
			this.visit((BooleanVariable)exp);
		}
		else if (exp instanceof Input) {
			this.visit((Input)exp);
		}
		else if(exp instanceof AddExpression) {
			this.visit((AddExpression)exp);
		}

		else if(exp instanceof SubExpression) {
			this.visit((SubExpression)exp);
		}

		else if(exp instanceof MulExpression) {
			this.visit((MulExpression)exp);
		}

		else if(exp instanceof DivExpression) {
			this.visit((DivExpression)exp);
		}

		else if(exp instanceof GreaterThanEqualToExpression) {
			this.visit((GreaterThanEqualToExpression)exp);
		}
		
		else if(exp instanceof LesserThanExpression) {
			this.visit((LesserThanExpression)exp);
		}
		else if(exp instanceof LesserThanEqualToExpression) {
			this.visit((LesserThanEqualToExpression)exp);
		}
		
		else if(exp instanceof AndExpression) {
			this.visit((AndExpression)exp);
		}
		else if(exp instanceof OrExpression) {
			this.visit((OrExpression)exp);
		}
		else if(exp instanceof NotExpression) {
			this.visit((NotExpression)exp);
		}
		else if(exp instanceof EqualsExpression) {
			this.visit((EqualsExpression)exp);
		}*/
		else if(exp == null) {
		}
		else {
			/*Exception e = new Exception("ExpressionPreorderToStringVisitor : Type '" + exp.getClass().getCanonicalName() + "' of expression not handled.");
			throw e;*/
		}
	}
	
	public String getValue() {
		return this.mStack.peek();
	}

	/*@Override
	public void visit(Input exp) {
		// TODO Auto-generated method stub
		
	}*/

}
