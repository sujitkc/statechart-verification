package testcases;

import cfg.*;
import expression.Variable;
import junit.framework.Assert;
import mycfg.CFEdge;
import mycfg.CFG;
import mycfg.CFGBasicBlockNode;
import mycfg.CFGDecisionNode;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * Consists of test cases that would ensure the structure establishment of a
 * graph
 * 
 * @author pavithra
 * 
 */
public class TestMyCFG {

	private ICFG mCFG;
	private ICFGBasicBlockNode mStart, mEnd;

	/*
	 * Creates an CFG with only start,end nodes
	 */
	@Test
	public final void testCFG() throws Exception {
		this.mStart = new CFGBasicBlockNode(this.mCFG);
		this.mEnd = new CFGBasicBlockNode(this.mCFG);
		this.mCFG = new CFG(this.mStart, this.mEnd);

		Assert.assertEquals(this.mStart.getCFG(), this.mCFG);
		Assert.assertEquals(this.mEnd.getCFG(), this.mCFG);
		Assert.assertEquals(this.mStart, this.mCFG.getStartNode());
		Assert.assertEquals(this.mEnd, this.mCFG.getStopNode());
	}

	/*
	 * Tests a CFG with a single decision block
	 */

	@Test
	public final void testAddDecisionNode() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);

		ICFGDecisionNode node = new CFGDecisionNode(this.mCFG, null);
		this.mCFG.addDecisionNode(node);
		Assert.assertEquals(this.mCFG.hasDecisionNode(node), true);
		Assert.assertEquals(this.mCFG.hasNode(node), true);
	}

	/*
	 * Tests a CFG with a single basic block
	 */
	@Test
	public final void testAddBasicBlockNode() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFGBasicBlockNode node = new CFGBasicBlockNode(null);
		this.mCFG.addBasicBlockNode(node);
		Assert.assertEquals(this.mCFG.hasBasicBlockNode(node), true);
		Assert.assertEquals(this.mCFG.hasNode(node), true);
	}

	/**
	 * Checks the process of deleting a Basic Block
	 */
	@Test
	public final void testDeleteBasicBlockNode() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFGBasicBlockNode node = new CFGBasicBlockNode(null);
		this.mCFG.addBasicBlockNode(node);
		Assert.assertEquals(this.mCFG.hasBasicBlockNode(node), true);
		Assert.assertEquals(this.mCFG.hasNode(node), true);
		this.mCFG.deleteBasicBlockNode(node);
		Assert.assertEquals(this.mCFG.hasNode(node), false);
		// Added this test case to check if it is deleted.
		Assert.assertEquals(this.mCFG.getNumberOfBasicBlockNodes(), 2);// Start,
																		// Stop
																		// nodes
	}

	/**
	 * Checks the addition of Basic blocks by confirming the total number.
	 */
	@Test
	public final void testGetNumberOfBasicBlockNodes() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFGBasicBlockNode node = new CFGBasicBlockNode(null);
		this.mCFG.addBasicBlockNode(node);
		Assert.assertEquals(this.mCFG.getNumberOfBasicBlockNodes(), 3);
	}

	/**
	 * Checks the addition of Decision blocks by confirming the total number.
	 */

	@Test
	public final void testGetNumberOfCFGDecisionNodes() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFGDecisionNode node = new CFGDecisionNode(this.mCFG, null);
		this.mCFG.addDecisionNode(node);
		Assert.assertEquals(this.mCFG.getNumberOfDecisionNodes(), 1);
	}

	/**
	 * Checks the existence of a node.
	 */
	@Test
	public final void testHasNode() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFGDecisionNode dnode = new CFGDecisionNode(this.mCFG, null);
		this.mCFG.addDecisionNode(dnode);
		ICFGBasicBlockNode bbnode = new CFGBasicBlockNode(null);
		this.mCFG.addBasicBlockNode(bbnode);

		Assert.assertEquals(this.mCFG.hasNode(this.mStart), true);
		Assert.assertEquals(this.mCFG.hasNode(this.mEnd), true);
		Assert.assertEquals(this.mCFG.hasNode(dnode), true);
		Assert.assertEquals(this.mCFG.hasNode(bbnode), true);
		ICFGBasicBlockNode bbnode1 = new CFGBasicBlockNode(null);
		Assert.assertEquals(this.mCFG.hasNode(bbnode1), false);
		ICFGDecisionNode dnode1 = new CFGDecisionNode(null, null);
		Assert.assertEquals(this.mCFG.hasNode(dnode1), false);
	}

	/**
	 * Checks the total number of nodes
	 */
	@Test
	public final void testGetNumberOfNodes() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFGDecisionNode dnode = new CFGDecisionNode(this.mCFG, null);
		this.mCFG.addDecisionNode(dnode);
		ICFGBasicBlockNode bbnode = new CFGBasicBlockNode(null);
		this.mCFG.addBasicBlockNode(bbnode);
		Assert.assertEquals(this.mCFG.getNumberOfNodes(), 4);
	}

	/**
	 * Checks the addition of an edge
	 */
	@Test
	public final void testAddEdge() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFEdge edge = new CFEdge(this.mCFG, this.mStart, this.mEnd);
		Assert.assertEquals(edge.getHead(), this.mEnd);
		Assert.assertEquals(edge.getTail(), this.mStart);
		this.mCFG.addEdge(edge);
		Assert.assertEquals(this.mStart.getOutgoingEdge(), edge);
		List<ICFEdge> list = this.mEnd.getIncomingEdgeList();
		Assert.assertEquals(list.size(), 1);
		Assert.assertEquals(list.get(0), edge);
	}

	/**
	 * Checks deletion of an edge
	 */
	@Test
	public final void testDeleteEdge() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFEdge edge = new CFEdge(this.mCFG, this.mStart, this.mEnd);
		this.mCFG.addEdge(edge);
		this.mCFG.deleteEdge(edge);
		Assert.assertEquals(this.mCFG.hasEdge(edge), false);
		Assert.assertNotSame(this.mCFG, edge.getCFG());
		Assert.assertNotSame(this.mStart.getOutgoingEdge(), edge);
		List<ICFEdge> list = this.mEnd.getIncomingEdgeList();
		Assert.assertEquals(list.size(), 0);
	}

	/**
	 * Tests the total number of edges in the graph
	 */
	@Test
	public final void testGetNumberOfEdges() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFEdge edge = new CFEdge(this.mCFG, this.mStart, this.mEnd);
		this.mCFG.addEdge(edge);
		Assert.assertEquals(this.mCFG.getNumberOfEdges(), 1);
		this.mCFG.deleteEdge(edge);
		Assert.assertEquals(this.mCFG.getNumberOfEdges(), 0);
	}

	/**
	 * Validates the nodes present in the graph
	 */
	@Test
	public final void testGetNodeSet() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		Set<ICFGNode> set = this.mCFG.getNodeSet();
		Assert.assertEquals(set.size(), 2);
		Assert.assertEquals(set.contains(this.mStart), true);
		Assert.assertEquals(set.contains(this.mEnd), true);
	}

	/**
	 * Validates the edges in the graph
	 */
	@Test
	public final void testGetEdgeSet() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		Set<ICFEdge> set = this.mCFG.getEdgeSet();
		Assert.assertEquals(set.size(), 0);
		ICFEdge edge = new CFEdge(this.mCFG, this.mStart, this.mEnd);
		this.mCFG.addEdge(edge);
		Assert.assertEquals(set.size(), 1);
	}

	/**
	 * Validates the decision blocks in the graph
	 */
	@Test
	public final void testGetDecisionNodeSet() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		Set<ICFGDecisionNode> set = this.mCFG.getDecisionNodeSet();
		Assert.assertEquals(set.size(), 0);
		Assert.assertEquals(set.contains(this.mStart), false);
		Assert.assertEquals(set.contains(this.mEnd), false);
		ICFGDecisionNode dnode = new CFGDecisionNode(this.mCFG, null);
		this.mCFG.addDecisionNode(dnode);
		set = this.mCFG.getDecisionNodeSet();
		Assert.assertEquals(set.size(), 1);
		Assert.assertEquals(set.contains(dnode), true);
	}

	/**
	 * Validates the basic blocks in the graph
	 */
	@Test
	public final void testGetBasicBlockNodeSet() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		Set<ICFGBasicBlockNode> set = this.mCFG.getBasicBlockNodeSet();
		Assert.assertEquals(set.size(), 2);
		Assert.assertEquals(set.contains(this.mStart), true);
		Assert.assertEquals(set.contains(this.mEnd), true);
	}

	/**
	 * 
	 * Validates the variables in the graph
	 */
	@Test
	public final void testAddVariable() throws Exception {
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);

		Variable v = null;

		v = new Variable("var1", this.mCFG);
		this.mCFG.addVariable(v);
		Assert.assertEquals(this.mCFG.hasVariable(v), true);
	}
}
