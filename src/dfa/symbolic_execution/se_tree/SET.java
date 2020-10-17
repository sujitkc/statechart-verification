/*package set;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.*;

public class SET{
	
	SETNode mStartNode;
	
	Set<SETNode> mNodeSet = new HashSet<SETNode>();
	
	Set<Name> mVariableSet = new HashSet<Name>();
	
	private final String mId;
	
	public SET() {
		this.mStartNode = new SETNode(null);
		this.mNodeSet.add(this.mStartNode);
	}
	
	public SETNode getStartNode() {
		return this.mStartNode;
	}
	
	
	
	public SETNode addNode(SETNode node) {
		if(this.mNodeSet.contains(node)) {
			return null;
		}
		this.mNodeSet.add(node);
		//node.setSET(this);
		return node;
	}
	
	
	
	/*public int getNumberOfBasicBlockNodes() {
		return this.mBasicBlockNodeSet.size();
	}
	
	public boolean hasSETDecisionNode(SETDecisionNode node) {
		return this.mDecisionNodeSet.contains(node);
	}
	
	public int getNumberOfDecisionNodes() {
		return this.mDecisionNodeSet.size();
	}
	
	public boolean hasNode(SETNode node) {
		if(node instanceof SETBasicBlockNode) {
			return this.hasBasicBlockNode((SETBasicBlockNode)node);
		}
		else if(node instanceof SETDecisionNode) {
			this.hasSETDecisionNode((SETDecisionNode)node);
		}
		return false;
	}
	
	public int getNumberOfNodes() {
		return this.getNumberOfBasicBlockNodes() + this.getNumberOfDecisionNodes();
	}
	
	public SETEdge addEdge(SETEdge edge) {
		if(this.mSETEdgeSet.contains(edge)) {
			return null;
		}
		this.mSETEdgeSet.add(edge);
		edge.setSET(this);
		return edge;
	}
	
	public int getNumberOfEdges() {
		return this.mSETEdgeSet.size();
	}
	
	public Set<SETNode> getNodeSet() {
		Set<SETNode> set = new HashSet<SETNode>();
		for(SETNode n : this.mBasicBlockNodeSet) {
			set.add(n);
		}
		for(SETNode n : this.mDecisionNodeSet) {
			set.add(n);
		}
		return set;
	}
	
	
	//@Override
	public Name addVariable(Name var) {
		if(this.hasVariable(var)) {
			return null;
		}
		this.mVariableSet.add(var);
		return var;
	}
	
	@Override
	public boolean hasVariable(IIdentifier var) {
		return this.mVariableSet.contains(var);
	}

	
	public void updateLeafNodeSet() {
		this.mLeafNodeSet.clear();
		for(SETNode node : this.getNodeSet()) {
			List<SETNode> succ = node.getSuccessorSETNodeList();
			if(succ.isEmpty()) {
				this.mLeafNodeSet.add(node);
			}
		}
	}

	@Override
	public Set<IIdentifier> getVariables() {
		return this.mVariableSet;
	}

	
	/*public String toString() {
		SETtoStringVisitor visitor = new SETtoStringVisitor(this);
		try {
			visitor.visit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return visitor.getOutputString();
	}
}*/
