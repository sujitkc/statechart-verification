package set;

import cfg.ICFGDecisionNode;
import ast.BinaryExpression;
import ast.Expression;
import ast.Name;
import ast.NotExpression;
import utilities.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class SETDecisionNode extends SETNode {

  private SETEdge mThenEdge;
  private SETEdge mElseEdge;
  private Expression mCondition;
  
  public SETDecisionNode(Expression c, SET set, ICFGDecisionNode cfgNode) {
    super(set, cfgNode);
    this.mCondition = c;
    this.mId = SETDecisionNode.generateId();
  }

  public SETDecisionNode(String id, Expression c, SET set, ICFGDecisionNode cfgNode) throws Exception {
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
  public Expression getLatestValue(Name var) {
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
  
  public Expression getCondition() {
    return this.mCondition;
  }

  public void setCondition(Expression value) {
    this.mCondition = value;
  }

  @Override
  public Expression getPathPredicate() throws Exception {
    if(this.mIncomingEdge == null) {
      return null;
    }
    SETEdge inEdge = this.mIncomingEdge;
    SETNode pred = this.mIncomingEdge.getTail();
    Expression result = null;
    if(pred instanceof SETDecisionNode) {
      SETDecisionNode pr = (SETDecisionNode)pred;
      if(inEdge.equals(pr.getThenEdge())) {
        //result = new AndExpression(this.mSET, pred.getPathPredicate(), pr.getCondition());
        result = new BinaryExpression(this.mSET, pred.getPathPredicate(), pr.getCondition(),"&");
      }
      else if(inEdge.equals(pr.getElseEdge())) {
        Expression and = this.mIncomingEdge.getTail().getPathPredicate();
        Expression l = and;
        Expression r = pr.getCondition();
        NotExpression not = new NotExpression(r.getProgram(), r);
        //result = new AndExpression(r.getProgram(), l, not);
        result = new BinaryExpression(r.getProgram(), l, not,"&");
        
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
