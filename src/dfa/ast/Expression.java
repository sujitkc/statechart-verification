package ast;
import java.util.*;
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
