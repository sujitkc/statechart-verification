package mycfg;

import cfg.*;
import expression.IIdentifier;
import utilities.IdGenerator;

import java.util.HashSet;
import java.util.Set;

public class CFG implements ICFG {

	private Set<IIdentifier> mVariables = new HashSet<IIdentifier>();

	private ICFGBasicBlockNode mStartNode; // = new CFGBasicBlockNode(null);
	private ICFGBasicBlockNode mStopNode; // = new CFGBasicBlockNode(null);
	
	private Set<ICFGDecisionNode> mDecisionNodeList = new HashSet<ICFGDecisionNode>();
	private Set<ICFGBasicBlockNode> mCFGBasicBlockNodeList = new HashSet<ICFGBasicBlockNode>();
	private Set<ICFEdge> mEdges = new HashSet<ICFEdge>();

	private String mId;

	// what does it do?
	// Initialises CFG instance with start & end node
	public CFG(ICFGBasicBlockNode start, ICFGBasicBlockNode stop) throws Exception {
		this.mStartNode = start;
		if(start == null || stop == null) {
			Exception e = new Exception("Can't construct CFG: start or stop node is null.");
			throw e;
		}
		start.setCFG(this);
		this.mStopNode = stop;
		stop.setCFG(this);
		this.mCFGBasicBlockNodeList.add(start);
		this.mCFGBasicBlockNodeList.add(stop);
		this.mId = CFG.generateId();
	}

	public CFG(String id, ICFGBasicBlockNode start, ICFGBasicBlockNode stop) throws Exception {
		this.mStartNode = start;
		if(start == null || stop == null) {
			Exception e = new Exception("Can't construct CFG: start or stop node is null.");
			throw e;
		}
		start.setCFG(this);
		this.mStopNode = stop;
		stop.setCFG(this);
		this.mCFGBasicBlockNodeList.add(start);
		this.mCFGBasicBlockNodeList.add(stop);
		
		if(IdGenerator.hasId(id)) {
			Exception e = new Exception("Can't construct CFG : something with name '" + id + "' already exists.");
			throw e;
		}
		IdGenerator.addId(id);
		this.mId = id;
	}

	private static String generateId() {
		return IdGenerator.generateId("CFG");
	}

	@Override
	public ICFGBasicBlockNode getStartNode() {
		return this.mStartNode;
	}

	@Override
	public ICFGBasicBlockNode getStopNode() {
		return this.mStopNode;
	}

	@Override
	public ICFGDecisionNode addDecisionNode(ICFGDecisionNode node) {
		if(this.hasDecisionNode(node)) {
			return null;
		}
		this.mDecisionNodeList.add(node);
		node.setCFG(this);
		return node;
	}

	@Override
	public ICFGBasicBlockNode addBasicBlockNode(ICFGBasicBlockNode node) {
		if(this.hasBasicBlockNode(node)) {
			return null;
		}
		this.mCFGBasicBlockNodeList.add(node);
		node.setCFG(this);
		return node;
	}

	@Override
	public ICFGBasicBlockNode deleteBasicBlockNode(ICFGBasicBlockNode node) {
		if(!node.getCFG().equals(this)) {
			return null;
		}
		if(!this.hasBasicBlockNode(node)) {
			return null;
		}
		this.mCFGBasicBlockNodeList.remove(node);
		node.setCFG(null);
		return node;
	}

	@Override
	public ICFGDecisionNode deleteDecisionNode(ICFGDecisionNode node) {
		if(!node.getCFG().equals(this)) {
			return null;
		}
		if(!this.hasDecisionNode(node)) {
			return null;
		}
		this.mCFGBasicBlockNodeList.remove(node);
		node.setCFG(null);
		return node;
	}

	@Override
	public boolean hasBasicBlockNode(ICFGBasicBlockNode node) {
		return this.mCFGBasicBlockNodeList.contains(node);
	}

	@Override
	public int getNumberOfBasicBlockNodes() {
		return this.mCFGBasicBlockNodeList.size();
	}

