//package testcases;
//
//import cfg.ICFEdge;
//import cfg.ICFG;
//import cfg.ICFGBasicBlockNode;
//import cfg.ICFGDecisionNode;
//import expression.*;
//import junit.framework.Assert;
//import mycfg.CFEdge;
//import mycfg.CFG;
//import mycfg.CFGBasicBlockNode;
//import mycfg.CFGDecisionNode;
//import org.junit.Test;
//import statement.Statement;
//import tester.SymTest;
//import tester.TestSequence;
//
//import java.util.*;
//
//public class TestSymTest {
//	private List<ICFEdge> edgeList;
//
//	/**
//	 * Checks CFG of format S->T refer testcase1.jpg in resources folder
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public final void testZeroNodeCFG() throws Exception {
//		System.out.println("Test Case 1 Start\n\n");
//		ICFG mCFG = null;
//		ICFGBasicBlockNode mCFGStart, mCFGEnd;
//		mCFGStart = new CFGBasicBlockNode("ZeroNodeCFG-S", null);
//		mCFGEnd = new CFGBasicBlockNode("ZeroNodeCFG-T", null);
//		mCFG = new CFG(mCFGStart, mCFGEnd);
//		Variable v1 = null;
//		v1 = new Variable("v1", mCFG);
//
//		Input i1;
//		i1 = new Input(mCFG);
//
//		CFEdge e6 = new CFEdge("ZeroNodeCFG-ST", mCFG, mCFGStart, mCFGEnd);
//		mCFG.addEdge(e6);
//		Statement s1 = new Statement(mCFG, v1, i1);
//		mCFGStart.addStatement(s1);
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(e6);
//
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 0);
//		System.out.println("Test Case 1 End\n\n");
//
//	}
//
//	/**
//	 * Check CFG of the form S->BasicBlock->Terminal Refer testcase2.jpg
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public final void testSingleNodeCFG() throws Exception {
//		System.out.println("TEst Case 2 Start\n\n");
//		ICFG mCFG = null;
//		ICFGBasicBlockNode mCFGStart, mCFGEnd;
//		mCFGStart = new CFGBasicBlockNode("SingleNodeCFG-S", null);
//		mCFGEnd = new CFGBasicBlockNode("SingleNodeCFG-T", null);
//		mCFG = new CFG(mCFGStart, mCFGEnd);
//		Variable v1 = null;
//
//		v1 = new Variable("v1", mCFG);
//
//		Input i1;
//		i1 = new Input(mCFG);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode("SingleNodeCFG-D", mCFG);
//		mCFG.addBasicBlockNode(D);
//		CFEdge e6 = new CFEdge("SingleNodeCFG-SD", mCFG, mCFGStart, D);
//		mCFG.addEdge(e6);
//		Statement s1 = new Statement(mCFG, v1, i1);
//		D.addStatement(s1);
//		ICFEdge e5 = new CFEdge("SingleNodeCFG-DT", mCFG, D, mCFGEnd);
//		mCFG.addEdge(e5);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(e5);
//		targets.add(e6);
//
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		System.out.println("Test Case 2 End\n\n");
//
//	}
//
//	/**
//	 * A looped If else CFG Refer TestCase4.jpg Edges in bold are the specified
//	 * targets.
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public final void testWhenTargetsCannotBeSatisfiedInSingleExecution()
//			throws Exception {
//		System.out.println("Test Case 3 Start\n\n");
//		ICFG mCFG = null;
//		ICFGBasicBlockNode mCFGStart, mCFGEnd;
//		mCFGStart = new CFGBasicBlockNode("S3", mCFG);
//		mCFGEnd = new CFGBasicBlockNode("T3", mCFG);
//		mCFG = new CFG(mCFGStart, mCFGEnd);
//		Variable v1 = null;
//		v1 = new Variable("v1", mCFG);
//
//		Input i1;
//
//		i1 = new Input(mCFG);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode("D", mCFG);
//		mCFG.addBasicBlockNode(D);
//		CFEdge e6 = new CFEdge("SD", mCFG, mCFGStart, D);
//		mCFG.addEdge(e6);
//		Statement s1 = new Statement(mCFG, v1, i1);
//		D.addStatement(s1);
//
//		ConcreteConstant exp1 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG, v1, exp1);
//		ICFGDecisionNode A = new CFGDecisionNode("A", mCFG, exp2);
//		mCFG.addDecisionNode(A);
//		ICFEdge e1 = new CFEdge("DA", mCFG, D, A);
//		mCFG.addEdge(e1);
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode("B", mCFG);
//		IExpression exp3 = new AddExpression(mCFG,
//				new ConcreteConstant(10, mCFG), new ConcreteConstant(20, mCFG));
//		B.addStatement(new Statement(mCFG, v1, exp3));
//		mCFG.addBasicBlockNode(B);
//
//		ICFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
//		IExpression exp4 = new AddExpression(mCFG,
//				new ConcreteConstant(1, mCFG), new ConcreteConstant(2, mCFG));
//		C.addStatement(new Statement(mCFG, v1, exp4));
//		mCFG.addBasicBlockNode(C);
//
//		ICFEdge e2 = new CFEdge("AB", mCFG, A, B);
//		mCFG.addEdge(e2);
//		ICFEdge e3 = new CFEdge("AC", mCFG, A, C);
//		mCFG.addEdge(e3);
//		A.setThenEdge(e2);
//		A.setElseEdge(e3);
//		ICFEdge e4 = new CFEdge("BT", mCFG, B, mCFGEnd);
//		mCFG.addEdge(e4);
//		ICFEdge e5 = new CFEdge("CT", mCFG, C, mCFGEnd);
//		mCFG.addEdge(e5);
//		ICFEdge e7 = new CFEdge("TS", mCFG, mCFGEnd, mCFGStart);
//		mCFG.addEdge(e7);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		// Adding the 'then' edge
//		targets.add(e2);
//		// Adding the 'else' edge
//		targets.add(e3);
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//
//		if(testseq == null) {
//			System.out.println("null test sequence!");
//			Assert.assertEquals(false, true);
//		}
//		System.out.println(seq);
//
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 2);
//		List<Object> values = testseq.get(v1);
//		for (Object v : values) {
//			int k = ((Integer) v).intValue();
//			Assert.assertTrue((k > 15) || (k <= 15));
//		}
//		System.out.println("Test Case 3 End\n\n");
//
//	}
//
//	/**
//	 * A looped If else CFG Refer TestCase3.jpg Edges in bold are the specified
//	 * targets.
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public final void testWhenTargetsCanBeSatisfiedInSingleExecution()
//			throws Exception {
//		System.out.println("Test Case 4 Start\n\n");
//		ICFG mCFG = null;
//		ICFGBasicBlockNode mCFGStart, mCFGEnd;
//		mCFGStart = new CFGBasicBlockNode(null);
//		mCFGEnd = new CFGBasicBlockNode(null);
//		mCFG = new CFG(mCFGStart, mCFGEnd);
//		Variable v1 = null;
//
//		v1 = new Variable("v1", mCFG);
//
//		Input i1;
//
//		i1 = new Input(mCFG);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(D);
//		CFEdge e6 = new CFEdge(mCFG, mCFGStart, D);
//		mCFG.addEdge(e6);
//		Statement s1 = new Statement(mCFG, v1, i1);
//		D.addStatement(s1);
//
//		ConcreteConstant exp1 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG, v1, exp1);
//		ICFGDecisionNode A = new CFGDecisionNode(mCFG, exp2);
//		mCFG.addDecisionNode(A);
//		ICFEdge e1 = new CFEdge(mCFG, D, A);
//		mCFG.addEdge(e1);
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//		IExpression exp3 = new AddExpression(mCFG,
//				new ConcreteConstant(10, mCFG), new ConcreteConstant(20, mCFG));
//		B.addStatement(new Statement(mCFG, v1, exp3));
//		mCFG.addBasicBlockNode(B);
//
//		ICFGBasicBlockNode C = new CFGBasicBlockNode(mCFG);
//		IExpression exp4 = new AddExpression(mCFG,
//				new ConcreteConstant(1, mCFG), new ConcreteConstant(2, mCFG));
//		C.addStatement(new Statement(mCFG, v1, exp4));
//		mCFG.addBasicBlockNode(C);
//
//		ICFEdge e2 = new CFEdge(mCFG, A, B);
//		mCFG.addEdge(e2);
//		ICFEdge e3 = new CFEdge(mCFG, A, C);
//		mCFG.addEdge(e3);
//		A.setThenEdge(e2);
//		A.setElseEdge(e3);
//		ICFEdge e4 = new CFEdge(mCFG, B, mCFGEnd);
//		mCFG.addEdge(e4);
//		ICFEdge e5 = new CFEdge(mCFG, C, mCFGEnd);
//		mCFG.addEdge(e5);
//		ICFEdge e7 = new CFEdge(mCFG, mCFGEnd, mCFGStart);
//		mCFG.addEdge(e7);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		// Adding the 'then' edge
//		targets.add(e2);
//		// Adding the next edge
//		targets.add(e4);
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 1);
//		List<Object> values = testseq.get(v1);
//		for (Object v : values) {
//			int k = ((Integer) v).intValue();
//			Assert.assertTrue(k > 15);
//		}
//		System.out.println("Test Case 4 End\n\n");
//
//	}
//
//	/**
//	 * A usual If else CFG Refer TestCase5.jpg Edges in bold are the specified
//	 * targets.
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public final void testWhenTargetsCannotBeSatisfiedWithNoLoops()
//			throws Exception {
//		System.out.println("Test Case 5 Start\n\n");
//		ICFG mCFG = null;
//		ICFGBasicBlockNode mCFGStart, mCFGEnd;
//		mCFGStart = new CFGBasicBlockNode(mCFG);
//		mCFGEnd = new CFGBasicBlockNode(mCFG);
//		mCFG = new CFG(mCFGStart, mCFGEnd);
//		Variable v1 = null;
//		v1 = new Variable("v1", mCFG);
//
//		Input i1;
//
//		i1 = new Input(mCFG);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(D);
//		CFEdge e6 = new CFEdge(mCFG, mCFGStart, D);
//		mCFG.addEdge(e6);
//		Statement s1 = new Statement(mCFG, v1, i1);
//		D.addStatement(s1);
//
//		ConcreteConstant exp1 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG, v1, exp1);
//		ICFGDecisionNode A = new CFGDecisionNode(mCFG, exp2);
//		mCFG.addDecisionNode(A);
//		ICFEdge e1 = new CFEdge(mCFG, D, A);
//		mCFG.addEdge(e1);
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//		IExpression exp3 = new AddExpression(mCFG,
//				new ConcreteConstant(10, mCFG), new ConcreteConstant(20, mCFG));
//		B.addStatement(new Statement(mCFG, v1, exp3));
//		mCFG.addBasicBlockNode(B);
//
//		ICFGBasicBlockNode C = new CFGBasicBlockNode(mCFG);
//		IExpression exp4 = new AddExpression(mCFG,
//				new ConcreteConstant(1, mCFG), new ConcreteConstant(2, mCFG));
//		C.addStatement(new Statement(mCFG, v1, exp4));
//		mCFG.addBasicBlockNode(C);
//
//		ICFEdge e2 = new CFEdge(mCFG, A, B);
//		mCFG.addEdge(e2);
//		ICFEdge e3 = new CFEdge(mCFG, A, C);
//		mCFG.addEdge(e3);
//		A.setThenEdge(e2);
//		A.setElseEdge(e3);
//		ICFEdge e4 = new CFEdge(mCFG, B, mCFGEnd);
//		mCFG.addEdge(e4);
//		ICFEdge e5 = new CFEdge(mCFG, C, mCFGEnd);
//		mCFG.addEdge(e5);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		// Adding the 'then' edge
//		targets.add(e2);
//		// Adding the 'else' edge
//		targets.add(e3);
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 1);
//		List<Object> values = testseq.get(v1);
//		for (Object v : values) {
//			int k = ((Integer) v).intValue();
//			Assert.assertTrue(k > 15);
//		}
//		System.out.println("Test Case 5 End\n\n");
//
//	}
//
//	/**
//	 * A usual If else CFG Refer TestCase6.jpg Edges in bold are the specified
//	 * targets.
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public final void testWhenTargetsCanBeSatisfiedWithNoLoops()
//			throws Exception {
//		System.out.println("Test Case 6 Start");
//		ICFG mCFG = null;
//		ICFGBasicBlockNode mCFGStart, mCFGEnd;
//		mCFGStart = new CFGBasicBlockNode(mCFG);
//		mCFGEnd = new CFGBasicBlockNode(mCFG);
//		mCFG = new CFG(mCFGStart, mCFGEnd);
//		Variable v1 = null;
//		v1 = new Variable("v1", mCFG);
//
//		Input i1;
//		i1 = new Input(mCFG);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(D);
//		CFEdge e6 = new CFEdge("StD", mCFG, mCFGStart, D);
//		mCFG.addEdge(e6);
//		Statement s1 = new Statement(mCFG, v1, i1);
//		D.addStatement(s1);
//
//		ConcreteConstant exp1 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG, v1, exp1);
//		ICFGDecisionNode A = new CFGDecisionNode(mCFG, exp2);
//		mCFG.addDecisionNode(A);
//		ICFEdge e1 = new CFEdge( mCFG, D, A);
//		mCFG.addEdge(e1);
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//		IExpression exp3 = new AddExpression(mCFG,
//				new ConcreteConstant(10, mCFG), new ConcreteConstant(20, mCFG));
//		B.addStatement(new Statement(mCFG, v1, exp3));
//		mCFG.addBasicBlockNode(B);
//
//		ICFGBasicBlockNode C = new CFGBasicBlockNode(mCFG);
//		IExpression exp4 = new AddExpression(mCFG,
//				new ConcreteConstant(1, mCFG), new ConcreteConstant(2, mCFG));
//		C.addStatement(new Statement(mCFG, v1, exp4));
//		mCFG.addBasicBlockNode(C);
//
//		ICFEdge e2 = new CFEdge(mCFG, A, B);
//		mCFG.addEdge(e2);
//		ICFEdge e3 = new CFEdge(mCFG, A, C);
//		mCFG.addEdge(e3);
//		A.setThenEdge(e2);
//		A.setElseEdge(e3);
//		ICFEdge e4 = new CFEdge(mCFG, B, mCFGEnd);
//		mCFG.addEdge(e4);
//		ICFEdge e5 = new CFEdge(mCFG, C, mCFGEnd);
//		mCFG.addEdge(e5);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		// Adding the 'then' edge
//		targets.add(e2);
//		// Adding the next edge
//		targets.add(e4);
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 1);
//		List<Object> values = testseq.get(v1);
//		for (Object v : values) {
//			int k = ((Integer) v).intValue();
//			Assert.assertTrue(k > 15);
//		}
//		System.out.println("Test Case 6 End\n\n");
//	}
//
//	/**
//	 * CFG: Find greatest of three numbers refer TestCase7.jpg Targets: v1>v2,
//	 * v1 > v3
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public void testCFGWithThreeDecisionBlocksWithoutLoopSatisfiableInSingleExecution() throws Exception{
//		System.out.println("Test Case 7 Start\n\n");
//		ICFG mCFG = constructLargestOfThreeNumbersCFG();
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(edgeList.get(2));
//		targets.add(edgeList.get(4));
//		targets.add(edgeList.get(8));
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 3);
//		List<Object> resVariable1 = null, resVariable2 = null, resVariable3 = null;
//		for (Map.Entry<IIdentifier, List<Object>> entry : testseq.entrySet()) {
//			Assert.assertEquals(entry.getValue().size(), 1);
//			if (entry.getKey().getName().equals("v1")) {
//				resVariable1 = entry.getValue();
//			} else if (entry.getKey().getName().equals("v2")) {
//				resVariable2 = entry.getValue();
//			} else if (entry.getKey().getName().equals("v3")) {
//				resVariable3 = entry.getValue();
//			}
//		}
//		Assert.assertTrue((((Integer) resVariable1.get(0)).intValue() > ((Integer) resVariable2
//				.get(0)).intValue())
//				&& (((Integer) resVariable1.get(0)).intValue() > ((Integer) resVariable3
//						.get(0)).intValue()));
//		System.out.println("Test Case 7 end\n\n");
//
//	}
//
//	/**
//	 * CFG: Find greatest of three numbers refer TestCase8.jpg Targets: All
//	 * branches of DecisionNode1, DecisionNode2 Case 1: v1>v2>v3 Case 2: V1>v2,
//	 * v1<=v3
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public void testCFGWithThreeDecisionBlocksWithLoopSatisfiableInMultipleExecution()  throws Exception {
//		System.out.println("Test Case 8 Start\n\n");
//		ICFG mCFG = constructLargestOfThreeNumbersCFG();
//		ICFEdge edge = new CFEdge(mCFG, mCFG.getStopNode(), mCFG.getStartNode());
//		mCFG.addEdge(edge);
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(edgeList.get(2));
//		targets.add(edgeList.get(4));
//		targets.add(edgeList.get(5));
//		targets.add(edgeList.get(8));
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 3);
//		List<Object> resVariable1 = null, resVariable2 = null, resVariable3 = null;
//		for (Map.Entry<IIdentifier, List<Object>> entry : testseq.entrySet()) {
//			Assert.assertEquals(entry.getValue().size(), 2);
//			if (entry.getKey().getName().equals("v1")) {
//				resVariable1 = entry.getValue();
//			} else if (entry.getKey().getName().equals("v2")) {
//				resVariable2 = entry.getValue();
//			} else if (entry.getKey().getName().equals("v3")) {
//				resVariable3 = entry.getValue();
//			}
//		}
//		Assert.assertTrue(((((Integer) resVariable1.get(0)).intValue() > ((Integer) resVariable2
//				.get(0)).intValue()) && (((Integer) resVariable1.get(0))
//				.intValue() > ((Integer) resVariable3.get(0)).intValue()))
//				|| ((((Integer) resVariable1.get(0)).intValue() > ((Integer) resVariable2
//						.get(0)).intValue()) && (((Integer) resVariable1.get(0))
//						.intValue() <= ((Integer) resVariable3.get(0))
//						.intValue())));
//
//		Assert.assertTrue(((((Integer) resVariable1.get(1)).intValue() > ((Integer) resVariable2
//				.get(1)).intValue()) && (((Integer) resVariable1.get(1))
//				.intValue() > ((Integer) resVariable3.get(1)).intValue()))
//				|| ((((Integer) resVariable1.get(1)).intValue() > ((Integer) resVariable2
//						.get(1)).intValue()) && (((Integer) resVariable1.get(1))
//						.intValue() <= ((Integer) resVariable3.get(1))
//						.intValue())));
//		System.out.println("Test Case 8 end\n\n");
//	}
//
//	/**
//	 * CFG: Find greatest of three numbers refer TestCase9.jpg Targets: All
//	 * possible Branches
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public void testCFGWithThreeDecisionBlocksWithLoopSatisfiableWithAllPossibleExecutions()
//			throws Exception {
//		System.out.println("Test Case 9 Start");
//		ICFG mCFG = constructLargestOfThreeNumbersCFG();
//		ICFEdge edge = new CFEdge("TeS", mCFG, mCFG.getStopNode(),
//				mCFG.getStartNode());
//		mCFG.addEdge(edge);
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(edgeList.get(2));
//		targets.add(edgeList.get(3));
//		targets.add(edgeList.get(4));
//		targets.add(edgeList.get(5));
//		targets.add(edgeList.get(6));
//		targets.add(edgeList.get(7));
//		targets.add(edgeList.get(8));
//		targets.add(edgeList.get(9));
//		targets.add(edgeList.get(10));
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 3);
//		List<Object> resVariable1 = null, resVariable2 = null, resVariable3 = null;
//		for (Map.Entry<IIdentifier, List<Object>> entry : testseq.entrySet()) {
//			Assert.assertEquals(entry.getValue().size(), 4);
//			if (entry.getKey().getName().equals("v1")) {
//				resVariable1 = entry.getValue();
//			} else if (entry.getKey().getName().equals("v2")) {
//				resVariable2 = entry.getValue();
//			} else if (entry.getKey().getName().equals("v3")) {
//				resVariable3 = entry.getValue();
//			}
//		}
//		Assert.assertTrue(((((Integer) resVariable1.get(0)).intValue() > ((Integer) resVariable2
//				.get(0)).intValue()) && (((Integer) resVariable1.get(0))
//				.intValue() > ((Integer) resVariable3.get(0)).intValue()))
//				|| ((((Integer) resVariable1.get(0)).intValue() > ((Integer) resVariable2
//						.get(0)).intValue()) && (((Integer) resVariable1.get(0))
//						.intValue() <= ((Integer) resVariable3.get(0))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(0)).intValue() <= ((Integer) resVariable2
//						.get(0)).intValue()) && (((Integer) resVariable2.get(0))
//						.intValue() > ((Integer) resVariable3.get(0))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(0)).intValue() <= ((Integer) resVariable2
//						.get(0)).intValue()) && (((Integer) resVariable1.get(0))
//						.intValue() <= ((Integer) resVariable3.get(0))
//						.intValue()))
//
//		);
//
//		Assert.assertTrue(((((Integer) resVariable1.get(1)).intValue() > ((Integer) resVariable2
//				.get(1)).intValue()) && (((Integer) resVariable1.get(1))
//				.intValue() > ((Integer) resVariable3.get(1)).intValue()))
//				|| ((((Integer) resVariable1.get(1)).intValue() > ((Integer) resVariable2
//						.get(1)).intValue()) && (((Integer) resVariable1.get(1))
//						.intValue() <= ((Integer) resVariable3.get(1))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(1)).intValue() <= ((Integer) resVariable2
//						.get(1)).intValue()) && (((Integer) resVariable2.get(1))
//						.intValue() > ((Integer) resVariable3.get(1))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(1)).intValue() <= ((Integer) resVariable2
//						.get(1)).intValue()) && (((Integer) resVariable1.get(1))
//						.intValue() <= ((Integer) resVariable3.get(1))
//						.intValue()))
//
//		);
//
//		Assert.assertTrue(((((Integer) resVariable1.get(2)).intValue() > ((Integer) resVariable2
//				.get(2)).intValue()) && (((Integer) resVariable1.get(2))
//				.intValue() > ((Integer) resVariable3.get(2)).intValue()))
//				|| ((((Integer) resVariable1.get(2)).intValue() > ((Integer) resVariable2
//						.get(2)).intValue()) && (((Integer) resVariable1.get(2))
//						.intValue() <= ((Integer) resVariable3.get(2))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(2)).intValue() <= ((Integer) resVariable2
//						.get(2)).intValue()) && (((Integer) resVariable2.get(2))
//						.intValue() > ((Integer) resVariable3.get(2))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(2)).intValue() <= ((Integer) resVariable2
//						.get(2)).intValue()) && (((Integer) resVariable1.get(2))
//						.intValue() <= ((Integer) resVariable3.get(2))
//						.intValue()))
//
//		);
//
//		Assert.assertTrue(((((Integer) resVariable1.get(3)).intValue() > ((Integer) resVariable2
//				.get(3)).intValue()) && (((Integer) resVariable1.get(3))
//				.intValue() > ((Integer) resVariable3.get(3)).intValue()))
//				|| ((((Integer) resVariable1.get(3)).intValue() > ((Integer) resVariable2
//						.get(3)).intValue()) && (((Integer) resVariable1.get(3))
//						.intValue() <= ((Integer) resVariable3.get(3))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(3)).intValue() <= ((Integer) resVariable2
//						.get(3)).intValue()) && (((Integer) resVariable2.get(3))
//						.intValue() > ((Integer) resVariable3.get(3))
//						.intValue()))
//				|| ((((Integer) resVariable1.get(3)).intValue() <= ((Integer) resVariable2
//						.get(3)).intValue()) && (((Integer) resVariable1.get(3))
//						.intValue() <= ((Integer) resVariable3.get(3))
//						.intValue()))
//
//		);
//
//		System.out.println("Test Case 9 end\n\n");
//	}
//
//	/**
//	 * CFG: Double decision Boxes and updated value for variables Targets: Can
//	 * be covered if the value gets updated Refer TestCase10.jpg
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public void testUpdatedValuesInDoubleDecisionBoxesWhenSatisfiable()
//			throws Exception {
//
//		System.out.println("Test Case 10 Start\n\n");
//		ICFG mCFG = null;
//		CFGBasicBlockNode A = new CFGBasicBlockNode(null);
//		CFGBasicBlockNode I = new CFGBasicBlockNode(null);
//
//		mCFG = new CFG(A, I);
//		Variable v1 = new Variable("v1", mCFG);
//		Input i1 = new Input(mCFG);
//		Statement stmt1 = new Statement(mCFG, v1, i1);
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//		B.addStatement(stmt1);
//		mCFG.addBasicBlockNode(B);
//
//		ConcreteConstant constant1 = new ConcreteConstant(5, mCFG);
//		GreaterThanExpression exp1 = new GreaterThanExpression(mCFG, constant1,
//				v1);
//		ICFGDecisionNode C = new CFGDecisionNode(mCFG, exp1);
//		mCFG.addDecisionNode(C);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode(mCFG);
//		ConcreteConstant constant3 = new ConcreteConstant(16, mCFG);
//		Statement stmt2 = new Statement(mCFG, v1, constant3);
//		D.addStatement(stmt2);
//		mCFG.addBasicBlockNode(D);
//
//		ICFGBasicBlockNode E = new CFGBasicBlockNode(mCFG);
//		mCFG.addBasicBlockNode(E);
//
//		ConcreteConstant constant2 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG, v1,
//				constant2);
//		ICFGDecisionNode F = new CFGDecisionNode(mCFG, exp2);
//		mCFG.addDecisionNode(F);
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
//		CFEdge e6 = new CFEdge(mCFG, E, F);
//		CFEdge e7 = new CFEdge(mCFG, F, G);
//		CFEdge e8 = new CFEdge(mCFG, F, H);
//		CFEdge e9 = new CFEdge(mCFG, G, I);
//		CFEdge e10 = new CFEdge(mCFG, H, I);
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
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(e3);
//		targets.add(e7);
//		targets.add(e9);
//
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 1);
//		List<Object> values = testseq.get(v1);
//		for (Object v : values) {
//			int k = ((Integer) v).intValue();
//			Assert.assertTrue(k <= 5);
//		}
//		System.out.println("Test Case 10 End\n\n");
//
//	}
//
//	// Creation of CFGs
//
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
//}
