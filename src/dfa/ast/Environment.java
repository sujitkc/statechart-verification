package ast;

import java.util.List;
import java.util.ArrayList;

public class Environment {
  private List<Declaration> declarations;
  private Environment next;

  public Environment(List<Declaration> declarations, Environment next) {
    this.declarations = declarations;
    this.next = next;
  }

  /*
    Returns a deep copy of the environment, with env as the next 
    of the last environment.
  */
  public Environment copy(Environment env) {
    List<Declaration> decs = new ArrayList<Declaration>();
    if(this.next == null) {
      return new Environment(decs, env);
    }
    else {
      return new Environment(decs, this.next.copy(env));
    }
  }

  public Environment copy() {
    return this.copy(null);
  }
}
