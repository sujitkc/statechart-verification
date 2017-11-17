package ast;

import java.util.List;

public class GuardStmt{

  public Name e;
  public Name n1, n2; 
  public String o,id1,id2;
  public GuardStmt(Name n1, Name n2) {
  	this.n1 = n1;
  	this.n2 = n2;
  }

  public GuardStmt(Name e){
  	this.e = e;
  }
  public GuardStmt(Name n1, String o,Name n2) {
    this.e = e;
    this.n1 = n1;
    this.n2 = n2;
    this.o = o;
  }
  public GuardStmt(Name n1, Name n2,String id1,String id2) {
    this.n1 = n1;
    this.n2 = n2;
    this.id1 = id1;
    this.id2 = id2;
  }
  public GuardStmt(Name n1, Name n2,String id1,String id2,String o) {
    this.n1 = n1;
    this.n2 = n2;
    this.id1 = id1;
    this.id2 = id2;
    this.o = o;
  }
  public GuardStmt(String id1,Name n1, String id2,Name n2,String o) {
    this.n1 = n1;
    this.n2 = n2;
    this.id1 = id1;
    this.id2 = id2;
    this.o = o;
  }
  public GuardStmt(String id1,Name n1, String id2,Name n2) {
    this.n1 = n1;
    this.n2 = n2;
    this.id1 = id1;
    this.id2 = id2;
  }

}
