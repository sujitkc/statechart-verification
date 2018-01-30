package ast;

public class BasicType extends Type {

  public BasicType(String name) {
    super(name);
  }

  public String toString() {
    return "basic type '" + this.name + "'\n";
  }
}
