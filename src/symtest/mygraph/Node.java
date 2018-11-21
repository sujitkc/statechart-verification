package mygraph;

import graph.IEdge;
import graph.IGraph;
import graph.INode;
import utilities.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class Node implements INode {

	private IGraph mGraph;
	private List<IEdge> mOutgoingEdgeList = new ArrayList<IEdge>();
	private List<IEdge> mIncomingEdgeList = new ArrayList<IEdge>();
	private String mId;

	public Node(IGraph graph) {
		if(graph != null) {
			graph.addNode(this);
		}
		else {
			this.mGraph = graph;
		}
		this.mId = Node.generateId();
	}

	private static String generateId() {
		return IdGenerator.generateId("node");
	}

	public Node(String id, IGraph graph) throws Exception {
		if(graph != null) {
			graph.addNode(this);
		}
		else {
			this.mGraph = graph;
		}
		if(IdGenerator.hasId(id)) {
			Exception e = new Exception("Can't construct node : something with name '" + id + "' already exists.");
			throw e;
		}
		IdGenerator.addId(id);
		this.mId = id;
	}


	@Override
	public List<INode> getChildList() {
		List<INode> list = new ArrayList<INode>();
		for(int i = 0; i < this.mOutgoingEdgeList.size(); i++) {
			list.add(this.mOutgoingEdgeList.get(i).getHead());
		}
		return list;
	}

	@Override
	public List<INode> getParentList() {
		List<INode> list = new ArrayList<INode>();
		for(int i = 0; i < this.mIncomingEdgeList.size(); i++) {
			list.add(this.mIncomingEdgeList.get(i).getTail());
		}
		return list;
	}

	@Override
	public IGraph getGraph() {
		return this.mGraph;
	}

	@Override
	public void setGraph(IGraph graph) {
		this.mGraph = graph;
		
	}

	@Override
	public boolean isIncomingEdge(IEdge edge) {
		return this.mIncomingEdgeList.contains(edge);
	}

	@Override
	public boolean isOutgoingEdge(IEdge edge) {
		return this.mOutgoingEdgeList.contains(edge);
	}

	@Override
	public boolean isParent(INode node) {
		for(IEdge e : this.mIncomingEdgeList) {
			if(e.getTail().equals(node)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isChild(INode node) {
		for(IEdge e : this.mOutgoingEdgeList) {
			if(e.getHead().equals(node)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<IEdge> getIncomingEdgeList() {
		return this.mIncomingEdgeList;
	}

	@Override
	public List<IEdge> getOutgoingEdgeList() {
		return this.mOutgoingEdgeList;
	}

	@Override
	public void addIncomingEdge(IEdge edge) {
		this.mIncomingEdgeList.add(edge);
	}

	@Override
	public void addOutgoingEdge(IEdge edge) {
		this.mOutgoingEdgeList.add(edge);
	}

	@Override
	public void deleteIncomingEdge(IEdge edge) {
		this.mIncomingEdgeList.remove(edge);
	}

	@Override
	public void deleteOutgoingEdge(IEdge edge) {
		this.mOutgoingEdgeList.remove(edge);
	}

	@Override
	public String getId() {
		return this.mId;
	}
}
