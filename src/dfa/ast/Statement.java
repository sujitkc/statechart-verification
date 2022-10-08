package ast;

import java.util.List;

import visitor.Visitor;

public abstract class Statement {

  public abstract Statement getFirstStatement();
  public abstract List<Statement> getLastStatements();

  public void visit (Visitor visitor) throws Exception {
    System.out.println (this.toString());
    this.visit(visitor);
  }
}
