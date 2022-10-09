package ast;

import visitor.Visitor;

public class SkipStatement extends InstructionStatement {
  public String toString() {
    return "skip;\n";
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitSkipStatement(this);
  }
}
