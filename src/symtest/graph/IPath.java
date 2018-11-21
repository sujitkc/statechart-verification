package graph;

import java.util.ArrayList;
import java.util.List;

public interface IPath {
	public ArrayList<IEdge> getPath();

	//Return the edge on success; null on failure
	public IEdge addEdge(IEdge edge);

	public void setPath(List<IEdge> path);
	public IGraph getGraph();
	public int getSize();
	public void concatenate(IPath path) throws Exception;
	public void concatenate(IEdge path) throws Exception;
	public boolean hasEdge(IEdge e);
	public String toString();
}
