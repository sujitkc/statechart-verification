package ast;
import program.IProgram;
public class StringLiteral extends Expression {

  public final String value;

  public StringLiteral(IProgram program, Type type){
	super(program,type);
	this.value="";
}

  public StringLiteral(String value) {
    super(null,null);
    this.value = value;
  }

  public String toString() {
    return "\"" + this.value + "\"";
  }
}