	@Override
	public boolean hasDecisionNode(ICFGDecisionNode node) {
		return this.mDecisionNodeList.contains(node);
	}

	@Override
	public int getNumberOfDecisionNodes() {
		return this.mDecisionNodeList.size();
	}

	@Override
	public boolean hasNode(ICFGNode node) {
		if(node instanceof ICFGDecisionNode) {
			return(this.hasDecisionNode((ICFGDecisionNode)node));
		}
		if(node instanceof ICFGBasicBlockNode) {
			return(this.hasBasicBlockNode((ICFGBasicBlockNode)node));
		}
		return false;
	}

	@Override
	public int getNumberOfNodes() {
		return this.mCFGBasicBlockNodeList.size() + this.mDecisionNodeList.size();
	}

	@Override
	public ICFEdge addEdge(ICFEdge edge) {
		ICFGNode h = edge.getHead();
		ICFGNode t = edge.getTail();
		if(!(this.hasNode(h) && this.hasNode(t))) {
			return null;
		}
		edge.setCFG(this);
		this.mEdges.add(edge);
		if((t instanceof ICFGBasicBlockNode)) {
			ICFGBasicBlockNode node = (ICFGBasicBlockNode)t;
			node.setOutgoingEdge(edge);
		}
		else {
			ICFGDecisionNode node = (ICFGDecisionNode)t;
			if(!((node.getThenEdge() == null) || (node.getElseEdge() == null))) {
				return null;
			}
			if(node.getThenEdge() == null) {
				node.setThenEdge(edge);
			}
			else if(node.getElseEdge() == null) {
				node.setElseEdge(edge);
			}
		}
		h.addIncomingEdge(edge);
//		System.out.println("IncomingEdge: "+h.getIncomingEdgeList());
		return edge;
	}

	@Override
	public ICFEdge deleteEdge(ICFEdge edge) {
		if(!this.hasEdge(edge)) {
			return null;
		}
		edge.setCFG(null);
		ICFGNode h = edge.getHead();
		ICFGNode t = edge.getTail();
		t.deleteOutgoingEdge(edge);
		h.deleteIncomingEdge(edge);
		this.mEdges.remove(edge);
		return edge;
	}

	@Override
	public boolean hasEdge(ICFEdge edge) {
		return this.mEdges.contains(edge);
	}

	@Override
	public int getNumberOfEdges() {
		return this.mEdges.size();
	}

	@Override
	public Set<ICFGNode> getNodeSet() {
		Set<ICFGNode> nodeSet = new HashSet<ICFGNode>();
		for(ICFGNode n : this.mCFGBasicBlockNodeList) {
			nodeSet.add(n);
		}
		for(ICFGNode n : this.mDecisionNodeList) {
			nodeSet.add(n);
		}
		return nodeSet;
	}

	@Override
	public Set<ICFEdge> getEdgeSet() {
		return this.mEdges;
	}

	@Override
	public Set<ICFGDecisionNode> getDecisionNodeSet() {
		return this.mDecisionNodeList;
	}

	@Override
	public Set<ICFGBasicBlockNode> getBasicBlockNodeSet() {
		return this.mCFGBasicBlockNodeList;
	}
	
	@Override
	public IIdentifier addVariable(IIdentifier var) {
		if(this.hasVariable(var)) {
			return null;
		}
		this.mVariables.add(var);
		var.setProgram(this);
		return var;
	}
	
	@Override
	public boolean hasVariable(IIdentifier var) {
		return this.mVariables.contains(var);
	}
	@Override
	public Set<IIdentifier> getVariables() {
		return this.mVariables;
	}
	@Override
	public String getId() {
		return this.mId;
	}
	
	public String toString() {
		String s = "CFG = " + this.mId;
		s = "Nodes {\n";
		for(ICFGNode node : this.getNodeSet()) {
			s = s + node.toString();
		}
		for(ICFEdge edge : this.getEdgeSet()) {
			s = s + edge.toString();
		}
		return s;
	}
}
