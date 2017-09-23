package ast;

public class Declaration {
  public final String vname;
  public final String type;

  public Declaration(String vname, String type) {
    this.vname = vname;
    this.type  = type;
  }

  public String toString() {
    return this.vname + " : " + this.type + ";";
  }
}
