package set;

import cfg.ICFGBasicBlockNode;
import expression.*;
import utilities.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SETBasicBlockNode extends SETNode {

	private Map<IIdentifier, IExpression> mValues = new HashMap<IIdentifier, IExpression>();
	private SETEdge mOutgoingEdge;
	
	public SETBasicBlockNode(SET set, ICFGBasicBlockNode cfgNode) {
		super(set, cfgNode);
		this.mId = SETBasicBlockNode.generateId();
	}

/*	public SETBasicBlockNode(String id, SET set, ICFGBasicBlockNode cfgNode) throws Exception {
		super(set, cfgNode);
		
		if(IdGenerator.hasId(id)) {
			Exception e = new Exception("Can't construct SETBasicBlockNode : something with name '" + id + "' already exists.");
			throw e;			
		}
		IdGenerator.addId(id);
		this.mId = id;

	}		
*/

	private static String generateId() {
		return IdGenerator.generateId("SETBasicBlockNode");
	}


/*	public String getId() {
		return this.mId;
	}
*/
	@Override
	public List<SETNode> getSuccessorSETNodeList() {
		List<SETNode> list = new ArrayList<SETNode>();
		if(this.mOutgoingEdge != null) {
			if(this.mOutgoingEdge.getHead() != null) {
				list.add(this.mOutgoingEdge.getHead());
			}
		}
		return list;
	}

	@Override
	public List<SETEdge> getOutgoingEdgeList() {
		List<SETEdge> list = new ArrayList<SETEdge>();
		if(this.mOutgoingEdge != null) {
			list.add(this.mOutgoingEdge);
		}
		return list;
	}
	
/*	public IExpression addValue(Variable var, IExpression val) {
		return this.mValues.put(var, val);
	}
*/
	@Override
	public IExpression getLatestValue(IIdentifier var) {
		if(this.mValues.keySet().contains(var)) {
			return this.mValues.get(var);
		}
		if(this.mIncomingEdge == null) {
			return null;
		}
		if(this.mIncomingEdge.getTail() == null) {
			return null;
		}
		return this.mIncomingEdge.getTail().getLatestValue(var);
	}
	
	public SETEdge setOutgoingEdge(SETEdge edge) {
		this.mOutgoingEdge = edge;
		return edge;
	}
	
	public void setValue(IIdentifier var, IExpression val) {
		this.mValues.put(var, val);
	}

	public Map<IIdentifier, IExpression> getValues() {
		return this.mValues;
	}

	@Override
	public IExpression getPathPredicate() throws Exception {
		if(this.getIncomingEdge() == null) {
			return new True(this.mSET);
		}
		SETEdge inEdge = this.mIncomingEdge;
		System.out.println("getPathPredicate node SET : " + this.getId() + " CFGId :  " + this.getCFGNode().getId() );
		//System.out.println("inEdgeTail:"+inEdge.getTail().getCFGNode().getId());
		//System.out.println("inEdgeHead:"+inEdge.getHead().getCFGNode().getId());
		SETNode pred = this.mIncomingEdge.getTail();
		System.out.println("getPathPredicate pred SET : " + pred.getId() + " CFG :  " + pred.getCFGNode().getId());
		IExpression result = null;
		if(pred instanceof SETDecisionNode) {
			SETDecisionNode pr = (SETDecisionNode)pred;
			//System.out.println("decision node found :" + pred);
//			System.out.println("then edge of node : " + pr.getId() + " is : " + pr.getThenEdge() );
//			System.out.println("else edge : " + pr.getElseEdge());
			if(inEdge.equals(pr.getThenEdge())) {
				//System.out.println("then edge");
				result = new AndExpression(this.mSET, pr.getPathPredicate(), pr.getCondition());
			}
			else if(inEdge.equals(pr.getElseEdge())) {
//				System.out.println("else edge");
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
			//System.out.println("basic node found:" + pred);
			result = pred.getPathPredicate();
		}
		else {
			Exception e = new Exception("SETDecisionBlockNode.getPathPredicate : predecessor's node type not handled here.");
			throw e;
		}
		return result;
	}
}
