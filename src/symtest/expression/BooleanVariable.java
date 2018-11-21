package expression;

import program.IProgram;
import visitors.IExprVisitor;

public class BooleanVariable extends Expression implements IIdentifier {
	private final String mName;

	public BooleanVariable(String name, IProgram program) throws Exception {
		super(program, Type.BOOLEAN);
		this.mName = name;
		if(program.hasVariable(this)) {
			Exception e = new Exception("Variable named '" + name + "' already exists in program.");
			throw e;
		}
		program.addVariable(this);
}

	@Override
	public String getName() {
		return this.mName;
	}

	@Override
	public String toString() {
		return this.mName;
	}

	@Override
	public void accept(IExprVisitor<?> visitor) throws Exception {
	}
}
