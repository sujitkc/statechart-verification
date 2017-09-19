package ast;

public class AssignmentStatement extends Statement {
  private final Name lhs;
  private final Expression rhs;

  public AssignmentStatement(Name lhs, Expression rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public String toString() {
    return this.lhs.toString() + " = " + this.rhs.toString();
  }
}
