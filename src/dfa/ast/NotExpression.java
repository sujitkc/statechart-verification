package ast;

import program.IProgram;

public class NotExpression extends UnaryExpression{

	private Expression mExpression;

	public NotExpression(IProgram program, Expression exp) throws Exception {
		super(program, exp.getType());
		/*if(exp.getType() != Type.BOOLEAN) {
			Exception e = new Exception("NotExpression : Type error in " + exp.toString());
			throw e;
		}*/
		this.mExpression = exp;
	}

	public Expression getExpression() {
		return this.mExpression;
	}
	
	
}
