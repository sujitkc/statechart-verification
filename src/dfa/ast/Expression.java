package ast;
import java.util.*;
import program.IProgram;
public abstract class Expression {
/*
  public enum Operator {
    ADD,
    MUL,
    SUB,
    DIV,
    GE,
    GT,
    LE,
    LT,
    EQ,
    NE,
    AND,
    OR,
    UMIN,
    NOT
  };
*/

  protected Type type;

protected IProgram mProgram = null;

	public Expression(IProgram program, Type type) {
		this.mProgram = program;
		this.type = type;
		/*if(!Type.hasType(type)) {
			Exception e = new Exception("Expression : Type " + type + " not found.");
			throw e;
		}*/
	}
	
	public IProgram getProgram() {
		return this.mProgram;
	}

	
	public void setProgram(IProgram program) {
		this.mProgram = program;
	}

	
  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }
  public List<Expression> getVariables(){
	return null;
  }
}
