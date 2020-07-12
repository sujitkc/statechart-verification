package ast;

public class Declaration {
  public final String vname;
  public final ScopeName scope;
  public final TypeName typeName;
  private DeclarationList declarationList;
  public final boolean input; // whether the declaration is input variable or not.
  public Type type; // to be set only when the Declaration is being substantiated.

  // this is only their for the time-being - any variable with a scope not explicitly mentioned defaults to a parameter variable
  public Declaration(String vname, TypeName typeName, boolean input) {
    this.vname    = vname;
    this.scope    = new ScopeName("parameter");
    this.typeName = typeName;
    this.input    = input;
  }

  public Declaration(String vname, ScopeName scope, TypeName typeName, boolean input) {
    this.vname    = vname;
    this.scope    = scope;
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

  public boolean isInput(){
    return this.input;
  }

  public ScopeName getScope() {
    return this.scope;
  }

  public String toString() {
    String s = this.vname;
    if(this.input) {
      s += " #";
    }
    s += " : " + this.typeName + " : " + this.type + " Scope : " + this.scope + " Input status: " + this.input + ";"; 
    return s;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setDeclarationList(DeclarationList declarationList) {
    this.declarationList = declarationList;
  }
}
