package mycfg;

import cfg.ICFEdge;
import cfg.ICFG;
import cfg.ICFGNode;
import graph.INode;

import java.util.ArrayList;
import java.util.List;

public abstract class CFGNode implements ICFGNode {

	protected ICFG mCFG;
	protected List<ICFEdge> mIncomingEdgeList = new ArrayList<ICFEdge>();
	protected String mId;

	@Override
	public boolean isIncomingEdge(ICFEdge edge) {
		return this.mIncomingEdgeList.contains(edge);
	}

	@Override
	public ICFG getCFG() {
		return this.mCFG;
	}

	@Override
	public void setCFG(ICFG graph) {
		this.mCFG = graph;
	}

	@Override
	public List<ICFEdge> getIncomingEdgeList() {
		return this.mIncomingEdgeList;
	}

	@Override
	public boolean isCFPredecessor(INode node) {
		for(ICFEdge e : this.mIncomingEdgeList) {
			if(e.getTail().equals(node)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ICFGNode> getCFPredecessorNodeList() {
		List<ICFGNode> list = new ArrayList<ICFGNode>();
		for(ICFEdge e : this.mIncomingEdgeList) {
			list.add(e.getTail());
		}
		return list;
	}
	

	@Override
	public ICFEdge addIncomingEdge(ICFEdge edge) {
		if(this.mIncomingEdgeList.add(edge)) {
			return edge;
		}
		return null;
	}

	@Override
	public ICFEdge deleteIncomingEdge(ICFEdge edge) {
		if(!this.mIncomingEdgeList.contains(edge)) {
			return null;
		}
		this.mIncomingEdgeList.remove(edge);
		return edge;
	}
	
	public String getId() {
		return this.mId;
	}
}