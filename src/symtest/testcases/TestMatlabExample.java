//package testcases;
//
//import cfg.ICFEdge;
//import cfg.ICFG;
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
//public class TestMatlabExample {
//
//	@Test
//	public void testMatlabExample() throws Exception {
//
//		// create CFG
//		System.out.println("Test Case 1 Start\n\n");
//		ICFG mCFG = null;
//		CFGBasicBlockNode A = new CFGBasicBlockNode("A", null);
//		CFGBasicBlockNode Y = new CFGBasicBlockNode("Y", null);
//		mCFG = new CFG(A, Y);
//
//		// constants
//		ConcreteConstant constant1 = new ConcreteConstant(5, mCFG);
//		ConcreteConstant constant2 = new ConcreteConstant(10, mCFG);
//		ConcreteConstant constant3 = new ConcreteConstant(15, mCFG);
//		ConcreteConstant constant4 = new ConcreteConstant(20, mCFG);
//		ConcreteConstant constant5 = new ConcreteConstant(25, mCFG);
//		ConcreteConstant constant7 = new ConcreteConstant(35, mCFG);
//		ConcreteConstant constant8 = new ConcreteConstant(40, mCFG);
//
//		ConcreteConstant trueconstant = new ConcreteConstant(1, mCFG);
//		ConcreteConstant falseconstant = new ConcreteConstant(0, mCFG);
//
//		// variables
//		Variable v1 = new Variable("v1", mCFG);
//		Variable v2 = new Variable("v2", mCFG);
//		Variable v3 = new Variable("v3", mCFG);
//		Variable v4 = new Variable("v4", mCFG);
//		Variable v5 = new Variable("v5", mCFG);
//		Variable v6 = new Variable("v6", mCFG);
//		Variable v7 = new Variable("v7", mCFG);
//		Variable v8 = new Variable("v8", mCFG);
//		Variable v9 = new Variable("v9", mCFG);
//		Variable v10 = new Variable("v10", mCFG);
//		Variable v14 = new Variable("v14", mCFG);
//
//		// ----------------------------------------------------------------------
//
//		// CFGBasicBlockNode B = new CFGBasicBlockNode("B", mCFG);
//		// mCFG.addBasicBlockNode(B);
//
//		CFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
//		Variable i1 = new Variable("i1", mCFG);
//		Input ip1 = new Input(mCFG);
//		C.addStatement(new Statement(mCFG, i1, ip1));
//
//		Variable i2 = new Variable("i2", mCFG);
//		Input ip2 = new Input(mCFG);
//		C.addStatement(new Statement(mCFG, i2, ip2));
//
//		Variable i3 = new Variable("i3", mCFG);
//		Input ip3 = new Input(mCFG);
//		C.addStatement(new Statement(mCFG, i3, ip3));
//
//		Variable i4 = new Variable("i4", mCFG);
//		Input ip4 = new Input(mCFG);
//		C.addStatement(new Statement(mCFG, i4, ip4));
//
//		// ----------------------------------------------------------------------
//
//		EqualsExpression exp1 = new EqualsExpression(mCFG, i1, constant1);
//		CFGDecisionNode D = new CFGDecisionNode("D", mCFG, exp1);
//		mCFG.addDecisionNode(D);
//
//		CFGBasicBlockNode E = new CFGBasicBlockNode("E", mCFG);
//		E.addStatement(new Statement(mCFG, v1, trueconstant));
//		mCFG.addBasicBlockNode(E);
//
//		CFGBasicBlockNode F = new CFGBasicBlockNode("F", mCFG);
//		F.addStatement(new Statement(mCFG, v1, falseconstant));
//		mCFG.addBasicBlockNode(F);
//
//		EqualsExpression exp2 = new EqualsExpression(mCFG, i3, constant2);
//		CFGDecisionNode G = new CFGDecisionNode("G", mCFG, exp2);
//		mCFG.addDecisionNode(G);
//
//		CFGBasicBlockNode H = new CFGBasicBlockNode("H", mCFG);
//		H.addStatement(new Statement(mCFG, v3, trueconstant));
//		mCFG.addBasicBlockNode(H);
//
//		CFGBasicBlockNode I = new CFGBasicBlockNode("I", mCFG);
//		I.addStatement(new Statement(mCFG, v3, falseconstant));
//		mCFG.addBasicBlockNode(I);
//
//		EqualsExpression exp3 = new EqualsExpression(mCFG, i2, constant3);
//		CFGDecisionNode J = new CFGDecisionNode("J", mCFG, exp3);
//		mCFG.addDecisionNode(J);
//
//		CFGBasicBlockNode K = new CFGBasicBlockNode("K", mCFG);
//		K.addStatement(new Statement(mCFG, v4, trueconstant));
//		mCFG.addBasicBlockNode(K);
//
//		CFGBasicBlockNode L = new CFGBasicBlockNode("L", mCFG);
//		L.addStatement(new Statement(mCFG, v4, falseconstant));
//		mCFG.addBasicBlockNode(L);
//
//		EqualsExpression exp4 = new EqualsExpression(mCFG, i2, constant4);
//		CFGDecisionNode M = new CFGDecisionNode("M", mCFG, exp4);
//		mCFG.addDecisionNode(M);
//
//		CFGBasicBlockNode N = new CFGBasicBlockNode("N", mCFG);
//		N.addStatement(new Statement(mCFG, v5, trueconstant));
//		mCFG.addBasicBlockNode(N);
//
//		CFGBasicBlockNode O = new CFGBasicBlockNode("O", mCFG);
//		O.addStatement(new Statement(mCFG, v5, falseconstant));
//		mCFG.addBasicBlockNode(O);
//
//		EqualsExpression exp5 = new EqualsExpression(mCFG, i3, i4);
//		CFGDecisionNode P = new CFGDecisionNode("P", mCFG, exp5);
//		mCFG.addDecisionNode(P);
//
//		CFGBasicBlockNode Q = new CFGBasicBlockNode("Q", mCFG);
//		Q.addStatement(new Statement(mCFG, v2, trueconstant));
//		mCFG.addBasicBlockNode(Q);
//
//		CFGBasicBlockNode R = new CFGBasicBlockNode("R", mCFG);
//		R.addStatement(new Statement(mCFG, v2, falseconstant));
//		mCFG.addBasicBlockNode(R);
//
//		CFGBasicBlockNode S = new CFGBasicBlockNode("S", mCFG);
//		S.addStatement(new Statement(mCFG, v6, new AddExpression(mCFG, v4, v5)));
//		S.addStatement(new Statement(mCFG, v7, new AddExpression(mCFG, v2,
//				new AddExpression(mCFG, v3, v6))));
//		// making C6 = true
//		S.addStatement(new Statement(mCFG, v9, new AddExpression(mCFG, v6,
//				trueconstant)));
//		S.addStatement(new Statement(mCFG, v10, new AddExpression(mCFG, v1, v7)));
//		mCFG.addBasicBlockNode(S);
//		// Since it is an 'or' expression expected, v10 > 1 for it to be true.
//		GreaterThanExpression exp6 = new GreaterThanExpression(mCFG, v10,
//				trueconstant);
//		CFGDecisionNode T = new CFGDecisionNode("T", mCFG, exp6);
//		mCFG.addDecisionNode(T);
//
//		CFGBasicBlockNode U = new CFGBasicBlockNode("U", mCFG);
//		U.addStatement(new Statement(mCFG, v14, constant5));
//		mCFG.addBasicBlockNode(U);
//
//		GreaterThanExpression exp7 = new GreaterThanExpression(mCFG, v9,
//				trueconstant);
//		CFGDecisionNode V = new CFGDecisionNode("V", mCFG, exp7);
//		mCFG.addDecisionNode(V);
//
//		CFGBasicBlockNode W = new CFGBasicBlockNode("W", mCFG);
//		W.addStatement(new Statement(mCFG, v14, constant7));
//		mCFG.addBasicBlockNode(W);
//
//		CFGBasicBlockNode X = new CFGBasicBlockNode("X", mCFG);
//		X.addStatement(new Statement(mCFG, v14, constant8));
//		mCFG.addBasicBlockNode(X);
//
//		// Add Edges
//		// ICFEdge e1 = new CFEdge("AB", mCFG, A, B);
//		// ICFEdge e2 = new CFEdge("BC", mCFG, B, C);
//		ICFEdge AC = new CFEdge("AC", mCFG, A, C);
//		ICFEdge e3 = new CFEdge("CD", mCFG, C, D);
//		ICFEdge e4 = new CFEdge("DE", mCFG, D, E);
//		ICFEdge e5 = new CFEdge("DF", mCFG, D, F);
//		ICFEdge e6 = new CFEdge("EG", mCFG, E, G);
//		ICFEdge e7 = new CFEdge("FG", mCFG, F, G);
//		ICFEdge e8 = new CFEdge("GH", mCFG, G, H);
//		ICFEdge e9 = new CFEdge("GI", mCFG, G, I);
//		ICFEdge e10 = new CFEdge("HJ", mCFG, H, J);
//		ICFEdge e11 = new CFEdge("IJ", mCFG, I, J);
//		ICFEdge e12 = new CFEdge("JK", mCFG, J, K);
//		ICFEdge e13 = new CFEdge("JL", mCFG, J, L);
//		ICFEdge e14 = new CFEdge("KM", mCFG, K, M);
//		ICFEdge e15 = new CFEdge("LM", mCFG, L, M);
//		ICFEdge e16 = new CFEdge("MN", mCFG, M, N);
//		ICFEdge e17 = new CFEdge("MO", mCFG, M, O);
//		ICFEdge e18 = new CFEdge("NP", mCFG, N, P);
//		ICFEdge e19 = new CFEdge("OP", mCFG, O, P);
//		ICFEdge e20 = new CFEdge("PQ", mCFG, P, Q);
//		ICFEdge e21 = new CFEdge("PR", mCFG, P, R);
//		ICFEdge e22 = new CFEdge("QS", mCFG, Q, S);
//		ICFEdge e23 = new CFEdge("RS", mCFG, R, S);
//		ICFEdge e24 = new CFEdge("ST", mCFG, S, T);
//		ICFEdge e25 = new CFEdge("TU", mCFG, T, U);
//		ICFEdge e26 = new CFEdge("TV", mCFG, T, V);
//		ICFEdge e27 = new CFEdge("VW", mCFG, V, W);
//		ICFEdge e28 = new CFEdge("VX", mCFG, V, X);
//		ICFEdge e29 = new CFEdge("UY", mCFG, U, Y);
//		ICFEdge e30 = new CFEdge("WY", mCFG, W, Y);
//		ICFEdge e31 = new CFEdge("XY", mCFG, X, Y);
//
//		// ICFEdge e18 = new CFEdge("NY", mCFG, N,Y);
//		// ICFEdge e19 = new CFEdge("OY", mCFG, O,Y);
//		ICFEdge e32 = new CFEdge("YA", mCFG, Y, A);
//
//		mCFG.addEdge(AC);
//		// mCFG.addEdge(e2);
//		mCFG.addEdge(e3);
//		mCFG.addEdge(e4);
//		mCFG.addEdge(e5);
//		mCFG.addEdge(e6);
//		mCFG.addEdge(e7);
//		mCFG.addEdge(e8);
//		mCFG.addEdge(e9);
//		mCFG.addEdge(e10);
//		mCFG.addEdge(e11);
//		mCFG.addEdge(e12);
//		mCFG.addEdge(e13);
//		mCFG.addEdge(e14);
//		mCFG.addEdge(e15);
//		mCFG.addEdge(e16);
//		mCFG.addEdge(e17);
//		mCFG.addEdge(e18);
//		mCFG.addEdge(e19);
//		mCFG.addEdge(e20);
//		mCFG.addEdge(e21);
//		mCFG.addEdge(e22);
//		mCFG.addEdge(e23);
//		mCFG.addEdge(e24);
//		mCFG.addEdge(e25);
//		mCFG.addEdge(e26);
//		mCFG.addEdge(e27);
//		mCFG.addEdge(e28);
//		mCFG.addEdge(e29);
//		mCFG.addEdge(e30);
//		mCFG.addEdge(e31);
//		mCFG.addEdge(e32);
//
//		// decision edges
//		D.setThenEdge(e4);
//		D.setElseEdge(e5);
//		G.setThenEdge(e8);
//		G.setElseEdge(e9);
//		J.setThenEdge(e12);
//		J.setElseEdge(e13);
//		M.setThenEdge(e16);
//		M.setElseEdge(e17);
//		P.setThenEdge(e21);
//		P.setElseEdge(e20);
//		T.setThenEdge(e25);
//		T.setElseEdge(e26);
//		V.setThenEdge(e27);
//		V.setElseEdge(e28);
//
//		Set<ICFEdge> targets = new HashSet<ICFEdge>();
//		targets.add(e4);
//		targets.add(e8);
//		targets.add(e12);
//		// targets.add(e13);
//		targets.add(e16);
//		targets.add(e20);
//		targets.add(e25);
//
//		SymTest st = new SymTest(mCFG, targets);
//		TestSequence seq = st.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//
//	}
//
//	@Test
//	public void testCruiseControl() throws Exception {
//
//		ICFG mCFG = null;
//		CFGBasicBlockNode start = new CFGBasicBlockNode("start", null);
//		CFGBasicBlockNode stop = new CFGBasicBlockNode("stop", null);
//
//		mCFG = new CFG(start,stop);
//
//		BooleanVariable x3 = new BooleanVariable("x3", mCFG);
//		Variable x6 = new Variable("x6", mCFG);
//		Variable PIC_DTI_x3 = new Variable("PIC_DTI_x3", mCFG);
//		CFGBasicBlockNode init = new CFGBasicBlockNode("init", mCFG);
//		init.addStatement(new Statement(mCFG, x3, new False(mCFG)));
//		init.addStatement(new Statement(mCFG, x6, new ConcreteConstant(0, mCFG)));
//		init.addStatement(new Statement(mCFG, PIC_DTI_x3, new ConcreteConstant(0, mCFG)));
//		mCFG.addBasicBlockNode(init);
//
//		CFGBasicBlockNode mainloop = new CFGBasicBlockNode("mainloop", mCFG);
//		mCFG.addBasicBlockNode(mainloop);
//
//		CFGBasicBlockNode inputs = new CFGBasicBlockNode("inputs", mCFG);
//
//		BooleanVariable enable = new BooleanVariable("enable", mCFG);
//		BooleanVariable brake = new BooleanVariable("brake", mCFG);
//		Variable speed = new Variable("speed", mCFG);
//		BooleanVariable set = new BooleanVariable("set", mCFG);
//		Variable inc = new Variable("inc", mCFG);
//		Variable dec = new Variable("dec", mCFG);
//		inputs.addStatement(new Statement(mCFG, enable, new Input(mCFG)));
//		inputs.addStatement(new Statement(mCFG, brake, new Input(mCFG)));
//		inputs.addStatement(new Statement(mCFG, speed, new Input(mCFG)));
//		inputs.addStatement(new Statement(mCFG, set, new BooleanInput(mCFG)));
//		inputs.addStatement(new Statement(mCFG, inc, new Input(mCFG)));
//		inputs.addStatement(new Statement(mCFG, dec, new Input(mCFG)));
//		mCFG.addBasicBlockNode(inputs);
//
//		CFGBasicBlockNode fp1 = new CFGBasicBlockNode("fp1", mCFG);
//		BooleanVariable x1 = new BooleanVariable("x1", mCFG);
//		fp1.addStatement(new Statement(mCFG, x1, new NotExpression(mCFG, brake)));
//		BooleanVariable x8 = new BooleanVariable("x8", mCFG);
//		fp1.addStatement(new Statement(mCFG, x8, new OrExpression(mCFG, set, x3)));
//		BooleanVariable x2 = new BooleanVariable("x2", mCFG);
//		fp1.addStatement(new Statement(mCFG, x2, new AndExpression(mCFG, enable, new AndExpression(mCFG, x1, x8))));
//		Variable x9 = new Variable("x9", mCFG);
//		fp1.addStatement(new Statement(mCFG, x9, new AddExpression(mCFG, x6, new ConcreteConstant(1, mCFG))));
//		Variable x10 = new Variable("x10", mCFG);
//		fp1.addStatement(new Statement(mCFG, x10, new AddExpression(mCFG, x6, new ConcreteConstant(1, mCFG))));
//		mCFG.addBasicBlockNode(fp1);
//
//		Variable x7 = new Variable("x7", mCFG);
//		CFGDecisionNode d1 = new CFGDecisionNode("d1", mCFG, new NotExpression(mCFG, new EqualsExpression(mCFG, dec, new ConcreteConstant(0, mCFG))));
//		mCFG.addDecisionNode(d1);
//		CFGBasicBlockNode d1then = new CFGBasicBlockNode("d1then", mCFG);
//		mCFG.addBasicBlockNode(d1then);
//		d1then.addStatement(new Statement(mCFG, x7, x10));
//		CFGBasicBlockNode d1else = new CFGBasicBlockNode("d1else", mCFG);
//		d1else.addStatement(new Statement(mCFG, x7, x6));
//		mCFG.addBasicBlockNode(d1else);
//
//		Variable x4 = new Variable("x4", mCFG);
//		CFGDecisionNode d2 = new CFGDecisionNode("d2", mCFG, new NotExpression(mCFG, new EqualsExpression(mCFG, inc, new ConcreteConstant(0, mCFG))));
//		mCFG.addDecisionNode(d2);
//		CFGBasicBlockNode d2then = new CFGBasicBlockNode("d2then", mCFG);
//		mCFG.addBasicBlockNode(d2then);
//		d2then.addStatement(new Statement(mCFG, x4, x9));
//		CFGBasicBlockNode d2else = new CFGBasicBlockNode("d2else", mCFG);
//		mCFG.addBasicBlockNode(d2else);
//		d2then.addStatement(new Statement(mCFG, x4, x7));
//
//		Variable x5 = new Variable("x5", mCFG);
//		CFGDecisionNode d3 = new CFGDecisionNode("d3", mCFG, new NotExpression(mCFG, new EqualsExpression(mCFG, set, new False(mCFG))));
//		mCFG.addDecisionNode(d3);
//		CFGBasicBlockNode d3then = new CFGBasicBlockNode("d3then", mCFG);
//		mCFG.addBasicBlockNode(d3then);
//		d3then.addStatement(new Statement(mCFG, x5, speed));
//		CFGBasicBlockNode d3else = new CFGBasicBlockNode("d3else", mCFG);
//		mCFG.addBasicBlockNode(d3else);
//		d3else.addStatement(new Statement(mCFG, x5, x4));
//
//		CFGBasicBlockNode fp2 = new CFGBasicBlockNode("fp2", mCFG);
//		mCFG.addBasicBlockNode(fp2);
//		Variable error = new Variable("error", mCFG);
//		fp2.addStatement(new Statement(mCFG, error, new SubExpression(mCFG, x5, speed)));
//		Variable PIC_x1 = new Variable("PIC_x1", mCFG);
//		fp2.addStatement(new Statement(mCFG, PIC_x1, new MulExpression(mCFG, new ConcreteConstant(2, mCFG), error)));
//		Variable PIC_x3 = new Variable("PIC_x3", mCFG);
//		fp2.addStatement(new Statement(mCFG, PIC_x3, new AddExpression(mCFG, error, PIC_DTI_x3)));
//		Variable throttle = new Variable("throttle", mCFG);
//		fp2.addStatement(new Statement(mCFG, throttle, new AddExpression(mCFG, PIC_x1, PIC_x3)));
//
//
//		CFGBasicBlockNode delay = new CFGBasicBlockNode("delay", mCFG);
//		mCFG.addBasicBlockNode(delay);
//		delay.addStatement(new Statement(mCFG, PIC_DTI_x3, PIC_x3));
//		delay.addStatement(new Statement(mCFG, x6, x5));
//		delay.addStatement(new Statement(mCFG, x3, x2));
//
//		ICFEdge start_init = new CFEdge("start_init", mCFG, start, init);
//		mCFG.addEdge(start_init);
//
//		ICFEdge init_mainloop = new CFEdge("init_mainloop", mCFG, init, mainloop);
//		mCFG.addEdge(init_mainloop);
//
//		ICFEdge mainloop_inputs = new  CFEdge("mainloop_inputs", mCFG, mainloop, inputs);
//		mCFG.addEdge(mainloop_inputs);
//
//		ICFEdge inputs_fp1 = new  CFEdge("inputs_fp1", mCFG, inputs, fp1);
//		mCFG.addEdge(inputs_fp1);
//
//		ICFEdge fp1_d1 = new  CFEdge("fp1_d1", mCFG, fp1, d1);
//		mCFG.addEdge(fp1_d1);
//
//		ICFEdge d1_d1then = new  CFEdge("d1_d1then", mCFG, d1, d1then);
//		mCFG.addEdge(d1_d1then);
//
//		ICFEdge d1_d1else = new  CFEdge("d1_d1else", mCFG, d1, d1else);
//		mCFG.addEdge(d1_d1else);
//
//		d1.setThenEdge(d1_d1then);
//		d1.setElseEdge(d1_d1else);
//
//		ICFEdge d1then_d2 = new CFEdge("d1then_d2", mCFG, d1then, d2);
//		mCFG.addEdge(d1then_d2);
//
//		ICFEdge d1else_d2 = new CFEdge("d1else_d2", mCFG, d1else, d2);
//		mCFG.addEdge(d1else_d2);
//
//
//		ICFEdge d2_d2then = new  CFEdge("d2_d2then", mCFG, d2, d2then);
//		mCFG.addEdge(d2_d2then);
//
//		ICFEdge d2_d2else = new  CFEdge("d2_d2else", mCFG, d2, d2else);
//		mCFG.addEdge(d2_d2else);
//
//		d2.setThenEdge(d2_d2then);
//		d2.setElseEdge(d2_d2else);
//
//		ICFEdge d2then_d3 = new CFEdge("d2then_d3", mCFG, d2then, d3);
//		mCFG.addEdge(d2then_d3);
//
//		ICFEdge d2else_d3 = new CFEdge("d2else_d3", mCFG, d2else, d3);
//		mCFG.addEdge(d2else_d3);
//
//		ICFEdge d3_d3then = new  CFEdge("d3_d3then", mCFG, d3, d3then);
//		mCFG.addEdge(d3_d3then);
//
//		ICFEdge d3_d3else = new  CFEdge("d3_d3else", mCFG, d3, d3else);
//		mCFG.addEdge(d3_d3else);
//
//		d3.setThenEdge(d3_d3then);
//		d3.setElseEdge(d3_d3else);
//
//		ICFEdge d3then_fp2 = new CFEdge("d3then_fp2", mCFG, d3then, fp2);
//		mCFG.addEdge(d3then_fp2);
//
//		ICFEdge d3else_fp2 = new CFEdge("d3else_fp2", mCFG, d3else, fp2);
//		mCFG.addEdge(d3else_fp2);
//
//		ICFEdge fp2_delay = new CFEdge("fp2_delay", mCFG, fp2, delay);
//		mCFG.addEdge(fp2_delay);
//
//		ICFEdge delay_stop = new CFEdge("delay_stop", mCFG, delay, stop);
//		mCFG.addEdge(delay_stop);
//
//		ICFEdge stop_start = new CFEdge("stop_start", mCFG, stop, start);
//		mCFG.addEdge(stop_start);
//
//		Set<ICFEdge> targets = mCFG.getEdgeSet();
//		targets.remove(stop_start);
//		targets.remove(init_mainloop);
//		targets.remove(start_init);
//		SymTest st = new SymTest(mCFG, targets);
//		TestSequence seq = st.generateTestSequence();
//		System.out.println(seq);
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//	}
//}
