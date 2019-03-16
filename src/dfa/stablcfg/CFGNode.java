package stablcfg;

import java.util.List;
import java.util.ArrayList;
import java.util.List;

import ast.Statement;
import utilities.IdGenerator;

public class CFGNode {

  public final String name;
  protected List<CFEdge> incomingEdges = new ArrayList<CFEdge>();
//  public final List<CFEdge> outgoingEdgeList = new ArrayList<CFEdge>();
  

  public CFGNode() {
    this.name = IdGenerator.generateId("node");
  }

  public boolean isIncomingEdge(CFEdge edge) {
    return this.incomingEdges.contains(edge);
  }

  public List<CFEdge> getIncomingEdgeList() {
    return this.incomingEdges;
  }

  public boolean isCFPredecessor(CFGNode node) {
    for(CFEdge e : this.incomingEdges) {
      if(e.getTail().equals(node)) {
        return true;
      }
    }
    return false;
  }

  public List<CFGNode> getCFPredecessorNodeList() {
    List<CFGNode> list = new ArrayList<CFGNode>();
    for(CFEdge e : this.incomingEdges) {
      list.add(e.getTail());
    }
    return list;
  }

  public CFEdge addIncomingEdge(CFEdge edge) {
    if(this.incomingEdges.add(edge)) {
      return edge;
    }
    return null;
  }
}
