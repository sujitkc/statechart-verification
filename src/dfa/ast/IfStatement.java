package ast;

public class IfStatement extends Statement {
  public final Expression condition;
  public final Statement then_body;
  public final Statement else_body;

  public IfStatement(Expression condition, Statement then_body, Statement else_body) {
    this.condition = condition;
    this.then_body = then_body;
    this.else_body = else_body;
  }

  public IfStatement(Expression condition, Statement then_body){
    this.condition = condition;
    this.then_body = then_body;
    this.else_body = null;
  }

  public String toString() {
    if(this.else_body==null)
      return "\nif(" + this.condition + ")" + this.then_body + "\n";
    else
      return "\nif(" + this.condition + ")" + this.then_body + "\nelse\n" + this.else_body;

  }
}
