/*

Test Program:
=================

package testcases;

import java.util.Scanner;

public class Addition {

    public static void addition(){                  //  A

        while(true)                                 //  B
        {
            Scanner sc = new Scanner(System.in);    //  B
            int x = sc.nextInt();                   //  B
            int y = sc.nextInt();                   //  B

            int z = x + y;                          //  C

            if(z>10){                               //  D
                if(x>5){                            //  E
                    if(y<3){                        //  F
                        x = 6;                      //  G
                    }
                    if(y>5){                        //  H
                        x = 4;                      //  I
                    }
                }
            }
        }                                           //  W
    }                                               //

    public static void main( String[] args){
        addition();
    }

}
*/

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

public class TestDependency {
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
        Variable i = new Variable("i", mCFG);
        Variable j = new Variable("j", mCFG);

        ICFGBasicBlockNode B = new CFGBasicBlockNode("B", mCFG);
//        Input i1 = new Input(mCFG);
        Statement stmt1 = new Statement(mCFG, x, CONSTANT_THIRTY);
        B.setStatement(stmt1);
        mCFG.addBasicBlockNode(B);

        ICFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
        Input i2 = new Input(mCFG);
        Statement stmt2 = new Statement(mCFG, i, i2);
        C.setStatement(stmt2);
        mCFG.addBasicBlockNode(C);

        LesserThanExpression expr3 = new LesserThanExpression(mCFG, x, i);
        ICFGDecisionNode D = new CFGDecisionNode("D",mCFG,expr3);
        mCFG.addDecisionNode(D);

        ICFGBasicBlockNode E = new CFGBasicBlockNode("E", mCFG);
        Input i3 = new Input(mCFG);
        Statement stmt3 = new Statement(mCFG, j, i3);
        E.setStatement(stmt3);
        mCFG.addBasicBlockNode(E);

        AddExpression addExpr1 = new AddExpression(mCFG,x,i);
        AddExpression addExpr2 = new AddExpression(mCFG,x,j);

//        SubExpression subExpr = new SubExpression(mCFG,x,j);
        LesserThanExpression expr4 = new LesserThanExpression(mCFG, addExpr1, addExpr2);
        ICFGDecisionNode F = new CFGDecisionNode("F",mCFG,expr4);
        mCFG.addDecisionNode(F);

        ICFGBasicBlockNode G = new CFGBasicBlockNode("G", mCFG);
        AddExpression subExpr1 = new AddExpression(mCFG,i,j);
        Statement stmt5 = new Statement(mCFG, x, subExpr1);
        G.setStatement(stmt5);
        mCFG.addBasicBlockNode(G);

        ICFGBasicBlockNode H = new CFGBasicBlockNode("H", mCFG);
        AddExpression addExpr3 = new AddExpression(mCFG,i,j);

//        SubExpression subExpr2 = new SubExpression(mCFG,i,j);
        Statement stmt6 = new Statement(mCFG, x, addExpr3);
        G.setStatement(stmt6);
        mCFG.addBasicBlockNode(H);

//        //edges
        ICFEdge AB = new CFEdge("AB", mCFG, A, B);
        ICFEdge BC = new CFEdge("BC", mCFG, B, C);
        ICFEdge CD = new CFEdge("CD", mCFG, C, D);
        ICFEdge DE = new CFEdge("DE", mCFG, D, E);
        ICFEdge EF = new CFEdge("EF", mCFG, E, F);
        ICFEdge FG = new CFEdge("FG", mCFG, F, G);
        ICFEdge FH = new CFEdge("FH", mCFG, F, H);
        ICFEdge GD = new CFEdge("GD", mCFG, G, D);
        ICFEdge HD = new CFEdge("HD", mCFG, H, D);
        ICFEdge DW = new CFEdge("DW", mCFG, D, W);


        D.setThenEdge(DE);
        D.setElseEdge(DW);

        F.setThenEdge(FG);
        F.setElseEdge(FH);

        System.out.println(mCFG.getEdgeSet());

        SEE seeNew2 = new SEE(mCFG);

        SETNode setNode6 = seeNew2.allPathSE(mCFG,10);

    }
}