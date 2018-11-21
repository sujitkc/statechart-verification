package ast;
import program.IProgram;
public class BooleanConstant extends Expression {

  public final boolean value;
  public BooleanConstant(IProgram prog,Type t){
	super(prog,t);
	this.value=false;
   }
  public BooleanConstant(boolean value) {
    super(null,null);
    this.value = value;
  }

  public String toString() {
    if(this.value) {
      return "true";
    } else {
      return "false";
    }
  }
}
