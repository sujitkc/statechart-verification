package searchsim.cfg;

public class CFGSkipNode extends CFGBasicBlockNode {

  public CFGSkipNode() {
    super();
  }

  public String toString() {
    return this.cfg.name+" : "+ "Skip Node";
  }
  
}
