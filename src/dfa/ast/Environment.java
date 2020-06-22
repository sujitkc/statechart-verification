package ast;

import java.util.List;
import java.util.ArrayList;

public class Environment {
  private DeclarationList declarations;
  private Environment next;

  public static final Environment emptyEnvironment = new Environment(new DeclarationList(), null);

  public Environment(DeclarationList declarations, Environment next) {
    this.declarations = declarations;
    this.next = next;
  }

  public DeclarationList getDeclarations() {
    return this.declarations;
  }

  public void setDeclarations(DeclarationList d) {
    this.declarations = d;
  }


  public Environment getNextEnvironment() {
    return this.next;
  }

  public Declaration lookup(String name) {
    Declaration dec = this.declarations.lookup(name);
    if(dec != null) {
      return dec;
    }
    else {
      if(this.next != null) {
        return this.next.lookup(name);
      }
      else {
        return null;
      }
    }
  }

  public List<Declaration> lookupAll(String name) {

    List<Declaration> decs = null;
    if(this.next != null) {
      decs = this.next.lookupAll(name);
    }
    else {
      decs = new ArrayList<Declaration>();
    }
    Declaration dec = this.declarations.lookup(name);
    if(dec != null) {
      decs.add(0, dec);
    }
    return decs;
  }

  public List<Declaration> getAllDeclarations() {

    List<Declaration> decs = null;
    if(this.next != null) {
      decs = this.next.getAllDeclarations();
    }
    else {
      decs = new ArrayList<Declaration>();
    }
    for(Declaration dec : this.declarations) {
      decs.add(0, dec);
    }
    return decs;
  }

  /*
    Returns a deep copy of the environment, with env as the next 
    of the last environment.
  */
  public Environment copyInclusive(Environment env) {
    if(this.next == null) {
      return new Environment(this.declarations, env);
    }
    else {
      return new Environment(this.declarations, this.next.copyInclusive(env));
    }
  }

  public Environment copy() {
    return this.copyInclusive(null);
  }

  /* Returns a deep copy of the environment, until the passed environment env is met.
     env is excluded.
  */
  public Environment copyExclusive(Environment env) throws Exception {
    if(this.next == null || env.equals(this.next)) {
      return new Environment(this.declarations, null);
    }
    else {
      return new Environment(this.declarations, this.next.copyExclusive(env));
    }
  }

  public String toString() {
    String s = "Env() {";
    s += this.declarations.toString();
    s += "}";
    if(this.next != null) {
      s += this.next.toString();
    }
    return s;
  }
}
