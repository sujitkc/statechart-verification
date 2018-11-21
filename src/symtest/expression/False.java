package expression;

import program.IProgram;
import visitors.IExprVisitor;

public class False extends Expression {

	public False(IProgram program) throws Exception {
		super(program, Type.BOOLEAN);
	}

	@Override
	public String toString() {
		return "false";
	}

	@Override
	public void accept(IExprVisitor<?> visitor) {
	}
}
