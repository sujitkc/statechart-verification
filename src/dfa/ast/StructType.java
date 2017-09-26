package ast;

import java.util.List;

public class StructType extends Type {

  public final List<Declaration> declarations;
  public StructType(String name, List<Declaration> declarations) {
    super(name);
    this.declarations = declarations;
  }
}
