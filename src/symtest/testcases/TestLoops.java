package testcases;

import cfg.ICFEdge;
import cfg.ICFG;
import cfg.ICFGBasicBlockNode;
import cfg.ICFGDecisionNode;
import expression.*;
import mycfg.CFEdge;
import mycfg.CFG;
import mycfg.CFGBasicBlockNode;
import mycfg.CFGDecisionNode;
import org.junit.Test;
import see.SEE;
import set.SETNode;
import statement.Statement;

public class TestLoops {
    @Test
    public void testNaya() throws Exception {
        // Why mCFG ? Because it is an inteface for Control Flow Graph
        ICFG mCFG = null;

        // Why A,W ?
        // A -> start node
        // W -> end node
        ICFGBasicBlockNode A = new CFGBasicBlockNode("A",null);
        ICFGBasicBlockNode W = new CFGBasicBlockNode("W",null);
        // This constructor initializes mCFG with start & end node
        mCFG = new CFG(A, W);

        ConcreteConstant CONSTANT_TWO = new ConcreteConstant(2,mCFG);
        ConcreteConstant CONSTANT_FIVE = new ConcreteConstant(5,mCFG);
        ConcreteConstant CONSTANT_TWENTY = new ConcreteConstant(20,mCFG);
        ConcreteConstant CONSTANT_THIRTY = new ConcreteConstant(30,mCFG);


        //  variables x & y
        Variable x = new Variable("x", mCFG);
        Variable y = new Variable("y", mCFG);
        Variable p = new Variable("p", mCFG);
        Variable q = new Variable("q", mCFG);

        True trueExpr = new True(mCFG);
        ICFGDecisionNode B = new CFGDecisionNode(mCFG,trueExpr);
        mCFG.addDecisionNode(B);

        ICFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
        Input i1 = new Input(mCFG);
        Statement stmt1 = new Statement(mCFG, x, i1);
        C.setStatement(stmt1);
        mCFG.addBasicBlockNode(C);

        ICFGBasicBlockNode D = new CFGBasicBlockNode("D", mCFG);
        Input i2 = new Input(mCFG);
        Statement stmt2 = new Statement(mCFG, y, i2);
        D.setStatement(stmt2);
        mCFG.addBasicBlockNode(D);

        LesserThanExpression expr3 = new LesserThanExpression(mCFG, x, y);
        ICFGDecisionNode E = new CFGDecisionNode(mCFG,expr3);
        mCFG.addDecisionNode(E);

        ICFGBasicBlockNode F = new CFGBasicBlockNode("F", mCFG);
        AddExpression addExpr1 = new AddExpression(mCFG,x,y);
        Statement stmt3 = new Statement(mCFG, p, addExpr1);
        F.setStatement(stmt3);
        mCFG.addBasicBlockNode(F);

        ICFGBasicBlockNode G = new CFGBasicBlockNode("G", mCFG);
        SubExpression subExpr1 = new SubExpression(mCFG,x,y);
        Statement stmt4 = new Statement(mCFG, p, subExpr1);
        G.setStatement(stmt4);
        mCFG.addBasicBlockNode(G);
//
//
//        //edges
        ICFEdge AB = new CFEdge("AB", mCFG, A, B);
        ICFEdge BC = new CFEdge("BC", mCFG, B, C);
        ICFEdge CD = new CFEdge("CD", mCFG, C, D);
        ICFEdge DE = new CFEdge("DE", mCFG, D, E);
        ICFEdge EF = new CFEdge("EF", mCFG, E, F);
        ICFEdge EG = new CFEdge("EG", mCFG, E, G);
        ICFEdge FB = new CFEdge("FB", mCFG, F, B);
        ICFEdge GB = new CFEdge("GB", mCFG, G, B);
        ICFEdge BW = new CFEdge("BW", mCFG, B, W);

        B.setThenEdge(BC);
        B.setElseEdge(BW);

        E.setThenEdge(EF);
        E.setElseEdge(EG);

        SEE seeNew2 = new SEE(mCFG);

        SETNode setNode6 = seeNew2.allPathSE(mCFG,10);




    }
}