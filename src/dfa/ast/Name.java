package ast;

import java.util.List;
import java.util.ArrayList;

public class Name implements Expression {

  public final List<String> name = new ArrayList<String>();

  public Name(String id) {
    this.name.add(id);
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
}
