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
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class TestSymTestUnSatPredicates {
//
//	@Test
//	public void testSwapHeuristicsWithUnsatisfiableEdgeAsThenEedge() throws Exception {
//		System.out.println("Test Case 1 Start\n\n");
//		ICFG mCFG = null;
//		CFGBasicBlockNode A = new CFGBasicBlockNode("S",null);
//		CFGBasicBlockNode I = new CFGBasicBlockNode("I", null);
//
//		mCFG = new CFG(A, I);
//		Variable v1 = new Variable("v1", mCFG);
//		Input i1 = new Input(mCFG);
//		Statement stmt1 = new Statement(mCFG, v1, i1);
//		ICFGBasicBlockNode B = new CFGBasicBlockNode("B", mCFG);
//		B.addStatement(stmt1);
//		mCFG.addBasicBlockNode(B);
//
//		ConcreteConstant constant1 = new ConcreteConstant(5, mCFG);
//		GreaterThanExpression exp1 = new GreaterThanExpression(mCFG, constant1,
//				v1);
//		ICFGDecisionNode C = new CFGDecisionNode("C", mCFG, exp1);
//		mCFG.addDecisionNode(C);
//
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode("D", mCFG);
//		ConcreteConstant constant3 = new ConcreteConstant(16, mCFG);
//		Statement stmt2 = new Statement(mCFG, v1, constant3);
//		D.addStatement(stmt2);
//		mCFG.addBasicBlockNode(D);
//
//		ICFGBasicBlockNode E = new CFGBasicBlockNode("E", mCFG);
//		ConcreteConstant constant4 = new ConcreteConstant(5, mCFG);
//		Statement stmt3 = new Statement(mCFG, v1, constant4);
//		D.addStatement(stmt3);
//		mCFG.addBasicBlockNode(E);
//
//
//		ConcreteConstant constant2 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG,
//				v1, constant2);
//		ICFGDecisionNode F = new CFGDecisionNode("F", mCFG, exp2);
//		mCFG.addDecisionNode(F);
//
//		ICFGBasicBlockNode G = new CFGBasicBlockNode("G1", mCFG);
//		mCFG.addBasicBlockNode(G);
//
//		ICFGBasicBlockNode H = new CFGBasicBlockNode("H1", mCFG);
//		mCFG.addBasicBlockNode(H);
//
//
//		CFEdge e1 = new CFEdge("AB", mCFG, A, B);
//		CFEdge e2 = new CFEdge("BC", mCFG, B, C);
//		CFEdge e3 = new CFEdge("CD", mCFG, C, D);
//		CFEdge e4 = new CFEdge("CE", mCFG, C, E);
//		CFEdge e5 = new CFEdge("DF", mCFG, D, F);
//		CFEdge e6 = new CFEdge("EF", mCFG, E, F);
//		CFEdge e7 = new CFEdge("FG", mCFG, F, G);
//		CFEdge e8 = new CFEdge("FH", mCFG, F, H);
//		CFEdge e9 = new CFEdge("GI", mCFG, G, I);
//		CFEdge e10 = new CFEdge("HI", mCFG, H, I);
//		CFEdge e11 = new CFEdge("IA", mCFG, I, A);
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
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(e3);
//		targets.add(e4);
//		targets.add(e7);
//		targets.add(e8);
//
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 2);
//
//
//		System.out.println("Test Case 1 End\n\n");
//	}
//
//
//
//	@Test
//	public void testSwapHeuristicsWithUnsatisfiableEdgeAsElseEedge() throws Exception {
//		System.out.println("Test Case 2 Start\n\n");
//		ICFG mCFG = null;
//		CFGBasicBlockNode A = new CFGBasicBlockNode("S1",null);
//		CFGBasicBlockNode I = new CFGBasicBlockNode("I1", null);
//
//		mCFG = new CFG(A, I);
//		Variable v1 = new Variable("v11", mCFG);
//		Input i1 = new Input(mCFG);
//		Statement stmt1 = new Statement(mCFG, v1, i1);
//		ICFGBasicBlockNode B = new CFGBasicBlockNode("B1", mCFG);
//		B.addStatement(stmt1);
//		mCFG.addBasicBlockNode(B);
//
//		ConcreteConstant constant1 = new ConcreteConstant(5, mCFG);
//		GreaterThanExpression exp1 = new GreaterThanExpression(mCFG, constant1,
//				v1);
//		ICFGDecisionNode C = new CFGDecisionNode("C1", mCFG, exp1);
//		mCFG.addDecisionNode(C);
//
//		ICFGBasicBlockNode D = new CFGBasicBlockNode("D1", mCFG);
//		ConcreteConstant constant3 = new ConcreteConstant(16, mCFG);
//		Statement stmt2 = new Statement(mCFG, v1, constant3);
//		D.addStatement(stmt2);
//		mCFG.addBasicBlockNode(D);
//
//		ICFGBasicBlockNode E = new CFGBasicBlockNode("E1", mCFG);
//		ConcreteConstant constant4 = new ConcreteConstant(5, mCFG);
//		Statement stmt3 = new Statement(mCFG, v1, constant4);
//		D.addStatement(stmt3);
//		mCFG.addBasicBlockNode(E);
//
//		ConcreteConstant constant2 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG,
//				v1, constant2);
//		ICFGDecisionNode F = new CFGDecisionNode("F1", mCFG, exp2);
//		mCFG.addDecisionNode(F);
//
//		ICFGBasicBlockNode G = new CFGBasicBlockNode("G1", mCFG);
//		mCFG.addBasicBlockNode(G);
//
//		ICFGBasicBlockNode H = new CFGBasicBlockNode("H1", mCFG);
//		mCFG.addBasicBlockNode(H);
//
//		CFEdge e1 = new CFEdge("AB1", mCFG, A, B);
//		CFEdge e2 = new CFEdge("BC1", mCFG, B, C);
//		CFEdge e3 = new CFEdge("CD1", mCFG, C, D);
//		CFEdge e4 = new CFEdge("CE1", mCFG, C, E);
//		CFEdge e5 = new CFEdge("DF1", mCFG, D, F);
//		CFEdge e6 = new CFEdge("EF1", mCFG, E, F);
//		CFEdge e7 = new CFEdge("FG1", mCFG, F, G);
//		CFEdge e8 = new CFEdge("FH1", mCFG, F, H);
//		CFEdge e9 = new CFEdge("GI1", mCFG, G, I);
//		CFEdge e10 = new CFEdge("HI1", mCFG, H, I);
//		CFEdge e11 = new CFEdge("IA1", mCFG, I, A);
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
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(e3);
//		targets.add(e4);
//		targets.add(e6);
//		targets.add(e7);
//		targets.add(e8);
//
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 2);
//
//		System.out.println("Test Case 2 End\n\n");
//	}
//
//	//Test with a single decision to check Backtracking
//	@Test
//	public void testSingleDecision() throws Exception {
//		System.out.println("Test Case 2 Start\n\n");
//		ICFG mCFG = null;
//		CFGBasicBlockNode A = new CFGBasicBlockNode("A",null);
//		CFGBasicBlockNode I = new CFGBasicBlockNode("I", null);
//
//		mCFG = new CFG(A, I);
//		Variable v1 = new Variable("v11", mCFG);
//
//		Input i1 = new Input(mCFG);
//		Statement stmt1 = new Statement(mCFG, v1, i1);
//		ICFGBasicBlockNode B = new CFGBasicBlockNode("B1", mCFG);
//		B.addStatement(stmt1);
//		mCFG.addBasicBlockNode(B);
//
//
//		ICFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
//		ConcreteConstant constant3 = new ConcreteConstant(5, mCFG);
//		Statement stmt2 = new Statement(mCFG, v1, constant3);
//		C.addStatement(stmt2);
//		mCFG.addBasicBlockNode(C);
//
//		ConcreteConstant constant2 = new ConcreteConstant(15, mCFG);
//		GreaterThanExpression exp2 = new GreaterThanExpression(mCFG,
//				v1, constant2);
//		ICFGDecisionNode D = new CFGDecisionNode("D", mCFG, exp2);
//		mCFG.addDecisionNode(D);
//
//		ICFGBasicBlockNode E = new CFGBasicBlockNode("E", mCFG);
//		Statement stmt3 = new Statement(mCFG, v1, new ConcreteConstant(10, mCFG));
//		C.addStatement(stmt3);
//		mCFG.addBasicBlockNode(E);
//
//		ICFGBasicBlockNode F = new CFGBasicBlockNode("F", mCFG);
//		Statement stmt4 = new Statement(mCFG, v1, new ConcreteConstant(15, mCFG));
//		C.addStatement(stmt4);
//		mCFG.addBasicBlockNode(F);
//
//
//
//		CFEdge e1 = new CFEdge("AB", mCFG, A, B);
//		CFEdge e2 = new CFEdge("BC", mCFG, B, C);
//		CFEdge e3 = new CFEdge("CD", mCFG, C, D);
//		CFEdge e4 = new CFEdge("DE", mCFG, D, E);
//		CFEdge e5 = new CFEdge("DF", mCFG, D, F);
//		CFEdge e6 = new CFEdge("EI", mCFG, E, I);
//		CFEdge e7 = new CFEdge("FI", mCFG, F, I);
//		CFEdge e8 = new CFEdge("ID", mCFG, I, D);
//
//		mCFG.addEdge(e1);
//		mCFG.addEdge(e2);
//		mCFG.addEdge(e3);
//		mCFG.addEdge(e4);
//		mCFG.addEdge(e5);
//		mCFG.addEdge(e6);
//		mCFG.addEdge(e7);
//		mCFG.addEdge(e8);
//
//		D.setElseEdge(e5);
//		D.setThenEdge(e4);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(e3);
//		targets.add(e7);
//		targets.add(e8);
//
//		SymTest tester = new SymTest(mCFG, targets);
//		TestSequence seq = tester.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		Assert.assertEquals(testseq.size(), 1);
//		Assert.assertTrue(testseq.containsKey(v1));
//		Assert.assertEquals(testseq.get(v1).size(), 2);
//
//		System.out.println("Test Case 3 End\n\n");
//	}
//
//
//}
