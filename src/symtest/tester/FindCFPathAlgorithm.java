package tester;

import graph.IEdge;
import graph.IGraph;
import graph.INode;
import graph.IPath;
import mygraph.Path;

import java.util.*;

public class FindCFPathAlgorithm {

	private IGraph mGraph;        // the graph over which the path computation is done. This is a digraph
		// with an acyclic core.
	private Set<IEdge> mTargets;  // the set of edges to be covered
	private INode mTargetNode;    // the target/destination node of the graph
	private INode mLoopStartNode; // the starting node of the main loop.
	private IPath path;
	/**
	 * Initializes the algorithm with the given parameters
	 * Validates the targets
	 * Initialises the loop start node.
	 * @param graph
	 * @param targets
	 * @param terminalNode
	 * @throws Exception
	 */
	public FindCFPathAlgorithm(IGraph graph, Set<IEdge> targets, INode terminalNode) throws Exception {
		
		for(IEdge edge : targets) {
			if(!graph.hasEdge(edge)) {
				Exception e = new Exception("Can't construct FinCFPathAlgorithm: Not all members in targets set are contained in the graph.");
				throw e;
			}
		}
		if(!graph.hasNode(terminalNode)) {
			Exception e = new Exception("Can't construct FinCFPathAlgorithm: terminal node not contained in the graph.");
			throw e;
		}
		this.mTable = null;
		this.mGraph = graph;
		this.mTargets = targets;
		this.mTargetNode = terminalNode;
		List<INode> c = this.mTargetNode.getChildList();
		if(c.size() != 1 && c.size() != 0) {
			Exception e = new Exception("Can't construct FinCFPathAlgorithm: t node should have less than or equal to 1 child. t has " + c.size() + " children.");
			throw e;
		}
		
		//Initialises Loop start node
		if(c.size() == 1) {
			this.mLoopStartNode = c.get(0);
		}
		//for cfg of the format start-> Tnode without any loops.
		if(c.size() == 0) {
			this.mLoopStartNode = this.mGraph.getRoot();
		}
		path = new Path(this.mGraph);
		IPath head = this.findPathToLoopStartNode(this.mGraph.getRoot());
		path.concatenate(head);

	}
	
	/**
	 * Returns the path from the root node to the loop start node.
	 * @param node
	 * @return Path from start node to loop start node\
	 * @throws Exception
	 */
	// If the loop start node is identical to the root node, then this path is empty.
	// Assumes: The graph is shaped in such a way that all the nodes from the root node to the 
	// loop start node constitute a chain, i.e. all these nodes (excluding the loop start node)
	// have one and only one outgoing edge as shown below:
	
	//      root -> n1 -> n2 -> ... -> loop-start-node -> DAG -> target-node
	//                                      ^                         |
	//                                      |                         |
	//                                      +-------------------------+
	private IPath findPathToLoopStartNode(INode node) throws Exception {
		IPath path = new Path(this.mGraph);
		if(!(node == this.mLoopStartNode)) {
			// find the path from node to the loop start node.
			List<IEdge> nextEdges = node.getOutgoingEdgeList();
			if(nextEdges.size() != 1) {
				throw new Exception("findPathToLoopStartNode: before hitting the loop start node, "
						+ "graph is not allowed to have nodes with non-unity (!= 1) number of "
						+ "outgoing edges.");
			}
			IEdge nextEdge = nextEdges.get(0);
			path.concatenate(nextEdge);
			path.concatenate(findPathToLoopStartNode(nextEdge.getHead()));
		}
		return path;
	}
	
	Map<INode, Pair> mTable; // the table (memo) for the dynamic programming
		// approach. This table contains, for each node, the information about the longest path 
		// (in terms of the number of target edges in it) to the target node of the graph.
		// Entries of this table are reset every time findLongestAcyclicPath is called 
		// from findCFPath.
	public IPath findLongestAcyclicPath(INode node, Set<IEdge> currentTargets) throws Exception {
		if(this.mTable == null) {
			this.mTable = getEmptyTable();
		}
		if(!node.equals(this.mTargetNode)) {
			for(IEdge e : node.getOutgoingEdgeList()) {
 				IPath candidatePath = new Path(this.mGraph);
				IPath candidateTail = findLongestAcyclicPath(e.getHead(), currentTargets);
				int candidateLength = 0; // the number of target edges in the candidate path
				
				// If e is one of the current target edges, then candidateLength is 1 more than the number
				// of targets in the longest path (in terms of the no. of target edges in it) from e.head.
				// Otherwise, it is the same as the number of target edges in the longest path (in terms of the no. of 
				// target edges in it)from e.head.
				if(currentTargets.contains(e)) {
					candidateLength = this.mTable.get(e.getHead()).mNumber + 1;
				}
				else {
					candidateLength = this.mTable.get(e.getHead()).mNumber;
				}
				
				// Update the table entry for node if the candidate path has more targets in it than the
				// previously found best path.
				if(((candidateLength > this.mTable.get(node).mNumber)|| (this.mTable.get(node).mNumber == 0))) {
					candidatePath.concatenate(e);
					candidatePath.concatenate(candidateTail);
					this.mTable.put(node, new Pair(candidatePath, candidateLength));
				}
			}
		}
		else {
			this.mTable.put(node, new Pair(new Path(this.mGraph), 0));
		}
//		System.out.println(this.mTable.get(node).mPath.toString());
		return this.mTable.get(node).mPath;
	}
	
	// Fill up the table with empty paths and 0 lengths for all nodes.
	private Map<INode, Pair> getEmptyTable() {
		Map<INode, Pair> map = new HashMap<INode, Pair>();
		for(INode n : this.mGraph.getNodeSet()) {
			map.put(n, new Pair(new Path(this.mGraph), 0));
		}
		return map;
	}
	
	public IPath findCFPath(INode startNode, Set<IEdge> targets) throws Exception {
		
		Set<IEdge> currentTargets = new HashSet<IEdge>();
		for(IEdge e : targets) {
			currentTargets.add(e);
		}
		while(!currentTargets.isEmpty()) {
			this.mTable = getEmptyTable();
			IPath newAcyclicPath = findLongestAcyclicPath(startNode, currentTargets);
/*
			// Adding the back edge.
			List<IEdge> p = newAcyclicPath.getPath();
			INode stopNode = p.get(p.size() - 1).getHead();
			IEdge stop_mainloop_edge = stopNode.getOutgoingEdgeList().get(0);
			newAcyclicPath.concatenate(stop_mainloop_edge);
*/
			
			currentTargets = updateTargets(newAcyclicPath, currentTargets);
			path.concatenate(newAcyclicPath);
			System.out.println("path = " + path);
			List<IEdge> loopEdgeList = this.mTargetNode.getOutgoingEdgeList();
			if(!loopEdgeList.isEmpty()) {
				path.concatenate(this.mTargetNode.getOutgoingEdgeList().get(0));
				startNode = this.mLoopStartNode;
			}
			if(mTargetNode.getOutgoingEdgeList().size() == 0) break;

		}
		return path;
	}
	
	private Set<IEdge> updateTargets(IPath newAcyclicPath, Set<IEdge> currentTargets) {
		currentTargets.removeAll(newAcyclicPath.getPath());
		return currentTargets;
	}

	
}
