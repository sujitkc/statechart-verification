package visitors;

import expression.*;

public interface IExpressionVisitor {
	public IExpression visit(ConcreteConstant exp);
	public IExpression visit(False exp);
	public IExpression visit(GreaterThanExpression exp);
	public IExpression visit(True exp);
	public IExpression visit(Variable exp);
	public void push(IExpression lhs);
	public IExpression visit(IExpression exp) throws Exception;
	public IExpression visit(AddExpression exp);
	public IExpression visit(SubExpression exp);
	public IExpression visit(MulExpression exp);
	public IExpression visit(DivExpression exp);
	public IExpression visit(GreaterThanEqualToExpression exp);
	public IExpression visit(LesserThanExpression exp);
	public IExpression visit(LesserThanEqualToExpression exp);
}