package visitor;

import ast.*;

public interface ExpressionVisitor {
	public void visitBinaryExpression(BinaryExpression expr) throws Exception;
	public void visitBooleanConstant(BooleanConstant expr) throws Exception; 
	public void visitFunctionCall(FunctionCall expr) throws Exception;
	public void visitIntegerConstant(IntegerConstant expr) throws Exception;
	public void visitName(Name expr) throws Exception;
	public void visitStringLiteral(StringLiteral expr) throws Exception;
	public void visitUnaryExpression(UnaryExpression expr) throws Exception;
}
