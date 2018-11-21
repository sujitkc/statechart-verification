package expression;

import program.IProgram;
import visitors.IAcceptor;

public interface IExpression extends IAcceptor {
	public IProgram getProgram();
	public void setProgram(IProgram program);
	public String getType();
	public String toString();
}
