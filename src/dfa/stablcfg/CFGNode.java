package stablcfg;


import java.util.List;
import cfg.*;
import ast.Statement;

public class CFGNode implements ICFGNode {

	public boolean isIncomingEdge(ICFEdge edge){
		return false;
	}
	public boolean isOutgoingEdge(ICFGNode node){
		return false;
	}
	public boolean isCFSuccessor(ICFGNode node){
		return false;
	}
	public ICFG getCFG(){	
		return null;
	}
	public void setCFG(ICFG graph){}	
	public List<ICFEdge> getIncomingEdgeList(){
		return null;
	}
	public List<ICFEdge> getOutgoingEdgeList(){
		return null;
	}
	public List<ICFGNode> getCFPredecessorNodeList(){
		return null;
	}
	public List<ICFGNode> getCFSuccessorNodeList(){
			return null;
	}
	public ICFEdge addIncomingEdge(ICFEdge edge){
		return null;
	}
	public ICFEdge deleteIncomingEdge(ICFEdge edge){
		return null;
	}
	public ICFEdge addOutgoingEdge(ICFEdge edge){
		return null;
	}
	public ICFEdge deleteOutgoingEdge(ICFEdge edge){
		return null;
	}
	public String getId(){
		return null;
	}
	
}
