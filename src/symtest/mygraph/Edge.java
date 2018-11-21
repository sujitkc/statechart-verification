package mygraph;

import graph.IEdge;
import graph.IGraph;
import graph.INode;
import utilities.IdGenerator;

/**
 * @author ZZ4JWJ
 *
 */
public class Edge implements IEdge {

	private IGraph mGraph;
	private INode mTail; //source
	private INode mHead; //target
		
	private String mId;
	
	public Edge(IGraph graph, INode tail, INode head) throws Exception {
		this.mTail = tail;
		this.mHead = head;
		if(graph != null) {
			graph.addEdge(this);
		}
		this.mId = Edge.generateId();
	}

	public Edge(String id, IGraph graph, INode tail, INode head) throws Exception {
		this.mTail = tail;
		this.mHead = head;
		if(graph != null) {
			graph.addEdge(this);
		}
		if(IdGenerator.hasId(id)) {
//			IdGenerator.printAllIds();
			Exception e = new Exception("Can't construct edge : something with name '" + id + "' already exists.");
			throw e;			
		}
		IdGenerator.addId(id);
		this.mId = id;
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
	public INode getHead() {
		return this.mHead;
	}

	@Override
	public INode getTail() {
		return this.mTail;
	}

	@Override
	public String getId() {
		return this.mId;
	}
	
	private static String generateId() {
		return IdGenerator.generateId("edge");
	}
}
