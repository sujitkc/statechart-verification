package simulator2.cfg;

import java.util.Set;
import java.util.HashSet;

public abstract class CFGNode {
  protected CFG cfg;

  protected Set<CFGNode> predecessors = new HashSet<>();

  public CFGNode(CFG cfg) {
    this.cfg = cfg;
  }

  public CFGNode() {
    this.cfg = null;
  }

  public void setCFG(CFG cfg) {
    this.setCFG(cfg, new HashSet<CFGNode>());
  }

  protected abstract void setCFG(CFG cfg, Set<CFGNode> added);

  public CFG getCFG() {
    return this.cfg;
  }

  public void addPredecessor(CFGNode p) {
    this.predecessors.add(p);
  }

  
}
