package ast;

import java.util.List;
import java.util.ArrayList;

public class StatementList extends Statement {
  private List<Statement> statements = new ArrayList<Statement>();

  public StatementList() {
  }

  public StatementList(Statement s) {
    this.statements.add(s);
  }

  public void add(Statement s) {
    this.statements.add(s);
  }

  public void add(int i, Statement s) {
    this.statements.add(i, s);
  }
}
