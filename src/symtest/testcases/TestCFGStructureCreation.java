package testcases;

import cfg.*;
import junit.framework.Assert;
import mycfg.CFEdge;
import mycfg.CFG;
import mycfg.CFGBasicBlockNode;
import mycfg.CFGDecisionNode;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test cases for different types of basic constructs of CFG
 * 
 * @author pavithra
 * 
 */

public class TestCFGStructureCreation {

	private ICFG mCFG;
	private ICFGBasicBlockNode mStart, mEnd;

	/*
	 * Creates an CFG with only start,end nodes
	 */
	@Test
	public final void testCFG() throws Exception{
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
	public final void testAddDecisionNode() throws Exception{
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
	public final void testAddBasicBlockNode() throws Exception{
		this.mStart = new CFGBasicBlockNode(null);
		this.mEnd = new CFGBasicBlockNode(null);
		this.mCFG = new CFG(this.mStart, this.mEnd);
		ICFGBasicBlockNode node = new CFGBasicBlockNode(null);
		this.mCFG.addBasicBlockNode(node);
		Assert.assertEquals(this.mCFG.hasBasicBlockNode(node), true);
		Assert.assertEquals(this.mCFG.hasNode(node), true);
	}

	/*
	 * Simulates ifelse_construct.odg in /resources
	 */

	@Test
	public final void testIfElseCFG() throws Exception{

		CFGBasicBlockNode A = new CFGBasicBlockNode(null);
		CFGBasicBlockNode F = new CFGBasicBlockNode(null);
		Set<ICFGNode> nodeSet = new HashSet<ICFGNode>();
		this.mCFG = new CFG(A, F);
		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
		this.mCFG.addBasicBlockNode(B);

		ICFGDecisionNode C = new CFGDecisionNode(this.mCFG, null);
		this.mCFG.addDecisionNode(C);

		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
		this.mCFG.addBasicBlockNode(D);

		ICFGBasicBlockNode E = new CFGBasicBlockNode(mCFG);
		this.mCFG.addBasicBlockNode(E);

		CFEdge e1 = new CFEdge(this.mCFG, A, B);
		CFEdge e2 = new CFEdge(this.mCFG, B, C);
		CFEdge e3 = new CFEdge(this.mCFG, C, D);
		CFEdge e4 = new CFEdge(this.mCFG, C, E);
		CFEdge e5 = new CFEdge(this.mCFG, D, F);
		CFEdge e6 = new CFEdge(this.mCFG, E, F);

		this.mCFG.addEdge(e1);
		this.mCFG.addEdge(e2);
		this.mCFG.addEdge(e3);
		this.mCFG.addEdge(e4);
		this.mCFG.addEdge(e5);
		this.mCFG.addEdge(e6);

		nodeSet.add(A);
		nodeSet.add(B);
		nodeSet.add(C);
		nodeSet.add(D);
		nodeSet.add(E);
		nodeSet.add(F);

		Assert.assertEquals(this.mCFG.getNumberOfBasicBlockNodes(), 5);
		Assert.assertEquals(this.mCFG.getNumberOfDecisionNodes(), 1);
		Assert.assertEquals(this.mCFG.getNumberOfEdges(), 6);
		Assert.assertEquals(this.mCFG.getNumberOfNodes(), 6);
		Assert.assertEquals(this.mCFG.getNodeSet(), nodeSet);
		ICFEdge edge = new CFEdge(this.mCFG, D, C);
		Assert.assertFalse(this.mCFG.getEdgeSet().contains(edge));
		Assert.assertEquals(this.mCFG.hasEdge(e1), true);
	}

	/*
	 * Simulates Loop.odg in /resources
	 */

	@Test
	public final void testLoopCFG() throws Exception{

		CFGBasicBlockNode A = new CFGBasicBlockNode(null);
		CFGBasicBlockNode E = new CFGBasicBlockNode(null);
		Set<ICFGNode> nodeSet = new HashSet<ICFGNode>();
		this.mCFG = new CFG(A, E);
		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
		this.mCFG.addBasicBlockNode(B);

		ICFGDecisionNode C = new CFGDecisionNode(this.mCFG, null);
		this.mCFG.addDecisionNode(C);

		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
		this.mCFG.addBasicBlockNode(D);

		CFEdge e1 = new CFEdge(this.mCFG, A, B);
		CFEdge e2 = new CFEdge(this.mCFG, B, C);
		CFEdge e3 = new CFEdge(this.mCFG, C, D);
		CFEdge e4 = new CFEdge(this.mCFG, C, B);
		CFEdge e5 = new CFEdge(this.mCFG, D, E);

		this.mCFG.addEdge(e1);
		this.mCFG.addEdge(e2);
		this.mCFG.addEdge(e3);
		this.mCFG.addEdge(e4);
		this.mCFG.addEdge(e5);

		nodeSet.add(A);
		nodeSet.add(B);
		nodeSet.add(C);
		nodeSet.add(D);
		nodeSet.add(E);

		Assert.assertEquals(this.mCFG.getNumberOfBasicBlockNodes(), 4);
		Assert.assertEquals(this.mCFG.getNumberOfDecisionNodes(), 1);
		Assert.assertEquals(this.mCFG.getNumberOfEdges(), 5);
		Assert.assertEquals(this.mCFG.getNumberOfNodes(), 5);
		Assert.assertEquals(this.mCFG.getNodeSet(), nodeSet);
		ICFEdge edge = new CFEdge(this.mCFG, D, C);
		Assert.assertFalse(this.mCFG.getEdgeSet().contains(edge));
		Assert.assertEquals(this.mCFG.hasEdge(e1), true);
	}

}
