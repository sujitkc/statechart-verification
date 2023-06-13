package ast;

import java.util.List;

import visitor.Visitor;

import java.util.ArrayList;

public class Name extends Expression {

  public final List<String> name = new ArrayList<String>();
  private Declaration declaration;

  public Name(String id) {
    this.name.add(id);
  }

  public Name(List<String> name) {
    for(String id : name) {
      this.name.add(id);
    }
  }

  public Declaration getDeclaration() {
    return this.declaration;
  }

  public String toString() {
    String s = this.name.get(0);
    for(int i = 1; i < this.name.size(); i++) {
      s += "." + this.name.get(i);
    }
    return s;
  }

  public void add(String id) {
    this.name.add(id);
  }

  public void add(int i, String id) {
    this.name.add(i, id);
  }

  public void setDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }

  public boolean equals(Object o) {
  System.out.println("names equals::");
    boolean result = true;
    Name n = null;
    if((o instanceof Name) == false) {
      return false;
    }
    else {
      n = (Name)o;
      if(this.name.size() != n.name.size()) {
        return false;
      }
      else {
        for(int i = 0; i < this.name.size(); i++) {
          if(this.name.get(i).equals(n.name.get(i)) == false) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitName(this);
  }
}
