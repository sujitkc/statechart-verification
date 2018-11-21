package graph;

public interface IEdge {

	public INode getHead(); //target node
	public INode getTail(); //source node
	public IGraph getGraph();
	public void setGraph(IGraph graph);
	public String getId();
}
