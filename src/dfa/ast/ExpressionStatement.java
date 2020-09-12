package ast;

import visitor.Visitor;

public class ExpressionStatement extends InstructionStatement {
  public final Expression expression;

  public ExpressionStatement(Expression expression) {
    this.expression = expression;
  }

  public String toString() {
    return (this.expression.toString() + ";\n");
  }

  public void visit (Visitor visitor) throws Exception {
    visitor.visitExpressionStatement(this);
  }
}
