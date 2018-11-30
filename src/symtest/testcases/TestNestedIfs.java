package testcases;

import cfg.ICFEdge;
import cfg.ICFG;
import cfg.ICFGBasicBlockNode;
import cfg.ICFGDecisionNode;
import expression.ConcreteConstant;
import expression.Input;
import expression.LesserThanExpression;
import expression.Variable;
import mycfg.CFEdge;
import mycfg.CFG;
import mycfg.CFGBasicBlockNode;
import mycfg.CFGDecisionNode;
import org.junit.Test;
import see.SEE;
import set.SETNode;
import statement.Statement;

public class TestNestedIfs {
    @Test
    public void testNaya() throws Exception {
        // Why mCFG ? Because it is an inteface for Control Flow Graph
        ICFG mCFG = null;

        // Why A,W ?
        // A -> start node
        // W -> end node
        ICFGBasicBlockNode A = new CFGBasicBlockNode("A", null);
        ICFGBasicBlockNode W = new CFGBasicBlockNode("W", null);
        // This constructor initializes mCFG with start & end node
        mCFG = new CFG(A, W);

        ConcreteConstant CONSTANT_TWO = new ConcreteConstant(2, mCFG);
        ConcreteConstant CONSTANT_FIVE = new ConcreteConstant(5, mCFG);
        ConcreteConstant CONSTANT_TWENTY = new ConcreteConstant(20, mCFG);
        ConcreteConstant CONSTANT_THIRTY = new ConcreteConstant(30, mCFG);
        ConcreteConstant CONSTANT_ZERO = new ConcreteConstant(0, mCFG);


        //  variables x & y
        Variable x = new Variable("x", mCFG);
        Variable y = new Variable("y", mCFG);
        Variable z = new Variable("z", mCFG);

        ICFGBasicBlockNode B = new CFGBasicBlockNode("B", mCFG);
        Input i1 = new Input(mCFG);
        Statement stmt1 = new Statement(mCFG, x, i1);
        B.setStatement(stmt1);
        mCFG.addBasicBlockNode(B);

        ICFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
        Input i2 = new Input(mCFG);
        Statement stmt2 = new Statement(mCFG, y, i2);    //or in place of i2 put constant_two
        C.setStatement(stmt2);
        mCFG.addBasicBlockNode(C);

        ICFGBasicBlockNode G = new CFGBasicBlockNode("G", mCFG);
        // Input i3 = new Input(mCFG);
        Statement stmt3 = new Statement(mCFG, z, CONSTANT_THIRTY);    //or in place of i2 put constant_two
        G.setStatement(stmt3);
        mCFG.addBasicBlockNode(G);

        LesserThanExpression expr4 = new LesserThanExpression(mCFG, x, z);
        ICFGDecisionNode H = new CFGDecisionNode(mCFG, expr4);
        mCFG.addDecisionNode(H);

        LesserThanExpression expr3 = new LesserThanExpression(mCFG, x, y);
        ICFGDecisionNode D = new CFGDecisionNode(mCFG, expr3);
        mCFG.addDecisionNode(D);

        ICFGBasicBlockNode F = new CFGBasicBlockNode("F", mCFG);
        Statement stmt4 = new Statement(mCFG, x, CONSTANT_THIRTY);
        F.setStatement(stmt4);
        mCFG.addBasicBlockNode(F);

        ICFGBasicBlockNode I = new CFGBasicBlockNode("I", mCFG);
        Statement stmt6 = new Statement(mCFG, x, CONSTANT_ZERO);
        I.setStatement(stmt6);
        mCFG.addBasicBlockNode(I);

        //edges
        ICFEdge AB = new CFEdge("AB", mCFG, A, B);
        ICFEdge BC = new CFEdge("BC", mCFG, B, C);
        ICFEdge CG = new CFEdge("CG", mCFG, C, G);
        ICFEdge GD = new CFEdge("GD", mCFG, G, D);
        ICFEdge DH = new CFEdge("DH", mCFG, D, H);
        ICFEdge DF = new CFEdge("DF", mCFG, D, F);
        ICFEdge HI = new CFEdge("HI", mCFG, H, I);
        ICFEdge HW = new CFEdge("HW", mCFG, H, W);
        ICFEdge IW = new CFEdge("IW", mCFG, I, W);
        ICFEdge FW = new CFEdge("FW", mCFG, F, W);

        SEE seeNew2 = new SEE(mCFG);

        SETNode setNode6 = seeNew2.allPathSE(mCFG, 6);

        System.out.println();
    }
}