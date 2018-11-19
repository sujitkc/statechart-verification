package cfg;

import ast.Statement;
import stablcfg.*;
public interface ICFGBasicBlockNode extends ICFGNode {
	public Statement getStatement();
	public void setStatement(Statement stmt);
	public ICFEdge getOutgoingEdge();
	public ICFGNode getSuccessorNode();
	public void setSuccessorNode(CFGNode node);
	public ICFEdge setOutgoingEdge(ICFEdge edge);
}
