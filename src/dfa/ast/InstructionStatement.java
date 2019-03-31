package ast;

import java.util.List;
import java.util.ArrayList;

public abstract class InstructionStatement extends Statement {

  public final Statement getFirstStatement() {
    return this;
  }

  public final List<Statement> getLastStatements() {
    List<Statement> last = new ArrayList<Statement>();
    last.add(this);
    return last;
  }
}
