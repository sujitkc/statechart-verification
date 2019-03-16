package ast;

import java.util.List;

public abstract class Statement {

  public abstract Statement getFirstStatement();
  public abstract List<Statement> getLastStatements();
}
