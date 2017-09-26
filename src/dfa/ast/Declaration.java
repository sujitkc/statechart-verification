package ast;

public class Declaration {
  public final String vname;
  public final String typeName;
  private Type type;

  public Declaration(String vname, String typeName) {
    this.vname = vname;
    this.typeName  = typeName;
  }

  public String toString() {
    return this.vname + " : " + this.typeName + ";";
  }

  public void setType(Type type) {
    this.type = type;
  }
}
