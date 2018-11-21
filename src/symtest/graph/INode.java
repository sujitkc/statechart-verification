package graph;

import java.util.List;

public interface INode {
	public String getId();
	public List<INode> getChildList();
	public List<INode> getParentList();
	public boolean isIncomingEdge(IEdge edge);
	public boolean isOutgoingEdge(IEdge edge);
	public boolean isParent(INode node);
	public boolean isChild(INode node);
	public IGraph getGraph();
	public void setGraph(IGraph graph);	
	public List<IEdge> getIncomingEdgeList();
	public List<IEdge> getOutgoingEdgeList();
	public void addIncomingEdge(IEdge edge);
	public void addOutgoingEdge(IEdge edge);
	public void deleteIncomingEdge(IEdge edge);
	public void deleteOutgoingEdge(IEdge edge);
}
