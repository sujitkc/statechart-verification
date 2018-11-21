//package testcases;
//
//import cfg.ICFEdge;
//import cfg.ICFG;
//import cfg.ICFGBasicBlockNode;
//import cfg.ICFGDecisionNode;
//import expression.*;
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
//public class TestTimer {
//	@Test
//	public void testTimer() throws Exception {
//
//		System.out.println("Test Case 1 Start\n\n");
//		ICFG mCFG = null;
//		ICFGBasicBlockNode A = new CFGBasicBlockNode("A",null);
//		ICFGBasicBlockNode W = new CFGBasicBlockNode("W",null);
//		mCFG = new CFG(A, W);
////		System.out.println(mCFG);
//
//		//constants
//		ConcreteConstant constant_zero = new ConcreteConstant(0, mCFG);
//		ConcreteConstant constant_one = new ConcreteConstant(1, mCFG);
//		ConcreteConstant constant_c = new ConcreteConstant(10, mCFG);
//		ConcreteConstant constant_decrement = new ConcreteConstant(-1, mCFG);
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode(mCFG);
//
//		Variable x5 = new Variable("x5", mCFG);
//		Variable start = new Variable("start", mCFG);
//		Statement stmt1 = new Statement(mCFG, start, constant_zero);
//		B.addStatement(stmt1);
//
//		Variable prev_start = new Variable("prev_start", mCFG);
//		Statement stmt2 = new Statement(mCFG, prev_start, constant_zero);
//		B.addStatement(stmt2);
//
//		Variable prev_out = new Variable("prev_out", mCFG);
//		Statement stmt3 = new Statement(mCFG, prev_out, constant_zero);
//		B.addStatement(stmt3);
//
//		Variable out = new Variable("out", mCFG);
//		Statement stmt4 = new Statement(mCFG, out, constant_zero);
//		B.addStatement(stmt4);
//
//		Variable t_on = new Variable("t_on", mCFG);
//		Statement stmt5 = new Statement(mCFG, t_on, constant_zero);
//		B.addStatement(stmt5);
//		mCFG.addBasicBlockNode(B);
//
//		ICFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
//		Input i1 = new Input(mCFG);
//		Statement stmt6 = new Statement(mCFG, start, i1);
//		C.addStatement(stmt6);
//		mCFG.addBasicBlockNode(C);
//
//		ICFGBasicBlockNode E = new CFGBasicBlockNode("E", mCFG);
//		Statement stmt7 = new Statement(mCFG, x5, constant_one);
//		E.addStatement(stmt7);
//		mCFG.addBasicBlockNode(E);
//
//		ICFGBasicBlockNode F = new CFGBasicBlockNode("F", mCFG);
//		Statement stmt8 = new Statement(mCFG, x5, constant_zero);
//		F.addStatement(stmt8);
//		mCFG.addBasicBlockNode(F);
//
//		ICFGBasicBlockNode H = new CFGBasicBlockNode("H", mCFG);
//		Statement stmt9 = new Statement(mCFG, out, constant_c);
//		H.addStatement(stmt9);
//		mCFG.addBasicBlockNode(H);
//
//		ICFGBasicBlockNode K = new CFGBasicBlockNode("K", mCFG);
//		Statement stmt10 = new Statement(mCFG, out, prev_out);
//		K.addStatement(stmt10);
//		mCFG.addBasicBlockNode(K);
//
//		ICFGBasicBlockNode J = new CFGBasicBlockNode("J", mCFG);
//		AddExpression exp1 = new AddExpression(mCFG, prev_out, constant_decrement);
//		Statement stmt11 = new Statement(mCFG, out, exp1);
//		J.addStatement(stmt11);
//		mCFG.addBasicBlockNode(J);
//
//		ICFGBasicBlockNode M = new CFGBasicBlockNode("M", mCFG);
//		Statement stmt12 = new Statement(mCFG, t_on, constant_one);
//		M.addStatement(stmt12);
//		mCFG.addBasicBlockNode(M);
//
//		ICFGBasicBlockNode N = new CFGBasicBlockNode("N", mCFG);
//		Statement stmt13 = new Statement(mCFG, t_on, constant_zero);
//		N.addStatement(stmt13);
//		mCFG.addBasicBlockNode(N);
//
//		ICFGBasicBlockNode O = new CFGBasicBlockNode("O", mCFG);
//		Statement stmt14 = new Statement(mCFG, prev_start, start);
//		O.addStatement(stmt14);
//		Statement stmt15 = new Statement(mCFG, prev_out, out);
//		O.addStatement(stmt15);
//		mCFG.addBasicBlockNode(O);
//
//		//decision nodes
//		AddExpression exp2 = new AddExpression(mCFG, start, constant_decrement);
//		EqualsExpression exp3 = new EqualsExpression(mCFG, prev_start, exp2);
//		ICFGDecisionNode D = new CFGDecisionNode("D", mCFG, exp3);
//		mCFG.addDecisionNode(D);
//
//		EqualsExpression exp4 = new EqualsExpression(mCFG, x5, constant_one);
//		ICFGDecisionNode G = new CFGDecisionNode("G", mCFG, exp4);
//		mCFG.addDecisionNode(G);
//
//		EqualsExpression exp5 = new EqualsExpression(mCFG, prev_out, constant_zero);
//		ICFGDecisionNode I = new CFGDecisionNode("I", mCFG, exp5);
//		mCFG.addDecisionNode(I);
//
//		EqualsExpression exp6 = new EqualsExpression(mCFG, prev_out, out);
//		ICFGDecisionNode L = new CFGDecisionNode("L", mCFG, exp6);
//		mCFG.addDecisionNode(L);
//
//
//		//edges
//		ICFEdge AB = new CFEdge("AB", mCFG, A, B);
//		ICFEdge BC = new CFEdge("BC", mCFG, B, C);
//		ICFEdge CD = new CFEdge("CD", mCFG, C, D);
//		ICFEdge DE = new CFEdge("DE", mCFG, D, E);
//		ICFEdge DF = new CFEdge("DF", mCFG, D, F);
//		ICFEdge EG = new CFEdge("EG", mCFG, E, G);
//		ICFEdge FG = new CFEdge("FG", mCFG, F, G);
//		ICFEdge GH = new CFEdge("GH", mCFG, G, H);
//		ICFEdge GI = new CFEdge("GI", mCFG, G, I);
//		ICFEdge IJ = new CFEdge("IJ", mCFG, I, J);
//		ICFEdge IK = new CFEdge("IK", mCFG, I, K);
//		ICFEdge HL = new CFEdge("HL", mCFG, H, L);
//		ICFEdge JL = new CFEdge("JL", mCFG, J, L);
//		ICFEdge KL = new CFEdge("KL", mCFG, K, L);
//		ICFEdge LM = new CFEdge("LM", mCFG, L, M);
//		ICFEdge LN = new CFEdge("LN", mCFG, L, N);
//		ICFEdge MO = new CFEdge("MO", mCFG, M, O);
//		ICFEdge NO = new CFEdge("NO", mCFG, N, O);
//		ICFEdge OW = new CFEdge("OW", mCFG, O, W);
//		mCFG.addEdge(AB);
//		mCFG.addEdge(BC);
//		mCFG.addEdge(CD);
//		mCFG.addEdge(DE);
//		mCFG.addEdge(DF);
//		mCFG.addEdge(EG);
//		mCFG.addEdge(FG);
//		mCFG.addEdge(GH);
//		mCFG.addEdge(GI);
//		mCFG.addEdge(IJ);
//		mCFG.addEdge(IK);
//		mCFG.addEdge(HL);
//		mCFG.addEdge(JL);
//		mCFG.addEdge(KL);
//		mCFG.addEdge(LM);
//		mCFG.addEdge(LN);
//		mCFG.addEdge(MO);
//		mCFG.addEdge(NO);
//		mCFG.addEdge(OW);
//		//Looping Edge
//		ICFEdge WC = new CFEdge("WC", mCFG, W, C);
//		mCFG.addEdge(WC);
//		// decision edges
//		D.setThenEdge(DE);
//		D.setElseEdge(DF);
//		G.setThenEdge(GH);
//		G.setElseEdge(GI);
//		I.setThenEdge(IK);
//		I.setElseEdge(IJ);
//		L.setElseEdge(LM);
//		L.setThenEdge(LN);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(CD);
////		targets.add(DE);
////		targets.add(EG);
//		targets.add(GH);
////		targets.add(HL);
////		targets.add(DF);
////		targets.add(IJ);
//		targets.add(IK);
//
//
//		SymTest st = new SymTest(mCFG, targets);
//		TestSequence seq = st.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//
//
//
//
//
//
//	}
//}
