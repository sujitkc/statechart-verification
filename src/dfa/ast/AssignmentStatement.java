package ast;

import java.util.List;

import visitor.Visitor;

import java.util.ArrayList;

public class AssignmentStatement extends InstructionStatement {
  public final Name lhs;
  public final Expression rhs;

  public AssignmentStatement(Name lhs, Expression rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public String toString() {
    return (this.lhs.toString() + " := " + this.rhs.toString() + ";\n");
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitAssignmentStatement(this);
  }
}
