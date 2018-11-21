package statement;

import expression.IExpression;
import expression.IIdentifier;
import program.IProgram;

public interface IStatement {
	public IIdentifier getLHS();
	public IExpression getRHS();
	public IProgram getProgram();
}
