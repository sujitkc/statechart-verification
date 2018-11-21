package program;

import statement.IStatement;

import java.util.List;

public interface IBasicBlockNode extends IProgramNode {
	public IStatement getStatement();
	public void setStatement(IStatement statement);
}
