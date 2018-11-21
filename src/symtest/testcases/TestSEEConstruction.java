//package testcases;
//
//import cfg.ICFEdge;
//import cfg.ICFG;
//import cfg.ICFGBasicBlockNode;
//import cfg.ICFGDecisionNode;
//import expression.GreaterThanExpression;
//import expression.Input;
//import expression.Variable;
//import junit.framework.Assert;
//import mycfg.CFEdge;
//import mycfg.CFG;
//import mycfg.CFGBasicBlockNode;
//import mycfg.CFGDecisionNode;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import see.SEENew2;
//import set.SET;
//import statement.Statement;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Test cases for the Symbolic Execution Engine
// * refer //resources/seecfg.jpg
// * @author pavithra
// *
// */
//public class TestSEEConstruction {
//
//	private List<ICFEdge> edgeList;
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();
//
//	//Test cases to ensure elimination of invalid constructs
//
//	/**
//	 * 11.4.13 Input: NUll CFG Expected Output: Exception Thrown
//	 */
//	@Test(expected = Exception.class)
//	public final void testNullCFG() throws Exception {
//		ICFG mCFG = null;
//		new SEE(mCFG);
//	}
//
//	/**
//	 * 11.4.13 Input: Valid Loop CFG with Null Edges on the path Expected
//	 * Output: Exception Thrown
//	 */
//	@Test(expected = Exception.class)
//	public final void testValidCFGWithNullEdges() throws Exception {
//		ICFG mCFG = createLoopCFG();
//		SEE see = new SEE(mCFG);
//		List<ICFEdge> edgeSet = new ArrayList<ICFEdge>();
//		edgeSet.add(null);
//		see.expandSET(edgeSet);
//	}
//
//	/**
//	 * 11.4.13 Input: Loop CFG with dangling edges.
//	 * Expected Output: Exception Thrown
//	 */
//	@Test(expected = Exception.class)
//	public final void testCFGWithDanglingEdges() throws Exception {
//		ICFG mCFG;
//		edgeList = new ArrayList<ICFEdge>();
//		mCFG = createLoopCFG();
//		edgeList.get(1).setHead(null);
//		List<ICFEdge> edgeSet = new ArrayList<ICFEdge>();
//		edgeSet.add(edgeList.get(0));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(3));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(3));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(2));
//		edgeSet.add(edgeList.get(4));
//
//		SEE see = new SEE(mCFG);
//		see.expandSET(edgeSet);
//		see.getSET();
//	}
//
//	/**
//	 * Input: Passing a null condition
//	 * Expected: Exception to be thrown
//	 * Refer testNullCondition.jpg
//	 * @throws Exception
//	 */
//	@Test(expected = Exception.class)
//	public final void testNullCondition() throws Exception {
//		ICFG mCFG = null;
//		mCFG = constructIfElseCFG();
//		List<ICFEdge> edgeSet = new ArrayList<ICFEdge>();
//		edgeSet.add(edgeList.get(0));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(2));
//		edgeSet.add(edgeList.get(4));
//		SEE see = new SEE(mCFG);
//		see.expandSET(edgeSet);
//
//	}
//	//Test cases to ensure construction of valid set from valid constructs
//
//	/**
//	 * 10.4.14 Input: valid cfg and a valid set of edges
//	 * Expected Output: Valid SET
//	 * Covers the case of two basic blocks and Decision block -> Basic block
//	 * Targets: e1,e2,e3,e5,e9,e13, e6,e10
//	 * @throws Exception
//	 */
//
//	@Test
//	public final void testCFGwithValidEdges() throws Exception {
//		ICFG mCFG;
//		mCFG = createLoopCFG();
//
//		List<ICFEdge> edgeSet = new ArrayList<ICFEdge>();
//		edgeSet.add(edgeList.get(0));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(3));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(3));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(2));
//		edgeSet.add(edgeList.get(4));
//
//		SEE see = new SEE(mCFG);
//		see.expandSET(edgeSet);
//		SET set = see.getSET();
//
//		Assert.assertEquals(set.getNumberOfNodes(), 9);
//		Assert.assertEquals(set.getNumberOfBasicBlockNodes(), 6);
//		Assert.assertEquals(set.getNumberOfDecisionNodes(), 3);
//		Assert.assertEquals(set.getNumberOfEdges(), 8);
//	}
//
//	/**
//	 * 10.4.14 Input: valid cfg and a valid set of edges
//	 * Expected Output: Valid SET
//	 * Targets: e1,e2,e3, e5, e9, e4,e7, e11
//	 */
//	@Test
//	public final void testSETWithTwoConsecutiveDecisionBlocksOnThenEdge()
//			throws Exception {
//		ICFG mCFG;
//		mCFG = constructLargestOfThreeNumbersCFG();
//
//		List<ICFEdge> edgeSet = new ArrayList<ICFEdge>();
//		edgeSet.add(edgeList.get(0));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(2));
//		edgeSet.add(edgeList.get(4));
//		edgeSet.add(edgeList.get(8));
//
//		SEE see = new SEE(mCFG);
//		see.expandSET(edgeSet);
//		SET set = see.getSET();
//
//		Assert.assertEquals(set.getNumberOfNodes(), 6);
//		Assert.assertEquals(set.getNumberOfBasicBlockNodes(), 4);
//		Assert.assertEquals(set.getNumberOfDecisionNodes(), 2);
//		Assert.assertEquals(set.getNumberOfEdges(), 5);
//	}
//
//	/**
//	 * 10.4.13 Input: valid cfg and a valid set of edges
//	 * Expected Output: Valid SET
//	 * Covers e1,e2,e3,e5,e9,e8, e12, e14
//	 */
//	@Test
//	public final void testSETWithTwoConsecutiveDecisionBlocksOnElseEdge()
//			throws Exception {
//		ICFG mCFG;
//		mCFG = constructLargestOfThreeNumbersCFG();
//
//		List<ICFEdge> edgeSet = new ArrayList<ICFEdge>();
//		edgeSet.add(edgeList.get(0));
//		edgeSet.add(edgeList.get(1));
//		edgeSet.add(edgeList.get(3));
//		edgeSet.add(edgeList.get(7));
//		edgeSet.add(edgeList.get(10));
//
//		SEE see = new SEE(mCFG);
//		see.expandSET(edgeSet);
//		SET set = see.getSET();
//
//		Assert.assertEquals(set.getNumberOfNodes(), 6);
//		Assert.assertEquals(set.getNumberOfBasicBlockNodes(), 4);
//		Assert.assertEquals(set.getNumberOfDecisionNodes(), 2);
//		Assert.assertEquals(set.getNumberOfEdges(), 5);
//	}
//
//	/**
//	 * Refer greatestofthreenumbers.jpg
//	 * @return cfg
//	 * @throws Exception
//	 */
//	public ICFG constructLargestOfThreeNumbersCFG() throws Exception {
//
//		ICFG mCFG = null;
//		edgeList = new ArrayList<ICFEdge>();
//
//		ICFGBasicBlockNode A = new CFGBasicBlockNode(null);
//		ICFGBasicBlockNode I = new CFGBasicBlockNode(null);
//
//		mCFG = new CFG(A, I);
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(B);
//
//		Variable v1 = new Variable("v1", mCFG);
//		Input i1 = new Input(mCFG);
//		Statement stmt1 = new Statement(mCFG, v1, i1);
//		B.addStatement(stmt1);
//
//		Variable v2 = new Variable("v2", mCFG);
//		Input i2 = new Input(mCFG);
//		Statement stmt2 = new Statement(mCFG, v2, i2);
//		B.addStatement(stmt2);
//
//		Variable v3 = new Variable("v3", mCFG);
//		Input i3 = new Input(mCFG);
//		Statement stmt3 = new Statement(mCFG, v3, i3);
//		B.addStatement(stmt3);
//
//		GreaterThanExpression exp1 = new GreaterThanExpression(mCFG, v1, v2);
//		ICFGDecisionNode C = new CFGDecisionNode(mCFG, exp1);
//		mCFG.addDecisionNode(C);
//
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG, v1, v3);
//		ICFGDecisionNode D = new CFGDecisionNode(mCFG, exp2);
//		mCFG.addDecisionNode(D);
//
//		GreaterThanExpression exp3 = new GreaterThanExpression(mCFG, v2, v3);
//		ICFGDecisionNode E = new CFGDecisionNode(mCFG, exp3);
//		mCFG.addDecisionNode(E);
//
//		ICFGBasicBlockNode F = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(F);
//
//		ICFGBasicBlockNode G = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(G);
//
//		ICFGBasicBlockNode H = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(H);
//
//		CFEdge e1 = new CFEdge(mCFG, A, B);
//		CFEdge e2 = new CFEdge(mCFG, B, C);
//		CFEdge e3 = new CFEdge(mCFG, C, D);
//		CFEdge e4 = new CFEdge(mCFG, C, E);
//		CFEdge e5 = new CFEdge(mCFG, D, F);
//		CFEdge e6 = new CFEdge(mCFG, D, G);
//		CFEdge e7 = new CFEdge(mCFG, E, G);
//		CFEdge e8 = new CFEdge(mCFG, E, H);
//		CFEdge e9 = new CFEdge(mCFG, F, I);
//		CFEdge e10 = new CFEdge(mCFG, G, I);
//		CFEdge e11 = new CFEdge(mCFG, H, I);
//
//		mCFG.addEdge(e1);
//		mCFG.addEdge(e2);
//		mCFG.addEdge(e3);
//		mCFG.addEdge(e4);
//		mCFG.addEdge(e5);
//		mCFG.addEdge(e6);
//		mCFG.addEdge(e7);
//		mCFG.addEdge(e8);
//		mCFG.addEdge(e9);
//		mCFG.addEdge(e10);
//		mCFG.addEdge(e11);
//
//		C.setThenEdge(e3);
//		C.setElseEdge(e4);
//		D.setThenEdge(e5);
//		D.setElseEdge(e6);
//		E.setThenEdge(e7);
//		E.setElseEdge(e8);
//
//		edgeList.add(e1);
//		edgeList.add(e2);
//		edgeList.add(e3);
//		edgeList.add(e4);
//		edgeList.add(e5);
//		edgeList.add(e6);
//		edgeList.add(e7);
//		edgeList.add(e8);
//		edgeList.add(e9);
//		edgeList.add(e10);
//		edgeList.add(e11);
//
//		return mCFG;
//	}
//
//	public ICFG constructIfElseCFG() throws Exception {
//		ICFG mCFG = null;
//		edgeList = new ArrayList<ICFEdge>();
//		CFGBasicBlockNode A = new CFGBasicBlockNode(null);
//		CFGBasicBlockNode F = new CFGBasicBlockNode(null);
//
//		mCFG = new CFG(A, F);
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(B);
//
//		ICFGDecisionNode C = new CFGDecisionNode(mCFG, null);
//		mCFG.addDecisionNode(C);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(D);
//
//		ICFGBasicBlockNode E = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(E);
//
//		CFEdge e1 = new CFEdge(mCFG, A, B);
//		CFEdge e2 = new CFEdge(mCFG, B, C);
//		CFEdge e3 = new CFEdge(mCFG, C, D);
//		CFEdge e4 = new CFEdge(mCFG, C, E);
//		CFEdge e5 = new CFEdge(mCFG, D, F);
//		CFEdge e6 = new CFEdge(mCFG, E, F);
//
//		mCFG.addEdge(e1);
//		mCFG.addEdge(e2);
//		mCFG.addEdge(e3);
//		mCFG.addEdge(e4);
//		mCFG.addEdge(e5);
//		mCFG.addEdge(e6);
//
//		edgeList.add(e1);
//		edgeList.add(e2);
//		edgeList.add(e3);
//		edgeList.add(e4);
//		edgeList.add(e5);
//		edgeList.add(e6);
//		return mCFG;
//	}
//
//	/**
//	 * Creates  a cfg with a single loop
//	 * @return
//	 * refer
//	 * @throws Exception
//	 */
//	public ICFG createLoopCFG() throws Exception {
//		ICFG mCFG = null;
//		edgeList = new ArrayList<ICFEdge>();
//		CFGBasicBlockNode A = new CFGBasicBlockNode(null);
//		CFGBasicBlockNode E = new CFGBasicBlockNode(null);
//
//		mCFG = new CFG(A, E);
//		Variable v1 = new Variable("v1", mCFG);
//		Input i1 = new Input(mCFG);
//		Statement stmt1 = new Statement(mCFG, v1, i1);
//
//		Variable v2 = new Variable("v2", mCFG);
//		Input i2 = new Input(mCFG);
//		Statement stmt2 = new Statement(mCFG, v2, i2);
//
//		Statement stmt6 = new Statement(mCFG, v2, i2);
//		E.addStatement(stmt6);
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//		B.addStatement(stmt1);
//		B.addStatement(stmt2);
//		mCFG.addBasicBlockNode(B);
//
//		GreaterThanExpression exp1 = new GreaterThanExpression(mCFG, v1, v2);
//		ICFGDecisionNode C = new CFGDecisionNode(mCFG, exp1);
//		mCFG.addDecisionNode(C);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
//		Statement stmt5 = new Statement(mCFG, v2, i2);
//		D.addStatement(stmt5);
//		mCFG.addBasicBlockNode(D);
//
//		CFEdge e1 = new CFEdge(mCFG, A, B);
//		CFEdge e2 = new CFEdge(mCFG, B, C);
//		CFEdge e3 = new CFEdge(mCFG, C, D);
//		CFEdge e4 = new CFEdge(mCFG, C, B);
//		CFEdge e5 = new CFEdge(mCFG, D, E);
//		edgeList.add(e1);
//		edgeList.add(e2);
//		edgeList.add(e3);
//		edgeList.add(e4);
//		edgeList.add(e5);
//		mCFG.addEdge(e1);
//		mCFG.addEdge(e2);
//		mCFG.addEdge(e3);
//		mCFG.addEdge(e4);
//		mCFG.addEdge(e5);
//
//		return mCFG;
//	}
//}
