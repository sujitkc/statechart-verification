package ast;

import java.util.List;

public class Environment {
  private List<Declaration> declarations;
  private Environment next;

  public Environment(List<Declaration> declarations, Environment next) {
    this.declarations = declarations;
    this.next = next;
  }
}
