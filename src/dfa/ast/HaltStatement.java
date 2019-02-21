package ast;

public class HaltStatement extends Statement {
  public HaltStatement() {
  }

  public String toString() {
    return "halt;\n";
  }
}
