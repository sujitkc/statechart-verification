package stablcfg;

import java.util.Set;

import program.IProgram;
import ast.Environment;
import java.util.Queue;
import ast.Expression;
import ast.Name;
import java.util.List;
import cfg.*;

public class CFG implements ICFG {
	public ICFGBasicBlockNode getStartNode(){
		return null;
	}
	public ICFGBasicBlockNode addBasicBlockNode(ICFGBasicBlockNode node){
		return null;
	}
	public void deleteCFGNode(ICFGNode node){}
	public boolean hasBasicBlockNode(ICFGBasicBlockNode node){
		return false;
	}
	public int getNumberOfBasicBlockNodes(){
		return 0;
	}
	public ICFGDecisionNode addDecisionNode(ICFGDecisionNode node){
		return null;
	}
	public boolean hasDecisionNode(ICFGDecisionNode node){
		return false;
	}
	public int getNumberOfDecisionNodes(){
		return 0;
	}
	public boolean hasNode(ICFGNode node){
		return false;
	}
	public int getNumberOfNodes(){
		return 0;
	}
	public ICFEdge addEdge(ICFEdge edge){
		return null;
	}
	public ICFEdge deleteEdge(ICFEdge edge){
		return null;
	}
	public boolean hasEdge(ICFEdge edge){
		return false;
	}
	public int getNumberOfEdges(){
		return 0;
	}
	public Set<ICFGNode> getNodeSet(){
		return null;
	}
	public Set<ICFEdge> getEdgeSet(){
		return null;
	}
	public Set<ICFGDecisionNode> getDecisionNodeSet(){
		return null;
	}
	public Set<ICFGBasicBlockNode> getBasicBlockNodeSet(){
		return null;
	}
	public String getId(){
		return null;
	}
	public Name addVariable(Name var){
		return null;
	}
	public Set<Name> getVariables(){
		return null;
	}
	public boolean hasVariable(Name var){
		return false;
	}
}
