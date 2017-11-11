package ast;

public class Declaration {
  public final String vname;
  public final String typeName;
  public   String hash="";
  private DeclarationList declarationList;
  private Type type;

  public Declaration(String vname, String hash, String typeName) {
    this.vname           = vname;
    this.typeName        = typeName;
    this.hash            = hash;
  }

   public Declaration(String vname, String typeName) {
    this.vname           = vname;
    this.typeName        = typeName;
  }

  public State getState() {
    return this.declarationList.getState();
  }

  public String getFullVName() {
    return this.getState().getFullName() + this.vname;
  }

  public Type getType() {
    return this.type;
  }

  public String toString() {
    if(hash==""){
      return this.vname + " : " + this.typeName + ";";
    }
    else{
      return this.vname + " # : " + this.typeName + ";"; 
    }
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setDeclarationList(DeclarationList declarationList) {
    this.declarationList = declarationList;
  }
}
