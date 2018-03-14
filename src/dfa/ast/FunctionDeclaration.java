package ast;

public class FunctionDeclaration {
  public final String name;
  public final TypeName returnTypeName;
  private DeclarationList argumentList;

  private Type returnType;

  public FunctionDeclaration(String name, TypeName returnTypeName, DeclarationList argumentList) {
    this.name           = name;
    this.returnTypeName = returnTypeName;
    this.argumentList   = argumentList;
  }

  public Type getReturnType() {
    return this.returnType;
  }

  public DeclarationList getArgumentList() {
    return this.argumentList;
  }

  public String toString() {
    String s = this.name + "(";
    for(Declaration arg : this.argumentList) {
      s += arg.vname + " : " + arg.typeName + ", ";
    }
    return s + ") : " + this.returnTypeName;
  }

  public void setReturnType(Type type) {
    this.returnType = type;
  }
}
