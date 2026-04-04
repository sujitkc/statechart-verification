package simulator2.cfg;

import ast.*;

public class CFGAssignmentNode extends CFGBasicBlockNode {
  public final AssignmentStatement assignment;

  public CFGAssignmentNode(AssignmentStatement assignment) {
    super();
    this.assignment = assignment;
  }

  public String toString() {
    return this.cfg.name+" : "+ "CFGAssignmentNode " + this.assignment.toString();
  }
}
