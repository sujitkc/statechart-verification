package simulator2.cfg;

public class CFGBasicBlockNode extends CFGNode {

  protected CFGNode successor = null;

  public CFGBasicBlockNode() {}

  public void setSuccessor(CFGNode successor) {
    this.successor = successor;
    this.successor.addPredecessor(this);
  }

  public CFGNode getSuccessor() {
    return this.successor;
  }
}
