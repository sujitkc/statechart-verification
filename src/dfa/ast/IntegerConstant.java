package ast;
import program.IProgram;
import visitors.IExprVisitor;

public class IntegerConstant extends Expression {

  public final int value;
  
 public IntegerConstant(int value, IProgram program) throws Exception {
		super(program, (new IntegerConstant(0)).getType());
		this.value = value;
}
  public IntegerConstant(int value) {
    super(null,null);
    this.value = value;
  }
  public int getValue() {
		return this.value;
	}
  public String toString() {
    return new Integer(this.value).toString();
  }
  @Override
	public void accept(IExprVisitor<?> visitor) {
	}

}
