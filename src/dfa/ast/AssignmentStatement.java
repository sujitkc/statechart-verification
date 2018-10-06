package ast;

public class AssignmentStatement extends Statement {
  public final Name lhs;
  public final Expression rhs;

  public AssignmentStatement(Name lhs, Expression rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }
  public Name getLHS(){
	return this.lhs;
  }
  public Expression getRHS(){
	return this.rhs;
  }
  public String toString() {
    return (this.lhs.toString() + " := " + this.rhs.toString() + ";\n");
  }
}
