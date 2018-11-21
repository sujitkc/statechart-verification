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
///**
// * Test cases for elevator.c in /resources
// * @author pavithra
// *
// */
//public class TestElevator {
//
//	//stationary = 0 up = 1 down = -1
//	@Test
//	public void testFloorInput() throws Exception {
//
//		Set<ICFEdge> targets;
//
//		//create CFG
//		System.out.println("Test Case 1 Start\n\n");
//		ICFG mCFG = null;
//		CFGBasicBlockNode A = new CFGBasicBlockNode("A",null);
//		CFGBasicBlockNode W = new CFGBasicBlockNode("W", null);
//		mCFG = new CFG(A, W);
//		//constants
//		ConcreteConstant constant1 = new ConcreteConstant(-1, mCFG);
//		ConcreteConstant constant2 = new ConcreteConstant(0, mCFG);
//		ConcreteConstant constant3 = new ConcreteConstant(1, mCFG);
////----------------------------------------------------------------------
//		//Basic Blocks
//
//		ICFGBasicBlockNode B = new CFGBasicBlockNode("B", mCFG);
//		Variable floorInput = new Variable("floorInput", mCFG);
//		Input i1 = new Input(mCFG);
//		Statement stmt1 = new Statement(mCFG, floorInput, i1);
//		B.addStatement(stmt1);
//
//		Variable shouldStop = new Variable("shouldStop", mCFG);
//		Input i2 = new Input(mCFG);
//		Statement stmt2 = new Statement(mCFG, shouldStop, i2);
//		B.addStatement(stmt2);
//
//		Variable direction = new Variable("direction", mCFG);
//		Input i3 = new Input(mCFG);
//		Statement stmt4 = new Statement(mCFG, direction, i3);
//		B.addStatement(stmt4);
//
//		Variable checkMax = new Variable("checkMax", mCFG);
//		Input i4 = new Input(mCFG);
//		Statement stmt6 = new Statement(mCFG, checkMax, i4);
//		B.addStatement(stmt6);
//
//		Variable checkMin = new Variable("checkMin", mCFG);
//		Input i5 = new Input(mCFG);
//		Statement stmt7 = new Statement(mCFG, checkMin, i5);
//		B.addStatement(stmt7);
//
//		Variable req1 = new Variable("Request", mCFG);
//		Input i6 = new Input(mCFG);
//		Statement stmt19 = new Statement(mCFG, req1, i6);
//		B.addStatement(stmt19);
//
//		Variable position = new Variable("position", mCFG);
//		Input i7 = new Input(mCFG);
//		Statement stmt21 = new Statement(mCFG, position, i7);
//		B.addStatement(stmt21);
//
//		Variable min = new Variable("min", mCFG);
//		Statement stmt22 = new Statement(mCFG, min, new ConcreteConstant(10, mCFG));
//		B.addStatement(stmt22);
//
//		Variable max = new Variable("max", mCFG);
//		Statement stmt23 = new Statement(mCFG, max, constant2);
//		B.addStatement(stmt23);
//
//
//		mCFG.addBasicBlockNode(B);
////--------------------------------------------------------------
//
//		ICFGBasicBlockNode J = new CFGBasicBlockNode("J", mCFG);
//		Statement stmt3 = new Statement(mCFG, shouldStop, constant1);
//		J.addStatement(stmt3);
//
//		Statement stmt5 = new Statement(mCFG, direction, constant2);
//		J.addStatement(stmt5);
//
//		mCFG.addBasicBlockNode(J);
////--------------------------------------------------------------------
//		ICFGBasicBlockNode G = new CFGBasicBlockNode("G", mCFG);
//		Statement stmt8 = new Statement(mCFG, direction, constant3);
//		G.addStatement(stmt8);
//		Statement stmt9 = new Statement(mCFG, checkMax, constant3);
//		G.addStatement(stmt9);
//		Statement stmt10 = new Statement(mCFG, max, floorInput);
//		G.addStatement(stmt10);
//		mCFG.addBasicBlockNode(G);
////--------------------------------------------------------------------
//		ICFGBasicBlockNode I = new CFGBasicBlockNode("I", mCFG);
//		Statement stmt11 = new Statement(mCFG, direction, constant1);
//		I.addStatement(stmt11);
//		Statement stmt12 = new Statement(mCFG, checkMin, constant3);
//		I.addStatement(stmt12);
//
//		Statement stmt13 = new Statement(mCFG, min, floorInput);
//		I.addStatement(stmt13);
//		mCFG.addBasicBlockNode(I);
////--------------------------------------------------------------------
//		ICFGBasicBlockNode M = new CFGBasicBlockNode("M", mCFG);
//		Statement stmt14 = new Statement(mCFG, checkMax, constant3);
//		M.addStatement(stmt14);
//		mCFG.addBasicBlockNode(M);
////--------------------------------------------------------------------
//		ICFGBasicBlockNode O = new CFGBasicBlockNode("O", mCFG);
//		Statement stmt15 = new Statement(mCFG, max, floorInput);
//		O.addStatement(stmt15);
//		mCFG.addBasicBlockNode(O);
////------------------------------------------------------------------------
//		ICFGBasicBlockNode P = new CFGBasicBlockNode("P", mCFG);
//		Statement stmt16 = new Statement(mCFG, checkMin, constant3);
//		P.addStatement(stmt16);
//		mCFG.addBasicBlockNode(P);
////------------------------------------------------------------------------
//		ICFGBasicBlockNode R = new CFGBasicBlockNode("R", mCFG);
//		Statement stmt17 = new Statement(mCFG, min, floorInput);
//		R.addStatement(stmt17);
//		mCFG.addBasicBlockNode(R);
////------------------------------------------------------------------------
//		ICFGBasicBlockNode T = new CFGBasicBlockNode("T", mCFG);
//		IExpression exp = new AddExpression(mCFG, shouldStop, constant3);
//		Statement stmt18 = new Statement(mCFG, shouldStop, exp);
//		T.addStatement(stmt18);
//		mCFG.addBasicBlockNode(T);
////------------------------------------------------------------------------
//		ICFGBasicBlockNode V = new CFGBasicBlockNode("V", mCFG);
//		Statement stmt20 = new Statement(mCFG, req1, constant3);
//		V.addStatement(stmt20);
////-------------------------------------------------------------------------
//		GreaterThanExpression gtexp = new GreaterThanExpression(mCFG, floorInput, constant1);
//		GreaterThanExpression gtexp2 = new GreaterThanExpression(mCFG, new ConcreteConstant(11, mCFG), floorInput);
//		AndExpression exp1 = new AndExpression(mCFG, gtexp, gtexp2);
//		ICFGDecisionNode C = new CFGDecisionNode("C", mCFG, exp1);
//		mCFG.addDecisionNode(C);
////---------------------------------------------------------------------------
//		EqualsExpression exp2 = new EqualsExpression(mCFG, shouldStop, constant2);
//		ICFGDecisionNode E = new CFGDecisionNode("E", mCFG, exp2);
//		mCFG.addDecisionNode(E);
////----------------------------------------------------------------------------
//		GreaterThanExpression exp3 = new GreaterThanExpression(mCFG, floorInput, new ConcreteConstant(5, mCFG));
//		ICFGDecisionNode F = new CFGDecisionNode("F", mCFG, exp3);
//		mCFG.addDecisionNode(F);
////-----------------------------------------------------------------------------
//		GreaterThanExpression exp4 = new GreaterThanExpression(mCFG, new ConcreteConstant(5, mCFG), floorInput);
//		ICFGDecisionNode H = new CFGDecisionNode("H", mCFG, exp4);
//		mCFG.addDecisionNode(H);
////-----------------------------------------------------------------------------------
//		GreaterThanExpression exp5 = new GreaterThanExpression(mCFG, shouldStop, constant2);
//		ICFGDecisionNode K = new CFGDecisionNode("K", mCFG, exp5);
//		mCFG.addDecisionNode(K);
////-------------------------------------------------------------------------------------
//		GreaterThanExpression exp6 = new GreaterThanExpression(mCFG, floorInput, position);
//		ICFGDecisionNode L = new CFGDecisionNode("L", mCFG, exp6);
//		mCFG.addDecisionNode(L);
////--------------------------------------------------------------------------------------
//		GreaterThanExpression exp7 = new GreaterThanExpression(mCFG, floorInput, max);
//		ICFGDecisionNode N = new CFGDecisionNode("N", mCFG, exp7);
//		mCFG.addDecisionNode(N);
////-------------------------------------------------------------------------------------
//		GreaterThanExpression exp8 = new GreaterThanExpression(mCFG, min, floorInput);
//		ICFGDecisionNode Q = new CFGDecisionNode("Q", mCFG, exp8);
//		mCFG.addDecisionNode(Q);
////----------------------------------------------------------------------------
//		EqualsExpression exp9 = new EqualsExpression(mCFG, req1, constant2);
//		ICFGDecisionNode S = new CFGDecisionNode("S", mCFG, exp9);
//		mCFG.addDecisionNode(S);
////-------------------------------------------------------------------------
//		GreaterThanExpression exp10 = new GreaterThanExpression(mCFG, shouldStop, constant2);
//		ICFGDecisionNode U = new CFGDecisionNode("U", mCFG, exp10);
//		mCFG.addDecisionNode(U);
////---------------------------------------------------------------------------------------
//		//Edges
//
//		ICFEdge AB = new CFEdge("AB", mCFG, A, B);
//		mCFG.addEdge(AB);
//		ICFEdge BC = new CFEdge("BC", mCFG, B, C);
//		mCFG.addEdge(BC);
//		ICFEdge CW = new CFEdge("CW", mCFG, C, W);
//		mCFG.addEdge(CW);
//		ICFEdge CE = new CFEdge("CE", mCFG, C, E);
//		mCFG.addEdge(CE);
//		ICFEdge EF = new CFEdge("EF", mCFG, E, F);
//		mCFG.addEdge(EF);
//		ICFEdge EK = new CFEdge("EK", mCFG, E, K);
//		mCFG.addEdge(EK);
//		ICFEdge FG = new CFEdge("FG", mCFG, F, G);
//		mCFG.addEdge(FG);
//		ICFEdge FH = new CFEdge("FH", mCFG, F, H);
//		mCFG.addEdge(FH);
//		ICFEdge HJ = new CFEdge("HJ", mCFG, H, J);
//		mCFG.addEdge(HJ);
//		ICFEdge HI = new CFEdge("HI", mCFG, H, I);
//		mCFG.addEdge(HI);
//		ICFEdge GS = new CFEdge("GS", mCFG, G, S);
//		mCFG.addEdge(GS);
//		ICFEdge IS = new CFEdge("IS",mCFG, I,S);
//		mCFG.addEdge(IS);
//		ICFEdge JS = new CFEdge("JS", mCFG, J, S);
//		mCFG.addEdge(JS);
//		ICFEdge KL = new CFEdge("KL", mCFG, K, L);
//		mCFG.addEdge(KL);
//		ICFEdge KS = new CFEdge("KS", mCFG, K, S);
//		mCFG.addEdge(KS);
//		ICFEdge LM = new CFEdge("LM", mCFG, L, M);
//		mCFG.addEdge(LM);
//		ICFEdge LP = new CFEdge("LP", mCFG, L, P);
//		mCFG.addEdge(LP);
//		ICFEdge MN = new CFEdge("MN", mCFG, M, N);
//		mCFG.addEdge(MN);
//		ICFEdge NO = new CFEdge("NO", mCFG, N, O);
//		mCFG.addEdge(NO);
//		ICFEdge NS = new CFEdge("NS", mCFG, N, S);
//		mCFG.addEdge(NS);
//		ICFEdge OS = new CFEdge("OS", mCFG, O, S);
//		mCFG.addEdge(OS);
//		ICFEdge PQ = new CFEdge("PQ", mCFG, P, Q);
//		mCFG.addEdge(PQ);
//		ICFEdge QR = new CFEdge("QR", mCFG, Q, R);
//		mCFG.addEdge(QR);
//		ICFEdge QS = new CFEdge("QS", mCFG, Q, S);
//		mCFG.addEdge(QS);
//		ICFEdge RS = new CFEdge("RS", mCFG, R, S);
//		mCFG.addEdge(RS);
//		ICFEdge ST = new CFEdge("ST", mCFG, S, T);
//		mCFG.addEdge(ST);
//		ICFEdge TU = new CFEdge("TU", mCFG, T, U);
//		mCFG.addEdge(TU);
//		ICFEdge UV = new CFEdge("UV", mCFG, U, V);
//		mCFG.addEdge(UV);
//		ICFEdge SW = new CFEdge("SW", mCFG, S, W);
//		mCFG.addEdge(SW);
//		ICFEdge UW = new CFEdge("UW", mCFG, U, W);
//		mCFG.addEdge(UW);
//		ICFEdge VW = new CFEdge("VW", mCFG, V, W);
//		mCFG.addEdge(VW);
//
//		//Looping Edge
//		ICFEdge WA = new CFEdge("WA", mCFG, W, A);
//		mCFG.addEdge(WA);
//
//		//Update conditional flow edges
//		C.setElseEdge(CW);
//		C.setThenEdge(CE);
//		E.setElseEdge(EK);
//		E.setThenEdge(EF);
//		F.setElseEdge(FH);
//		F.setThenEdge(FG);
//		H.setElseEdge(HI);
//		H.setThenEdge(HJ);
//		K.setElseEdge(KS);
//		K.setThenEdge(KL);
//		L.setElseEdge(LM);
//		L.setThenEdge(LP);
//		N.setElseEdge(NS);
//		N.setThenEdge(NO);
//		Q.setElseEdge(QS);
//		Q.setThenEdge(QR);
//		S.setElseEdge(SW);
//		S.setThenEdge(ST);
//		U.setThenEdge(UV);
//		U.setElseEdge(UW);
//
//		targets = new HashSet<ICFEdge>();
//		targets.add(BC);
//		targets.add(CE);
//		targets.add(CW);
//		targets.add(EF);
//		targets.add(EK);
//		targets.add(FG);
//		targets.add(FH);
//		targets.add(HI);
//		targets.add(HJ);
//		targets.add(KS);
//		targets.add(SW);
//		targets.add(ST);
//		targets.add(LP);
//		targets.add(KL);
//		targets.add(LM);
//		targets.add(QR);
//		targets.add(UV);
//		targets.add(QS);
//		targets.add(UW);
//		targets.add(NO);
//		targets.add(NS);
//		SymTest st = new SymTest(mCFG, targets);
//		TestSequence seq = st.generateTestSequence();
//		System.out.println(seq);
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//		System.out.println(testseq);
//
//	}
//
//}
