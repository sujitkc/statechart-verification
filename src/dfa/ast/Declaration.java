package ast;

public class Declaration {
  public final String vname;
  public final TypeName typeName;
  private DeclarationList declarationList;
  public final boolean input; // whether the declaration is input variable or not.
  public Type type; // to be set only when the Declaration is being substantiated.

  public Declaration(String vname, TypeName typeName, boolean input) {
    this.vname    = vname;
    this.typeName = typeName;
    this.input    = input;
  }

  public State getState() {
    return this.declarationList.getState();
  }

  public String getFullVName() {
    return this.getState().getFullName() + '.' + this.vname;
  }

  public Type getType() {
    return this.type;
  }

  public String toString() {
    String s = this.vname;
    if(this.input) {
      s += " #";
    }
    s += " : " + this.typeName + " : " + this.type + ";";
    return s;
  }

  public void setType(Type type) {
    this.type = type;
  }
  
  public void setDeclarationList(DeclarationList declarationList) {
    this.declarationList = declarationList;
  }
}
