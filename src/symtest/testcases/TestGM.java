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
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class TestGM {
//
//	@Test
//	public void testGM() throws Exception {
//
//		ICFG mCFG = null;
//		CFGBasicBlockNode start = new CFGBasicBlockNode("start", null);
//		CFGBasicBlockNode stop = new CFGBasicBlockNode("stop", null);
//
//		mCFG = new CFG(start,stop);
//
//		CFGBasicBlockNode inputs = new CFGBasicBlockNode("inputs", mCFG);
//
//		Variable i1 = new Variable("i1", mCFG);
//		Variable i2 = new Variable("i2", mCFG);
//		Variable i3 = new Variable("i3", mCFG);
//		Variable i4 = new Variable("i4", mCFG);
//		inputs.addStatement(new Statement(mCFG, i1, new Input(mCFG)));
//		inputs.addStatement(new Statement(mCFG, i2, new Input(mCFG)));
//		inputs.addStatement(new Statement(mCFG, i3, new Input(mCFG)));
//		inputs.addStatement(new Statement(mCFG, i4, new Input(mCFG)));
//		mCFG.addBasicBlockNode(inputs);
//
//		CFGDecisionNode D1 = new CFGDecisionNode("D1", mCFG, new EqualsExpression(mCFG, i1, new ConcreteConstant(1, mCFG)));
//		mCFG.addDecisionNode(D1);
//
//		CFGBasicBlockNode D1then = new CFGBasicBlockNode("D1then", mCFG);
//		mCFG.addBasicBlockNode(D1then);
//		BooleanVariable v1 = new BooleanVariable("v1",mCFG);
//		D1then.addStatement(new Statement(mCFG, v1, new True(mCFG)));
//
//		CFGBasicBlockNode D1else = new CFGBasicBlockNode("D1else", mCFG);
//		mCFG.addBasicBlockNode(D1else);
//		D1then.addStatement(new Statement(mCFG, v1, new False(mCFG)));
//
//		CFGDecisionNode D2 = new CFGDecisionNode("D2", mCFG, new EqualsExpression(mCFG, i3, new ConcreteConstant(2, mCFG)));
//		mCFG.addDecisionNode(D2);
//
//		CFGBasicBlockNode D2then = new CFGBasicBlockNode("D2then", mCFG);
//		mCFG.addBasicBlockNode(D2then);
//		BooleanVariable v3 = new BooleanVariable("v3", mCFG);
//		D2then.addStatement(new Statement(mCFG, v3, new True(mCFG)));
//
//		CFGBasicBlockNode D2else = new CFGBasicBlockNode("D2else", mCFG);
//		mCFG.addBasicBlockNode(D2else);
//		D2then.addStatement(new Statement(mCFG, v3, new False(mCFG)));
//
//		CFGDecisionNode D3 = new CFGDecisionNode("D3", mCFG, new EqualsExpression(mCFG, i2, new ConcreteConstant(3, mCFG)));
//		mCFG.addDecisionNode(D3);
//
//		CFGBasicBlockNode D3then = new CFGBasicBlockNode("D3then", mCFG);
//		mCFG.addBasicBlockNode(D3then);
//		BooleanVariable v4 = new BooleanVariable("v4", mCFG);
//		D3then.addStatement(new Statement(mCFG, v4, new True(mCFG)));
//
//		CFGBasicBlockNode D3else = new CFGBasicBlockNode("D3else", mCFG);
//		mCFG.addBasicBlockNode(D3else);
//		D3then.addStatement(new Statement(mCFG, v4, new False(mCFG)));
//
//		CFGDecisionNode D4 = new CFGDecisionNode("D4", mCFG, new EqualsExpression(mCFG, i4, new ConcreteConstant(4, mCFG)));
//		mCFG.addDecisionNode(D4);
//
//		CFGBasicBlockNode D4then = new CFGBasicBlockNode("D4then", mCFG);
//		mCFG.addBasicBlockNode(D4then);
//		BooleanVariable v5 = new BooleanVariable("v5", mCFG);
//		D4then.addStatement(new Statement(mCFG, v5, new True(mCFG)));
//
//		CFGBasicBlockNode D4else = new CFGBasicBlockNode("D4else", mCFG);
//		mCFG.addBasicBlockNode(D4else);
//		D4else.addStatement(new Statement(mCFG, v5, new False(mCFG)));
//
//		CFGDecisionNode D5 = new CFGDecisionNode("D5", mCFG, new NotExpression(mCFG, new EqualsExpression(mCFG, i3, i4)));
//		mCFG.addDecisionNode(D4);
//
//		CFGBasicBlockNode D5then = new CFGBasicBlockNode("D5then", mCFG);
//		mCFG.addBasicBlockNode(D5then);
//		BooleanVariable v2 = new BooleanVariable("v2", mCFG);
//		D5then.addStatement(new Statement(mCFG, v2, new True(mCFG)));
//
//		CFGBasicBlockNode D5else = new CFGBasicBlockNode("D5else", mCFG);
//		mCFG.addBasicBlockNode(D5else);
//		D5else.addStatement(new Statement(mCFG, v2, new False(mCFG)));
//
//
//		CFGBasicBlockNode fp = new CFGBasicBlockNode("fp", mCFG);
//		mCFG.addBasicBlockNode(fp);
//		BooleanVariable v6 = new BooleanVariable("v6", mCFG);
//		D5then.addStatement(new Statement(mCFG, v6, new OrExpression(mCFG, v4, v5)));
//		BooleanVariable v7 = new BooleanVariable("v7", mCFG);
//		D5then.addStatement(new Statement(mCFG, v7, new OrExpression(mCFG, v2, new OrExpression(mCFG, v3, v6))));
//		BooleanVariable v9 = new BooleanVariable("v9", mCFG);
//		D5then.addStatement(new Statement(mCFG, v9, new OrExpression(mCFG, v6, new True(mCFG))));
//		BooleanVariable v10 = new BooleanVariable("v10", mCFG);
//		D5then.addStatement(new Statement(mCFG, v10, new AndExpression(mCFG, v1, v7)));
//
//
//		CFGDecisionNode D6 = new CFGDecisionNode("D6", mCFG, new EqualsExpression(mCFG, v10, new True(mCFG)));
//		mCFG.addDecisionNode(D6);
//
//		CFGBasicBlockNode D6then = new CFGBasicBlockNode("D6then", mCFG);
//		mCFG.addBasicBlockNode(D6then);
//		D6then.addStatement(new Statement(mCFG, v6, new True(mCFG)));
//
//		CFGDecisionNode D7 = new CFGDecisionNode("D7", mCFG, new EqualsExpression(mCFG, v9, new True(mCFG)));
//		mCFG.addDecisionNode(D7);
//
//		CFGBasicBlockNode D7then = new CFGBasicBlockNode("D7then", mCFG);
//		mCFG.addBasicBlockNode(D7then);
//		BooleanVariable v14 = new BooleanVariable("v14", mCFG);
//		D7then.addStatement(new Statement(mCFG, v14, new True(mCFG)));
//
//		CFGBasicBlockNode D7else = new CFGBasicBlockNode("D7else", mCFG);
//		mCFG.addBasicBlockNode(D7else);
//		D7else.addStatement(new Statement(mCFG, v14, new False(mCFG)));
//
//		ICFEdge start_inputs = new CFEdge("start_inputs", mCFG, start, inputs);
//		mCFG.addEdge(start_inputs);
//
//		ICFEdge inputs_D1 = new CFEdge("inputs_D1", mCFG, inputs, D1);
//		mCFG.addEdge(inputs_D1);
//
//		ICFEdge D1_D1then = new CFEdge("D1_D1then", mCFG, D1, D1then);
//		mCFG.addEdge(D1_D1then);
//
//		ICFEdge D1_D1else = new CFEdge("D1_D1else", mCFG, D1, D1else);
//		mCFG.addEdge(D1_D1else);
//
//		D1.setThenEdge(D1_D1then);
//		D1.setElseEdge(D1_D1else);
//
//		ICFEdge D1then_D2 = new CFEdge("D1then_D2", mCFG, D1then, D2);
//		mCFG.addEdge(D1then_D2);
//
//		ICFEdge D1else_D2 = new CFEdge("D1else_D2", mCFG, D1else, D2);
//		mCFG.addEdge(D1else_D2);
//
//		ICFEdge D2_D2then = new CFEdge("D2_D2then", mCFG, D2, D2then);
//		mCFG.addEdge(D2_D2then);
//
//		ICFEdge D2_D2else = new CFEdge("D2_D2else", mCFG, D2, D2else);
//		mCFG.addEdge(D2_D2else);
//
//		D2.setThenEdge(D2_D2then);
//		D2.setElseEdge(D2_D2else);
//
//		ICFEdge D2then_D3 = new CFEdge("D2then_D3", mCFG, D2then, D3);
//		mCFG.addEdge(D2then_D3);
//
//		ICFEdge D2else_D3 = new CFEdge("D2else_D3", mCFG, D2else, D3);
//		mCFG.addEdge(D2else_D3);
//
//		ICFEdge D3_D3then = new CFEdge("D3_D3then", mCFG, D3, D3then);
//		mCFG.addEdge(D3_D3then);
//
//		ICFEdge D3_D3else = new CFEdge("D3_D3else", mCFG, D3, D3else);
//		mCFG.addEdge(D3_D3else);
//
//		D3.setThenEdge(D3_D3then);
//		D3.setElseEdge(D3_D3else);
//
//		ICFEdge D3then_D4 = new CFEdge("D3then_D4", mCFG, D3then, D4);
//		mCFG.addEdge(D3then_D4);
//
//		ICFEdge D3else_D4 = new CFEdge("D3else_D4", mCFG, D3else, D4);
//		mCFG.addEdge(D3else_D4);
//
//		ICFEdge D4_D4then = new CFEdge("D4_D4then", mCFG, D4, D4then);
//		mCFG.addEdge(D4_D4then);
//
//		ICFEdge D4_D4else = new CFEdge("D4_D4else", mCFG, D4, D4else);
//		mCFG.addEdge(D4_D4else);
//
//		D4.setThenEdge(D4_D4then);
//		D4.setElseEdge(D4_D4else);
//
//		ICFEdge D4then_D5 = new CFEdge("D4then_D5", mCFG, D4then, D5);
//		mCFG.addEdge(D4then_D5);
//
//		ICFEdge D4else_D5 = new CFEdge("D4else_D5", mCFG, D4else, D5);
//		mCFG.addEdge(D4else_D5);
//
//		ICFEdge D5_D5then = new CFEdge("D5_D5then", mCFG, D5, D5then);
//		mCFG.addEdge(D5_D5then);
//
//		ICFEdge D5_D5else = new CFEdge("D5_D5else", mCFG, D5, D5else);
//		mCFG.addEdge(D5_D5else);
//
//		D5.setThenEdge(D5_D5then);
//		D5.setElseEdge(D5_D5else);
//
//		ICFEdge D5then_fp = new CFEdge("D5then_fp", mCFG, D5then, fp);
//		mCFG.addEdge(D5then_fp);
//
//		ICFEdge D5else_fp = new CFEdge("D5else_fp", mCFG, D5else, fp);
//		mCFG.addEdge(D5else_fp);
//
//		ICFEdge fp_D6 = new CFEdge("fp_D6", mCFG, fp, D6);
//		mCFG.addEdge(fp_D6);
//
//		ICFEdge D6_D6then = new CFEdge("D6_D6then", mCFG, D6, D6then);
//		mCFG.addEdge(D6_D6then);
//
//		ICFEdge D6_D7 = new CFEdge("D6_D7", mCFG, D6, D7);
//		mCFG.addEdge(D6_D7);
//
//		D6.setThenEdge(D6_D6then);
//		D6.setElseEdge(D6_D7);
//
//		ICFEdge D6then_stop = new CFEdge("D6then_stop", mCFG, D6then, stop);
//		mCFG.addEdge(D6then_stop);
//
//		ICFEdge D7_D7then = new CFEdge("D7_D7then", mCFG, D7, D7then);
//		mCFG.addEdge(D7_D7then);
//
//		ICFEdge D7_D7else = new CFEdge("D7_D7else", mCFG, D7, D7else);
//		mCFG.addEdge(D7_D7else);
//
//		D7.setThenEdge(D7_D7then);
//		D7.setElseEdge(D7_D7else);
//
//		ICFEdge D7then_stop = new CFEdge("D7then_stop", mCFG, D7then, stop);
//		mCFG.addEdge(D7then_stop);
//
//		ICFEdge D7else_stop = new CFEdge("D7else_stop", mCFG, D7else, stop);
//		mCFG.addEdge(D7else_stop);
//
//		ICFEdge stop_start = new CFEdge("stop_start", mCFG, stop, start);
//		mCFG.addEdge(stop_start);
//
//		Set<ICFEdge> targets = mCFG.getEdgeSet();
//		targets.remove(stop_start);
//		targets.remove(start_inputs);
//		SymTest st = new SymTest(mCFG, targets);
//		TestSequence seq = st.generateTestSequence();
//		Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
//
//	}
//}