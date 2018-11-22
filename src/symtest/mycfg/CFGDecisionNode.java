package mycfg;

import cfg.ICFEdge;
import cfg.ICFG;
import cfg.ICFGDecisionNode;
import cfg.ICFGNode;
import expression.IExpression;
import graph.INode;
import utilities.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class CFGDecisionNode extends CFGNode implements ICFGDecisionNode {
	private IExpression mCondition;
	private ICFEdge mThenEdge;
	private ICFEdge mElseEdge;
	
	public CFGDecisionNode(ICFG cfg, IExpression c) {
		this.mCFG = cfg;
		if(cfg != null) {
			cfg.addDecisionNode(this);
		}
		this.mCondition = c;
//		System.out.println("CFGDecisionNodeCondition:"+this.mCondition);
		this.mId = CFGDecisionNode.generateId();
	}

	public CFGDecisionNode(String id, ICFG cfg, IExpression c) throws Exception {
		this.mCFG = cfg;
		if(cfg != null) {
			cfg.addDecisionNode(this);
		}
		this.mCondition = c;
		if(IdGenerator.hasId(id)) {
			Exception exception = new Exception("Can't construct CFGDecisionNode : something with name '" + id + "' already exists.");
			throw exception;
		}
		IdGenerator.addId(id);
		this.mId = id;
	}

	@Override
	public boolean isOutgoingEdge(ICFEdge edge) {
		return edge.equals(this.mThenEdge) || edge.equals(this.mElseEdge);
	}

	@Override
	public ICFEdge getThenEdge() {
		return this.mThenEdge;
	}

	@Override
	public ICFEdge getElseEdge() {
		return this.mElseEdge;
	}

	@Override
	public ICFGNode getThenSuccessorNode() {
		return this.mThenEdge.getHead();
	}

	@Override
	public ICFGNode getElseSuccessorNode() {
		return this.mElseEdge.getHead();
	}
	@Override
	public IExpression getCondition() {
		return this.mCondition;
	}

	@Override
	public ICFEdge setThenEdge(ICFEdge edge) {
		this.mThenEdge = edge;
		return edge;
	}

	@Override
	public ICFEdge setElseEdge(ICFEdge edge) {
		this.mElseEdge = edge;
		return edge;
	}

	@Override
	public ICFEdge addOutgoingEdge(ICFEdge edge) {
		if(this.mThenEdge == null) {
			return this.setThenEdge(edge);
		}
		if(this.mElseEdge == null) {
			return this.setElseEdge(edge);
		}
		return null;
	}

	@Override
	public ICFEdge deleteOutgoingEdge(ICFEdge edge) {
		if(edge.equals(this.mThenEdge)) {
			return this.deleteThenEdge();
		}
		if(edge.equals(this.mElseEdge)) {
			return this.deleteElseEdge();
		}
		return null;
	}

	@Override
	public ICFEdge deleteThenEdge() {
		ICFEdge e = this.mThenEdge;
		this.mThenEdge = null;
		return e;
	}

	@Override
	public ICFEdge deleteElseEdge() {
		ICFEdge e = this.mElseEdge;
		this.mElseEdge = null;
		return e;
	}

	@Override
	public boolean isCFSuccessor(INode node) {
		if(this.mThenEdge.getHead().equals(node)) {
			return true;
		}
		if(this.mElseEdge.getHead().equals(node)) {
			return true;
		}
		return false;
	}

	@Override
	public List<ICFGNode> getCFSuccessorNodeList() {
		List<ICFGNode> list = new ArrayList<ICFGNode>();
		if(this.mThenEdge != null) {
			if(this.mThenEdge.getHead() != null) {
				list.add(this.mThenEdge.getHead());
			}
		}
		if(this.mElseEdge != null) {
			if(this.mElseEdge.getHead() != null) {
				list.add(this.mElseEdge.getHead());
			}
		}
		return list;
	}

	@Override
	public List<ICFEdge> getOutgoingEdgeList() {
		List<ICFEdge> list = new ArrayList<ICFEdge>();
		if(this.mThenEdge != null) {
			list.add(this.mThenEdge);
		}
		if(this.mElseEdge != null) {
			list.add(this.mElseEdge);
		}
		return list;
	}
	
	private static String generateId() {
		return IdGenerator.generateId("CFGDecisionNode");
	}
	
	public String toString() {
		String s = "CFGDecisionNode " + this.mId + "\n";
		s = s + "Incoming edges = { ";
		for(ICFEdge e : this.mIncomingEdgeList) {
			s = s + e.getId() + " ";
		}

		s = s + "}\n";
		s = s + "Outgoing edges = { ";
		if(this.mThenEdge != null) {
		s = s + this.mThenEdge.getId() + " ";
		}
		else {
			s = s + "null ";
		}
		if(this.mElseEdge != null) {
			s = s + this.mElseEdge.getId();
		}
		else {
			s = s + "null";
		}
		s = s + "}\n";
		
		return s;
	}
}
