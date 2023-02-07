package set;

import cfg.ICFGBasicBlockNode;
//import expression.*;
import utilities.IdGenerator;
import ast.Expression;
import ast.Name;
import ast.True;
import ast.False;
import ast.BinaryExpression;
import ast.NotExpression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SETBasicBlockNode extends SETNode implements ISETInstructionNode {

  private Map<Name, Expression> mValues = new HashMap<Name, Expression>();
  private SETEdge mOutgoingEdge;
  
  public SETBasicBlockNode(SET set, ICFGBasicBlockNode cfgNode) {
    super(set, cfgNode);
    this.mId = SETBasicBlockNode.generateId();
  }

/*  public SETBasicBlockNode(String id, SET set, ICFGBasicBlockNode cfgNode) throws Exception {
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


/*  public String getId() {
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
  
/*  public Expression addValue(Variable var, Expression val) {
    return this.mValues.put(var, val);
  }
*/
  @Override
  public Expression getLatestValue(Name var) {
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
  
  public void setValue(Name var, Expression val) {
    this.mValues.put(var, val);
  }

  public Map<Name, Expression> getValues() {
    return this.mValues;
  }

  @Override
  public Expression getPathPredicate() throws Exception {
    if(this.getIncomingEdge() == null) {
      return new True(this.mSET);
    }
    SETEdge inEdge = this.mIncomingEdge;
    SETNode pred = this.mIncomingEdge.getTail();
    Expression result = null;
    if(pred instanceof SETDecisionNode) {
      SETDecisionNode pr = (SETDecisionNode)pred;
      if(inEdge.equals(pr.getThenEdge())) {
        result = new BinaryExpression(this.mSET, pred.getPathPredicate(), pr.getCondition(),"and");
        //it was AndExpression
      }
      else if(inEdge.equals(pr.getElseEdge())) {
        Expression and = this.mIncomingEdge.getTail().getPathPredicate();
        Expression l = and;
        Expression r = pr.getCondition();
        NotExpression not = new NotExpression(r.getProgram(), r);
        result = new BinaryExpression(r.getProgram(), l, not,"and");
        //it was AndExpression
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
