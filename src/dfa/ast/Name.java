package ast;

import java.util.List;
import java.util.ArrayList;

public class Name extends Expression{
  protected Type type;
  public final List<String> name = new ArrayList<String>();
  private Declaration declaration;
  public static List<UnaryExpr> uni = new ArrayList<UnaryExpr>();
  public static List<BinaryExpr> bi = new ArrayList<BinaryExpr>();
  

  public Name(String id) {
    this.name.add(id);
  }

  public Name(List<String> name) {
    for(String id : name) {
      this.name.add(id);
    }
  }


  public Declaration getDeclaration() {
    return this.declaration;
  }

  public String toString() {
    String s = this.name.get(0);
    for(int i = 1; i < this.name.size(); i++) {
      s += "." + this.name.get(i);
    }
    if(this.type != null) {  
      s += " : " + this.type.name;
    }
    else {
      s += " : not-assigned";
    }
    return s;
  }
  /*
  public String toString() {
    String s = this.name.get(0);
    String s1 = this.name.get(1);
    for(int i = 1; i < this.name.size(); i++) {
      s += "." + this.name.get(i);
    }
    for(int i = 1; i < this.name.size(); i++) {
      s1 += "." + this.name.get(i);
    }
    return (s+ " "+s1);
  }

*/
 /* public Name(String n1,Symbol s,String n2) {
    this.name.add(n1);
    this.name.add(n2);
  }
*/
  public void add(String id) {
    this.name.add(id);
  }

  public void add(int i, String id) {
    this.name.add(i, id);
  }
  public void setDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }
  public static void add(UnaryExpr u){
    uni.add(u);
  }
  public static void add(BinaryExpr u){
    bi.add(u);
  }

/*
  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }
*/

}
