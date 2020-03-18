package ast;

import program.IProgram;

public class NotExpression extends UnaryExpression{

	private Expression mExpression;

	public NotExpression(IProgram program, Expression exp){
		super(program, exp ,exp.getType(),0);
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
