package cfg;

import program.IProgram;

import java.util.Set;


public interface ICFG extends IProgram {
	public ICFGBasicBlockNode getStartNode();
	public ICFGBasicBlockNode getStopNode();
	public ICFGBasicBlockNode addBasicBlockNode(ICFGBasicBlockNode node);
	public ICFGBasicBlockNode deleteBasicBlockNode(ICFGBasicBlockNode node);
	public boolean hasBasicBlockNode(ICFGBasicBlockNode node);
	public int getNumberOfBasicBlockNodes();
	public ICFGDecisionNode addDecisionNode(ICFGDecisionNode node);
	public ICFGDecisionNode deleteDecisionNode(ICFGDecisionNode node);
	public boolean hasDecisionNode(ICFGDecisionNode node);
	public int getNumberOfDecisionNodes();
	public boolean hasNode(ICFGNode node);
	public int getNumberOfNodes();
	public ICFEdge addEdge(ICFEdge edge);
	public ICFEdge deleteEdge(ICFEdge edge);
	public boolean hasEdge(ICFEdge edge);
	public int getNumberOfEdges();
	public Set<ICFGNode> getNodeSet();
	public Set<ICFEdge> getEdgeSet();
	public Set<ICFGDecisionNode> getDecisionNodeSet();
	public Set<ICFGBasicBlockNode> getBasicBlockNodeSet();
	public String getId();
}
