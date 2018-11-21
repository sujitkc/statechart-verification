package ast;

import java.util.List;
import java.util.ArrayList;
import program.IProgram;
public class Name extends Expression {

  public final List<String> name = new ArrayList<String>();
  private Declaration declaration;
  public List<Expression> variables;
  

  public Name(IProgram prog,Type t){
	super(prog,t);
}
  public Name(String id) {
    super(null,null);
    this.name.add(id);
  }

 public Name(List<String> name) {
    super(null,null);
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
      s += " : " + this.type;
    }
    else {
      s += " : not-assigned";
    }
    return s;
  }

  public void add(String id) {
    this.name.add(id);
  }

  public void add(int i, String id) {
    this.name.add(i, id);
  }

  public void setDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }
  public List<String> getName(){
	return this.name;
  }
  public List<Expression> getVariables(){
	this.variables=new ArrayList<Expression>();
	this.variables.add(this);
	return this.variables;
  }
}
