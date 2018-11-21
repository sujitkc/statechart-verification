package mygraph;

import graph.IEdge;
import graph.IGraph;
import graph.INode;
import utilities.IdGenerator;

import java.util.HashSet;
import java.util.Set;

public class Graph implements IGraph {

	private final INode mRoot;
	private Set<INode> mNodes = new HashSet<INode>();
	private Set<IEdge> mEdges = new HashSet<IEdge>();
	private String mId;
	
	public Graph(INode root) {
		this.mNodes.add(root);
		this.mRoot = root;
		root.setGraph(this);
		this.mId = Graph.generateId();
	}

	public Graph(String id, INode root) throws Exception {
		this.mNodes.add(root);
		this.mRoot = root;
		root.setGraph(this);
		if(IdGenerator.hasId(id)) {
			Exception e = new Exception("Can't construct graph : something with name '" + id + "' already exists.");
			throw e;
		}
		IdGenerator.addId(id);
		this.mId = id;
	}

	@Override
	public INode getRoot() {
		return this.mRoot;
	}

	@Override
	public INode addNode(INode node) {
		if(this.mNodes.contains(node)) {
			return null;
		}
		this.mNodes.add(node);
		node.setGraph(this);
		return node;
	}

	@Override
	public boolean hasNode(INode node) {
		return this.mNodes.contains(node);
	}

	@Override
	public INode deleteNode(INode node) {
		if(!(this.mNodes.contains(node))) {
			return null;
		}
		this.mNodes.remove(node);
		return node;
	}

	@Override
	public int getNumberOfNodes() {
		return this.mNodes.size();
	}

	@Override
	public IEdge addEdge(IEdge edge) {
		if(this.hasEdge(edge)) {
			return null;
		}
		
		Node h = (Node)edge.getHead();
		Node t = (Node)edge.getTail();
		if(!this.hasNode(h)) {
			return null;
		}
		if(!this.hasNode(t)) {
			return null;
		}
		this.mEdges.add(edge);
		edge.setGraph(this);
		h.addIncomingEdge(edge);
		t.addOutgoingEdge(edge);
		return edge;
	}

	@Override
	public IEdge deleteEdge(IEdge edge) {
		if(this.hasEdge(edge)) {
			this.mEdges.remove(edge);
			edge.setGraph(null);
			Node h = (Node)edge.getHead();
			Node t = (Node)edge.getTail();
			h.deleteIncomingEdge(edge);
			t.deleteOutgoingEdge(edge);
			return edge;
		}
		return null;
	}

	@Override
	public boolean hasEdge(IEdge edge) {
		if(this.mEdges.contains(edge)) {
			return true;
		}
		return false;
	}

	@Override
	public int getNumberOfEdges() {
		return this.mEdges.size();
	}

	@Override
	public Set<INode> getNodeSet() {
		return this.mNodes;
	}

	@Override
	public Set<IEdge> getEdgeSet() {
		return this.mEdges;
	}

	@Override
	public String getId() {
		return this.mId;
	}

	private static String generateId() {
		return IdGenerator.generateId("graph");
	}
	
	public String toString() {
		GraphtoStringVisitor visitor = new GraphtoStringVisitor(this);
		visitor.visit();
		return visitor.getOutputString();
	}
}
