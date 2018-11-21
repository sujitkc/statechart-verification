package expression;

import program.IProgram;
import visitors.IExprVisitor;

public class LesserThanExpression extends Expression implements IBinaryExpression {

	private IExpression mLHS;
	private IExpression mRHS;
	
	public LesserThanExpression(IProgram program, IExpression lhs, IExpression rhs) throws Exception {
		super(program, Type.BOOLEAN);
		if(lhs.getType() != rhs.getType()) {
			Exception e = new Exception("LesserThanExpression : Type error.");
			throw e;
		}
		this.mLHS = lhs;
		this.mRHS = rhs;
//		System.out.println(lhs.getProgram());
	}

	public IExpression getLHS() {
		return this.mLHS;
	}

	public IExpression getRHS() {
		return this.mRHS;
	}

	@Override
	public String toString() {
		return "(" + this.mLHS.toString() + " < " + this.mRHS.toString() + ")";
	}

	@Override
	public void accept(IExprVisitor<?> visitor) {
		try {
			visitor.visit(this.mRHS);
			visitor.visit(this.mLHS);
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
}
