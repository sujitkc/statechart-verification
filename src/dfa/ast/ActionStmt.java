package ast;

import java.util.List;

public class ActionStmt{

  public Statement s;
  public String classnm, funcname,id; 
  public Expression arg2,arg1;
  public Name n1,n2;
  public ActionStmt(String classnm, String funcname, Expression arg1, Expression arg2) {
  	this.classnm = classnm;
  	this.funcname = funcname;
  	this.arg1 = arg1;
  	this.arg2 = arg2;
  }

  public ActionStmt(Statement s){
  	this.s = s;
  }
  public ActionStmt(String id, Name n1, Name n2) {
    this.id = id;
    this.n1 = n2;
    this.n2 = n2;
  }

}
