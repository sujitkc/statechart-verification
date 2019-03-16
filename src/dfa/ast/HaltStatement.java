package ast;

public class HaltStatement extends InstructionStatement {
  public HaltStatement() {
  }

  public String toString() {
    return "halt;\n";
  }
}
