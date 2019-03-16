package stablcfg;

import java.util.List;
import java.util.ArrayList;

import ast.Statement;
public class CFGBasicBlockNode extends CFGNode { // implements ICFGBasicBlockNode {
  public final Statement statement;
  private CFEdge outgoingEdge = null;

  public CFGBasicBlockNode(Statement stmt){
    this.statement = stmt;
  }

  public boolean isOutgoingEdge(CFEdge edge) {
    return this.outgoingEdge.equals(edge);
  }

  public CFEdge getOutgoingEdge() {
    return this.outgoingEdge;
  }

  public CFGNode getSuccessorNode() {
    if (this.outgoingEdge == null) {
      return null;
    }
    return this.outgoingEdge.getHead();
  }

  public boolean isCFPredecessor(CFGNode node) {
    for (CFEdge e : this.incomingEdges) {
      if (e.getTail().equals(node)) {
        return true;
      }
    }
    return false;
  }

  public boolean isCFSuccessor(CFGNode node) {
    if (this.outgoingEdge == null) {
      return false;
    }
    return this.outgoingEdge.getHead().equals(node);
  }

  public List<CFGNode> getCFPredecessorNodeList() {
    List<CFGNode> list = new ArrayList<CFGNode>();
    for (CFEdge e : this.incomingEdges) {
      list.add(e.getTail());
    }
    return list;
  }

  public CFEdge setOutgoingEdge(CFEdge edge) {
    this.outgoingEdge = edge;
    return edge;
  }

  public CFEdge addOutgoingEdge(CFEdge edge) {
    if (this.outgoingEdge != null) {
      return null;
    }
    return this.setOutgoingEdge(edge);
  }

  public List<CFGNode> getCFSuccessorNodeList() {
    List<CFGNode> list = new ArrayList<CFGNode>();
    if (this.outgoingEdge != null) {
      if (this.outgoingEdge.getHead() != null) {
        list.add(this.outgoingEdge.getHead());
      }
    }
    return list;
  }

  public List<CFEdge> getOutgoingEdgeList() {
    List<CFEdge> list = new ArrayList<CFEdge>();
    if (this.outgoingEdge != null) {
      list.add(this.outgoingEdge);
    }
    return list;
  }
}
