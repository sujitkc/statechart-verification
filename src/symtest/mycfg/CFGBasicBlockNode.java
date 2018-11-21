package mycfg;

import cfg.ICFEdge;
import cfg.ICFG;
import cfg.ICFGBasicBlockNode;
import cfg.ICFGNode;
import graph.INode;
import statement.IStatement;
import statement.Statement;
import utilities.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class CFGBasicBlockNode extends CFGNode implements ICFGBasicBlockNode {

	private ICFEdge mOutgoingEdge = null;
	// private List<ICFEdge> mOutgoingEdgeList = new ArrayList<ICFEdge>();
	private IStatement mStatement;

	public CFGBasicBlockNode(ICFG cfg) {
		this.mCFG = cfg;
		if (cfg != null) {
			cfg.addBasicBlockNode(this);
		}
		this.mId = generateId();
	}

	public CFGBasicBlockNode(String id, ICFG cfg) throws Exception {
		this.mCFG = cfg;
		if (cfg != null) {
			cfg.addBasicBlockNode(this);
		}
		if (IdGenerator.hasId(id)) {
			Exception e = new Exception(
					"Can't construct CFGBasicBlockNode : something with name '"
							+ id + "' already exists.");
			throw e;
		}
		IdGenerator.addId(id);
		this.mId = id;
	}

	@Override
	public boolean isOutgoingEdge(ICFEdge edge) {
		return this.mOutgoingEdge.equals(edge);
	}

	@Override
	public ICFEdge getOutgoingEdge() {
		return this.mOutgoingEdge;
	}

	@Override
	public ICFGNode getSuccessorNode() {
		if (this.mOutgoingEdge == null) {
			return null;
		}
		return this.mOutgoingEdge.getHead();
	}

	@Override
	public boolean isCFPredecessor(INode node) {
		for (ICFEdge e : this.mIncomingEdgeList) {
			if (e.getTail().equals(node)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isCFSuccessor(INode node) {
		if (this.mOutgoingEdge == null) {
			return false;
		}
		return this.mOutgoingEdge.getHead().equals(node);
	}

	@Override
	public List<ICFGNode> getCFPredecessorNodeList() {
		List<ICFGNode> list = new ArrayList<ICFGNode>();
		for (ICFEdge e : this.mIncomingEdgeList) {
			list.add(e.getTail());
		}
		return list;
	}

	@Override
	public ICFEdge setOutgoingEdge(ICFEdge edge) {
		this.mOutgoingEdge = edge;
		return edge;
	}

	@Override
	public ICFEdge addOutgoingEdge(ICFEdge edge) {
		if (this.mOutgoingEdge != null) {
			return null;
		}
		return this.setOutgoingEdge(edge);
	}

	@Override
	public ICFEdge deleteOutgoingEdge() {
		ICFEdge e = this.mOutgoingEdge;
		this.mOutgoingEdge = null;
		return e;
	}

	@Override
	public ICFEdge deleteOutgoingEdge(ICFEdge edge) {
		if (edge.equals(this.mOutgoingEdge)) {
			return this.deleteOutgoingEdge();
		}
		return null;
	}

	@Override
	public List<ICFGNode> getCFSuccessorNodeList() {
		List<ICFGNode> list = new ArrayList<ICFGNode>();
		if (this.mOutgoingEdge != null) {
			if (this.mOutgoingEdge.getHead() != null) {
				list.add(this.mOutgoingEdge.getHead());
			}
		}
		return list;
	}

	@Override
	public List<ICFEdge> getOutgoingEdgeList() {
		List<ICFEdge> list = new ArrayList<ICFEdge>();
		if (this.mOutgoingEdge != null) {
			list.add(this.mOutgoingEdge);
		}
		return list;
	}

	public IStatement getStatement() { return this.mStatement; }

	@Override
	public void setStatement(IStatement statement) {
		this.mStatement = statement;
	}

	private String generateId() {
		if (this.mCFG != null)
			return IdGenerator.generateId(this.mCFG.getId()
					+ "CFGBasicBlockNode");
		else
			return IdGenerator.generateId("CFGBasicBlockNode");
	}

	public String toString() {
		String s = "CFGBasicBlockNode " + this.mId + "\n";
		s = s + "Incoming edges = { ";
		for (ICFEdge e : this.mIncomingEdgeList) {
			s = s + e.getId() + " ";
		}

		s = s + "}\n";
		s = s + "Outgoing edges = { ";
		if (this.mOutgoingEdge != null) {
			s = s + this.mOutgoingEdge.getId();
		} else {
			s = s + "null";
		}
		s = s + "}\n";
		return s;
	}
}
