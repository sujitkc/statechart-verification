package stablcfg;

import cfg.*;
public class CFEdge implements ICFEdge {
	public ICFGNode getHead(){ //target node
		return null;
	} 
	public ICFGNode getTail(){ //source node
		return null;
	} 
	public void setHead(ICFGNode node){ //target node
	
	} 
	public void setTail(ICFGNode node){ //source node
	
	} 
	public ICFG getCFG(){
		return null;
	}
	public void setCFG(ICFG graph){}
	public void setEdgeAnnotation(boolean bool){}
	public boolean getEdgeAnnotation(){
		return false;
	}
	public String getId(){
		return null;
	}
}
