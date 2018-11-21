package cfg;

import expression.IExpression;
import program.IDecisionNode;

public interface ICFGDecisionNode extends ICFGNode, IDecisionNode {

	public IExpression getCondition();
	public ICFEdge getThenEdge();
	public ICFEdge getElseEdge();
	public ICFGNode getThenSuccessorNode();
	public ICFGNode getElseSuccessorNode();
	public ICFEdge setThenEdge(ICFEdge edge);
	public ICFEdge setElseEdge(ICFEdge edge);
	public ICFEdge deleteThenEdge();
	public ICFEdge deleteElseEdge();
}
