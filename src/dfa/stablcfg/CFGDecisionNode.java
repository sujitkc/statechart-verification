package stablcfg;

import ast.Expression;
import cfg.*;

public class CFGDecisionNode extends CFGNode implements ICFGDecisionNode {
	Expression condition;
	CFGNode thenNode;
	CFGNode elseNode;
	public CFGDecisionNode(Expression cond){
		this.condition=cond;
	}
	public Expression getCondition(){
		return this.condition;
	}
	public ICFEdge getThenEdge(){
		return null;
	}
	public ICFEdge getElseEdge(){
		return null;
	}
	public ICFGNode getThenSuccessorNode(){
		return null;
	}
	public ICFGNode getElseSuccessorNode(){
		return null;
	}
	public void setThenSuccessorNode(CFGNode node){
		this.thenNode=node;
	}
	public void setElseSuccessorNode(CFGNode node){
		this.elseNode=node;
	}
	public ICFEdge setThenEdge(ICFEdge edge){
		return null;
	}
	public ICFEdge setElseEdge(ICFEdge edge){
		return null;
	}
	public ICFEdge deleteThenEdge(){
		return null;
	}
	public ICFEdge deleteElseEdge(){
		return null;
	}
	
}
