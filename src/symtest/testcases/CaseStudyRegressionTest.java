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
//public class CaseStudyRegressionTest {
//
//	@Test
//	public void testCruiseControl_RT() throws Exception {
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
//		Set<ICFEdge> rt1_targets = new HashSet<ICFEdge>();
//		rt1_targets.add(d1_d1then);
//		rt1_targets.add(d2_d2else);
//		rt1_targets.add(d3_d3else);
//
//		SymTest st = new SymTest(mCFG, rt1_targets);
//		TestSequence seq = st.generateTestSequence();
//		System.out.println(seq);
//
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//	}
//
//}
