package ast;

public class Struct extends Type {
  public final DeclarationList declarations;

  public Struct(String name, DeclarationList declarations) {
    super(name);
    this.declarations = declarations;
  }

  public String toString() {
    String s = "struct " + this.name + "{\n";
    s += this.declarations.toString();
    s += "}\n";
    return s;
  }
}
