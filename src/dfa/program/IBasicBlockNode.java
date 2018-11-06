package program;

import java.util.List;

import ast.Statement;

public interface IBasicBlockNode extends IProgramNode {
	public Statement getStatement();
	public void addStatement(Statement statement);
}
