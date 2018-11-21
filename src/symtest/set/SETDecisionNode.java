package set;

import cfg.ICFGDecisionNode;
import expression.AndExpression;
import expression.IExpression;
import expression.IIdentifier;
import expression.NotExpression;
import utilities.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class SETDecisionNode extends SETNode {

	private SETEdge mThenEdge;
	private SETEdge mElseEdge;
	private IExpression mCondition;
	
	public SETDecisionNode(IExpression c, SET set, ICFGDecisionNode cfgNode) {
		super(set, cfgNode);
		this.mCondition = c;
		this.mId = SETDecisionNode.generateId();
	}

	public SETDecisionNode(String id, IExpression c, SET set, ICFGDecisionNode cfgNode) throws Exception {
		super(set, cfgNode);
		this.mCondition = c;
		
		if(IdGenerator.hasId(id)) {
			Exception e = new Exception("Can't construct SETDecisionNode : something with name '" + id + "' already exists.");
			throw e;			
		}
		IdGenerator.addId(id);
		this.mId = id;
	}

	private static String generateId() {
		return IdGenerator.generateId("SETDecisionNode");
	}

	@Override
	public List<SETEdge> getOutgoingEdgeList() {
		List<SETEdge> list = new ArrayList<SETEdge>();
		if(this.mThenEdge != null) {
			list.add(this.mThenEdge);
		}
		if(this.mElseEdge != null) {
			list.add(this.mElseEdge);
		}
		return list;
	}

	@Override
	public List<SETNode> getSuccessorSETNodeList() {
		List<SETNode> list = new ArrayList<SETNode>();
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
	public IExpression getLatestValue(IIdentifier var) {
//		System.out.println("type: "+this.getClass());
//		System.out.println("Outgoing edge: "+this.getOutgoingEdgeList());
//		System.out.println("Incoming edge: "+this.mIncomingEdge);
		if(this.mIncomingEdge == null) {
			return null;
		}
		if(this.mIncomingEdge.getTail() == null) {
			return null;
		}
		return this.mIncomingEdge.getTail().getLatestValue(var);
	}
	
	public SETEdge getThenEdge() {
		return this.mThenEdge;
	}
	
	public SETEdge getElseEdge() {
		return this.mElseEdge;
	}
	
	public SETEdge setThenEdge(SETEdge edge) {
		this.mThenEdge = edge;
		return edge;
	}
	
	public SETEdge setElseEdge(SETEdge edge) {
		this.mElseEdge = edge;
		return edge;
	}
	
	public IExpression getCondition() {
		return this.mCondition;
	}

	public void setCondition(IExpression value) {
		this.mCondition = value;
	}

	@Override
	public IExpression getPathPredicate() throws Exception {
		if(this.mIncomingEdge == null) {
			return null;
		}
		SETEdge inEdge = this.mIncomingEdge;
		SETNode pred = this.mIncomingEdge.getTail();
		IExpression result = null;
		if(pred instanceof SETDecisionNode) {
			SETDecisionNode pr = (SETDecisionNode)pred;
			if(inEdge.equals(pr.getThenEdge())) {
				result = new AndExpression(this.mSET, pred.getPathPredicate(), pr.getCondition());
			}
			else if(inEdge.equals(pr.getElseEdge())) {
				IExpression and = this.mIncomingEdge.getTail().getPathPredicate();
				IExpression l = and;
				IExpression r = pr.getCondition();
				NotExpression not = new NotExpression(r.getProgram(), r);
				result = new AndExpression(r.getProgram(), l, not);
			}
			else {
				Exception e = new Exception("SETBasicBlockNode.getPathPredicate : incoming edge not an outgoing edge of the predecessor.");
				throw e;
			}
		}
		else if(pred instanceof SETBasicBlockNode) {
			result = pred.getPathPredicate();
		}
		else {
			Exception e = new Exception("SETBasicBlockNode.getPathPredicate : predecessor's node type not handled here.");
			throw e;
		}
		return result;
	}
}
