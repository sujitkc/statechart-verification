package expression;

import program.IProgram;
import visitors.IExprVisitor;

public class ConcreteConstant  extends Expression {

	private final int mValue;

	public ConcreteConstant(int value, IProgram program) throws Exception {
		super(program, Type.INT);
		this.mValue = value;
	}

	public int getValue() {
		return this.mValue;
	}
	
	@Override
	public String toString() {
		Integer i = new Integer(this.mValue);
		return i.toString();
	}

	@Override
	public void accept(IExprVisitor<?> visitor) {
	}
}
