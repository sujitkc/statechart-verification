package simulator2.cfg;

public class CFG {
  public final CFGNode entryNode;
  public final CFGBasicBlockNode exitNode;
  public final String name;
 // List<CFG> cfgs;
  public CFG(String name, CFGNode entryNode, CFGBasicBlockNode exitNode) {
    this.entryNode = entryNode;
    this.exitNode  = exitNode;
    //this.cfgs
    this.entryNode.setCFG(this);
    this.exitNode.setCFG(this);
    this.name=name;
  }

  public String toString() {
    return "CFG "+this.name+"(\nentry: " + this.entryNode.toString() + "\nexit: " + this.exitNode.toString() + ")";
  }
}
