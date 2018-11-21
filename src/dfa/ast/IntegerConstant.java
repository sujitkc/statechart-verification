package ast;
import program.IProgram;
public class IntegerConstant extends Expression {

  public final int value;
  
  public IntegerConstant(IProgram prog,Type t){
	super(prog,t);
	this.value=0;
}
  public IntegerConstant(int value) {
    super(null,null);
    this.value = value;
  }

  public String toString() {
    return new Integer(this.value).toString();
  }
  
}
