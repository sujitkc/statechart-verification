package simulator2.cfg;

import java.util.Set;

public class CFGBasicBlockNode extends CFGNode {

  protected CFGNode successor = null;

  public CFGBasicBlockNode() {}

  protected void setCFG(CFG cfg, Set<CFGNode> added) {
    if(added.contains(this) == false) {
      this.cfg = cfg;
      added.add(this);
      if(this.successor != null) {
        this.successor.setCFG(cfg, added);
      }
    }
  }

  public void setSuccessor(CFGNode successor) {
    this.successor = successor;
    this.successor.addPredecessor(this);
  }

  public CFGNode getSuccessor() {
    return this.successor;
  }
}
