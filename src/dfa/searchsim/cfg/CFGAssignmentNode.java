package searchsim.cfg;

import java.util.Set;
import java.util.HashSet;
import ast.*;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
import searchsim.simulator.ActionLanguageInterpreter;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CFGAssignmentNode extends CFGBasicBlockNode {
  public final AssignmentStatement assignment;

  public CFGAssignmentNode(AssignmentStatement assignment) {
    super();
    this.assignment = assignment;
  }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
  @Override
  public Set<Declaration> getReadSet() {
    Set<Declaration> readSet = new HashSet<>();
    // read from RHS
    Set<Declaration> rhsDeps = ActionLanguageInterpreter.getDependentVarSet(assignment.rhs);
    if (rhsDeps != null) {
      readSet.addAll(rhsDeps);
    }
    return readSet;
  }

  @Override
  public Set<Declaration> getWriteSet() {
    Set<Declaration> writeSet = new HashSet<>();
    // write to LHS
    Name lhs = assignment.lhs;
    Declaration decl = lhs.getDeclaration();
    writeSet.add(decl);
    return writeSet;
  }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public String toString() {
    return this.cfg.name+" : "+ "CFGAssignmentNode " + this.assignment.toString();
  }
}
