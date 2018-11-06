package cfg;


import java.util.List;

import ast.Statement;

public interface ICFGNode {

	public boolean isIncomingEdge(ICFEdge edge);
	public boolean isOutgoingEdge(ICFGNode node);
	public boolean isCFSuccessor(ICFGNode node);
	public ICFG getCFG();
	public void setCFG(ICFG graph);	
	public List<ICFEdge> getIncomingEdgeList();
	public List<ICFEdge> getOutgoingEdgeList();
	public List<ICFGNode> getCFPredecessorNodeList();
	public List<ICFGNode> getCFSuccessorNodeList();
	public ICFEdge addIncomingEdge(ICFEdge edge);
	public ICFEdge deleteIncomingEdge(ICFEdge edge);
	public ICFEdge addOutgoingEdge(ICFEdge edge);
	public ICFEdge deleteOutgoingEdge(ICFEdge edge);
	public String getId();
	
}
