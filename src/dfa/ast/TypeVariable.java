package ast;

import java.util.List;

public class TypeVariable extends Type {

  public final int index;
  public TypeVariable(String name, int index) {
    super(name);
    this.index = index;
  }

  public String toString() {
    return "typevar(name=" + this.name + ", index=" + this.index + ")";
  }

  public Type substantiate(List<Type> typeArguments) throws Exception {
    throw new Exception("Substantiate failed. Trying to substantiate a type variable. Not allowed.");
  }

  public Type copy() {
    return new TypeVariable(this.name, this.index);
  }
}
