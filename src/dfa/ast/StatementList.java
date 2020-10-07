package ast;

import java.util.List;
import java.util.ArrayList;

public class StatementList extends Statement {
  private List<Statement> statements = new ArrayList<Statement>();

  public StatementList() {
  }
  public void makeEmpty(){
	  this.statements.clear();
  }
  public boolean isEmpty(){
	  if(this.statements.size()>0) return false;
	  else return true;
  }
  public StatementList(Statement s) {
    this.statements.add(s);
  }
  
  public void addAll(List<Statement> s){
	  this.statements.addAll(s);
  }
  
  public void add(Statement s) {
	  if(s instanceof StatementList){
		  this.statements.addAll(((StatementList)s).getStatements());
	  }
	  else this.statements.add(s);
  }

  public void add(int i, Statement s) {
    this.statements.add(i, s);
  }

  public List<Statement> getStatements() {
    return this.statements;
  }

  public Statement getFirstStatement() {
    if(this.statements.isEmpty() == false) {
      return this.statements.get(0).getFirstStatement();
    }
    return null;
  }

  public List<Statement> getLastStatements() {
    List<Statement> last = new ArrayList<Statement>();
    if(this.statements.isEmpty() == false) {
      last.addAll(this.statements.get(this.statements.size() - 1).getLastStatements());
    }
    return last;
  }

  public String toString() {
    String s = "\n{\n";
    for(Statement st : this.statements) {
      s += (st.toString() + '\n');
    }
    s += "}\n";
    return s;
  }
}
