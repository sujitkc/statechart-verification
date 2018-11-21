package expression;

import program.IProgram;
import visitors.IExprVisitor;

public class Input extends Expression {

	public Input(IProgram program) throws Exception {
		super(program, Type.INT);
	}
	
	public Input(IProgram program, String type) throws Exception {
		super(program, type);
	}
	
	@Override
	public String toString() {
		return "Input";
	}

	@Override
	public void accept(IExprVisitor<?> visitor) {
	}
}
