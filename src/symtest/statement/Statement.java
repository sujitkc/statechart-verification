package statement;

import expression.IExpression;
import expression.IIdentifier;
import program.IProgram;

public class Statement implements IStatement {

	private IIdentifier mLHS;
	private IExpression mRHS;
	private IProgram mProgram;
	
	public Statement(IProgram p, IIdentifier lhs, IExpression rhs) {
		this.mProgram = p;
		this.mLHS = lhs;
		this.mRHS = rhs;
	}
	@Override
	public IIdentifier getLHS() {
		return this.mLHS;
	}

	@Override
	public IExpression getRHS() {
		return this.mRHS;
	}
	
	@Override
	public IProgram getProgram() {
		return this.mProgram;
	}

	@Override
	public String toString() {
		return this.mLHS.toString() + " = " + this.mRHS.toString();
	}
	
}
