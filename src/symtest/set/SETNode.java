package set;

import cfg.ICFGNode;
import expression.IExpression;
import expression.IIdentifier;

import java.util.List;

public abstract class SETNode {

	protected SETEdge mIncomingEdge;
	protected SET mSET;
	
	protected ICFGNode mCFGNode;
	
	protected String mId;


	public SETNode(SET set, ICFGNode cfgNode) {
		this.mSET = set;
		this.mCFGNode = cfgNode;
//		here we need to assign mIncomingedge
//		this.mIncomingEdge = cfgNode.getEd;
	}

	public boolean isIncomingEdge(SETEdge edge) {
		return this.mIncomingEdge.equals(edge);
	}

	public SET getSET() {
		return this.mSET;
	}

	public void setSET(SET set) {
		this.mSET = set;
	}

	public String getId() {
		return this.mId;
	}
	
	public SETEdge getIncomingEdge() {
		return this.mIncomingEdge;
	}

/*	public boolean isSETPredecessor(SETNode node) {
		if(this.mIncomingEdge == null) {
			return false;
		}
		if(this.mIncomingEdge.getTail().equals(node)) {
			return true;
		}
		return false;
	}

	public SETNode getSETPredecessorNode() {
		if(this.mIncomingEdge == null) {
			return null;
		}
		return this.mIncomingEdge.getTail();
	}
	
	public SETEdge addIncomingEdge(SETEdge edge) {
		if(!(this.mIncomingEdge == null)) {
			return null;
		}
		return this.setIncomingEdge(edge);
	}
*/
	public SETEdge setIncomingEdge(SETEdge edge) {
		if(!this.mSET.equals(edge.getSET())) {
			return null;
		}
		this.mIncomingEdge = edge;
		return edge;
	}
	
	public SETEdge deleteIncomingEdge() {
		SETEdge edge = this.mIncomingEdge;
		this.mIncomingEdge = null;
		return edge;
	}

	public ICFGNode getCFGNode() {
		return this.mCFGNode;
	}

	public void setCFGNode(ICFGNode node) {
		this.mCFGNode = node;
	}
	
	public SETNode getPredecessorNode() {
		if(this.mIncomingEdge != null) {
			if(this.mIncomingEdge.getTail() != null) {
				return this.mIncomingEdge.getTail();
			}
		}
		return null;
	}
	
	public abstract List<SETEdge> getOutgoingEdgeList();
	public abstract List<SETNode> getSuccessorSETNodeList();
	public abstract IExpression getLatestValue(IIdentifier var);

	public abstract IExpression getPathPredicate() throws Exception;
}