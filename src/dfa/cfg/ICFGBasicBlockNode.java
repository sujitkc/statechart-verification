package cfg;

import ast.Statement;
public interface ICFGBasicBlockNode extends ICFGNode {
	public Statement getStatement();
	public void setStatement(Statement stmt);
	public ICFEdge getOutgoingEdge();
	public ICFGNode getSuccessorNode();
	public ICFEdge setOutgoingEdge(ICFEdge edge);
}
