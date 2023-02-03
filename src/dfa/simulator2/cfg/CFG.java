package simulator2.cfg;

public class CFG {
  public final CFGNode entryNode;
  public final CFGBasicBlockNode exitNode;

  public CFG(CFGNode entryNode, CFGBasicBlockNode exitNode) {
    this.entryNode = entryNode;
    this.exitNode  = exitNode;
    this.entryNode.setCFG(this);
    this.exitNode.setCFG(this);
  }

  public String toString() {
    return "CFG (\nentry: " + this.entryNode.toString() + "\nexit: " + this.exitNode.toString() + ")";
  }
}
