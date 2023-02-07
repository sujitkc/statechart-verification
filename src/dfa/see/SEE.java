//package see;
//
//import cfg.ICFEdge;
//import cfg.ICFG;
//import cfg.ICFGBasicBlockNode;
//import cfg.ICFGNode;
//import expression.IExpression;
//import expression.IIdentifier;
//import expression.Type;
//import mycfg.CFGBasicBlockNode;
//import mycfg.CFGDecisionNode;
//import set.*;
//import statement.IStatement;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class SEE {
//
//  private SET mSET;
//
//  /**
//   * Constructor to check whether cfg is null or not
//   */
//
//  public SEE(ICFG cfg) throws Exception {
//    if (cfg != null) {
//      this.mSET = new SET(cfg);
//    } else {
//      throw new Exception("Null CFG");
//    }
//  }
//
//  public SET getSET() {
//    return mSET;
//  }
//
//  /**
//   * The function expands the SET along the specified set of CFG edges
//   *
//   * @param cfgEdges
//   * @throws Exception
//   */
//  public void expandSET(List<ICFEdge> cfgEdges) throws Exception {
//    for (ICFEdge edge : cfgEdges) {
//      /* Shyam added below line */
//      System.out.println(edge);
//      /* Shyam added above line */
//      singlestep(edge);
//    }
//  }
//
//  /**
//   * The function performs the addition of a single node to the SET.
//   * Cases handled:
//   * case 1 : Leaf node - Basic Block Node & New Node - Basic Block Node
//   * case 2 : Leaf node - Basic Block Node & New Node - Decision Node
//   * case 3 : Leaf node - Decision Node & New Node - Basic Block Node (3a. New
//   * node belongs to 'Then' Edge 3b. New node belongs to 'Else' edge)
//   * case 4 : Leaf node - Decision Node & New Node - Decision Node (4a. New node
//   * belongs to 'Then' Edge 4b. New node belongs to 'Else' edge)
//   *
//   * @param edge
//   */
//
//  private void singlestep(ICFEdge edge) throws Exception {
//    boolean valid = false;
//    this.mSET.updateLeafNodeSet();
//    Set<SETNode> newLeafNodes = new HashSet<SETNode>();
//    newLeafNodes = this.mSET.getLeafNodes();
//    // check for null edge
//    if (edge == null) {
//      throw new Exception("Null Edge");
//    }
//    for (SETNode leaf : newLeafNodes) {
//      ICFGNode corrCFGNode = leaf.getCFGNode();
//      List<ICFEdge> outCFEdges = corrCFGNode.getOutgoingEdgeList();
//      if (outCFEdges.contains(edge)) {
//        valid = true;
//        ICFGNode newNode = edge.getHead();
//        // check for dangling edge
//        if (newNode == null) {
//          throw new Exception("Dangling Edge");
//        }
//        SETEdge newSETEdge = new SETEdge(mSET, leaf, null); // why head is null?
//
//        if (leaf instanceof SETBasicBlockNode) {
//          ((SETBasicBlockNode) leaf).setOutgoingEdge(newSETEdge);
//          // case 1
//          if (newNode instanceof CFGBasicBlockNode) {
//            addNewSETBasicBlockNode(newNode, newSETEdge);
//          }
//          // case 2
//          else if (newNode instanceof CFGDecisionNode) {
//            addNewSETDecisionNode(newNode, newSETEdge);
//          }
//        }
//
//        else if (leaf instanceof SETDecisionNode) {
//          CFGDecisionNode corrDecisionNode = (CFGDecisionNode) corrCFGNode;
//          if (edge.equals(corrDecisionNode.getThenEdge())) {
//            // case 3a
//            if (newNode instanceof CFGBasicBlockNode) {
//              addNewSETBasicBlockNode(newNode, newSETEdge);
//              ((SETDecisionNode) leaf).setThenEdge(newSETEdge);
//            }
//            // case 4a
//            else if (newNode instanceof CFGDecisionNode) {
//              addNewSETDecisionNode(newNode, newSETEdge);
//              ((SETDecisionNode) leaf).setThenEdge(newSETEdge);
//            }
//          } else if (edge.equals(corrDecisionNode.getElseEdge())) {
//            // case 3b
//            if (newNode instanceof CFGBasicBlockNode) {
//              addNewSETBasicBlockNode(newNode, newSETEdge);
//              ((SETDecisionNode) leaf).setElseEdge(newSETEdge);
//            }
//            // case 4b
//            else if (newNode instanceof CFGDecisionNode) {
//              addNewSETDecisionNode(newNode, newSETEdge);
//              ((SETDecisionNode) leaf).setElseEdge(newSETEdge);
//            }
//          }
//        }
//      }
//      else {
//        System.out.println("Invalid : edge = " + edge + " node = " + corrCFGNode);
//      }
//    }
//    if (!valid) {
//
//      throw new Exception("New Node not connected  to Leaf ");
//    }
//  }
//
//  public void addNewSETDecisionNode(ICFGNode newNode, SETEdge newSETEdge)
//      throws Exception {
//    CFGDecisionNode decisionNode = (CFGDecisionNode) newNode;
//    SETDecisionNode newSETNode = new SETDecisionNode(
//        decisionNode.getCondition(), mSET, decisionNode);
//    this.mSET.addDecisionNode(newSETNode);
//    newSETEdge.setHead(newSETNode);
//    newSETEdge.setHead(newSETNode);
//
//    newSETNode.setIncomingEdge(newSETEdge);
//    this.mSET.addEdge(newSETEdge);
//    this.computeExpression(newSETNode);
//  }
//
//  public void addNewSETBasicBlockNode(ICFGNode newNode, SETEdge newSETEdge) throws Exception {
//    SETBasicBlockNode newSETNode = new SETBasicBlockNode(mSET,
//        (CFGBasicBlockNode) newNode);
//    this.mSET.addBasicBlockNode(newSETNode);
//    newSETEdge.setHead(newSETNode);
//    newSETNode.setIncomingEdge(newSETEdge);
//    this.mSET.addEdge(newSETEdge);
//    this.computeStatementList(newSETNode);
//  }
//
//  private void computeStatementList(SETBasicBlockNode node) throws Exception {
//    ICFGBasicBlockNode cfgBasicBlockNode = (ICFGBasicBlockNode) node
//        .getCFGNode();
//    IStatement statements = cfgBasicBlockNode.getStatement();
//
//    for (IStatement statement : statements) {
//      SETExpressionVisitor visitor = new SETExpressionVisitor(node,
//          statement.getLHS().getType());
//      IExpression value = null;
//
//      visitor.visit(statement.getRHS());
//      value = visitor.getValue();
//
//      IIdentifier var = statement.getLHS();
//      node.setValue(var, value);
//    }
//  }
//
//  private void computeExpression(SETDecisionNode node) throws Exception {
//    SETExpressionVisitor visitor = new SETExpressionVisitor(node,
//        Type.BOOLEAN);
//    CFGDecisionNode cfgNode = (CFGDecisionNode) node.getCFGNode();
//    if (node.getCondition() == null) {
//      throw new Exception("Null Expression");
//    } else {
//      visitor.visit(cfgNode.getCondition());
//      IExpression value = visitor.getValue();
//      node.setCondition(value);
//    }
//  }
//}
