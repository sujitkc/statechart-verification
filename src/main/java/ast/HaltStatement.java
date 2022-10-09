package ast;

import visitor.Visitor;

public class HaltStatement extends InstructionStatement {
  public HaltStatement() {
  }

  public String toString() {
    return "halt;\n";
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitHaltStatement(this);
  }
}
