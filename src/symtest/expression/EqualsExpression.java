package expression;

import program.IProgram;
import visitors.IExprVisitor;

public class EqualsExpression extends Expression implements IBinaryExpression {

	private IExpression mLHS;
	private IExpression mRHS;

	public EqualsExpression(IProgram program, IExpression lhs, IExpression rhs) throws Exception {
		super(program, Type.BOOLEAN);
		if(lhs.getType() != rhs.getType()) {
			Exception e = new Exception("EqualsExpression : Type error.");
			throw e;
		}
		this.mLHS = lhs;
		this.mRHS = rhs;
	}

	@Override
	public void accept(IExprVisitor<?> visitor) throws Exception {
		visitor.visit(this.mRHS);
		visitor.visit(this.mLHS);
	}

	@Override
	public IExpression getLHS() {
		return this.mLHS;
	}

	@Override
	public IExpression getRHS() {
		return this.mRHS;
	}

	@Override
	public String toString() {
		return "(" + this.mLHS.toString() + " = " + this.mRHS.toString() + ")";
	}

}
