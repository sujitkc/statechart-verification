package stablcfg;

import ast.Statement;
import cfg.*;
public class CFGBasicBlockNode  extends CFGNode implements ICFGBasicBlockNode {
	Statement stmt;
	CFGNode next;
	public CFGBasicBlockNode(Statement stmt){
		this.stmt=stmt;
		
	}
	public void setSuccessorNode(CFGNode node){
		this.next=node;
	}
	public Statement getStatement(){
		return stmt;
	}
	public void setStatement(Statement stmt){
		this.stmt=stmt;
	}
	public ICFEdge getOutgoingEdge(){
		return null;
	}
	public ICFGNode getSuccessorNode(){
		return null;
	}
	public ICFEdge setOutgoingEdge(ICFEdge edge){
		return null;
	}
	
}
