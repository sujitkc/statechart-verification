package cfg;

import ast.Expression;

public interface ICFGDecisionNode extends ICFGNode {

	public Expression getCondition();
	public ICFEdge getThenEdge();
	public ICFEdge getElseEdge();
	public ICFGNode getThenSuccessorNode();
	public ICFGNode getElseSuccessorNode();
	public ICFEdge setThenEdge(ICFEdge edge);
	public ICFEdge setElseEdge(ICFEdge edge);
	public ICFEdge deleteThenEdge();
	public ICFEdge deleteElseEdge();
}
