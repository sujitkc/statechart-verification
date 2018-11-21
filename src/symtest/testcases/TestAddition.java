//package testcases;
//
//import Solver.ISolver;
//import Solver.SolverResult;
//import Solver.Z3Solver;
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
//import set.SET;
//import set.SETNode;
//import statement.Statement;
//import tester.SymTestUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//public class TestAddition {
//    @Test
//    public void testAddition() throws Exception{
//
//        System.out.println("Test case started\n\n");
//
//        /**
//         * First create an object of ICFG because it is an interface for Control Flow Graph
//         * Then, initialize mCFG with start & end node
//         * Then, create program constants used
//         */
//
//        // Why mCFG ? Because it is an inteface for Control Flow Graph
//        ICFG mCFG = null;
//
//        // Why A,W ?
//        // A -> start node
//        // W -> end node
//        ICFGBasicBlockNode A = new CFGBasicBlockNode("A",null);
//        ICFGBasicBlockNode W = new CFGBasicBlockNode("W",null);
//        // This constructor initializes mCFG with start & end node
//        mCFG = new CFG(A, W);
//
//        // create constants 10,5,3,4,6
//        ConcreteConstant CONSTANT_TEN = new ConcreteConstant(10,mCFG);
//        ConcreteConstant CONSTANT_FIVE = new ConcreteConstant(5,mCFG);
//        ConcreteConstant CONSTANT_THREE = new ConcreteConstant(3,mCFG);
//        ConcreteConstant CONSTANT_FOUR = new ConcreteConstant(4,mCFG);
//        ConcreteConstant CONSTANT_SIX = new ConcreteConstant(6,mCFG);
//
//        //  variables x & y
//        Variable x = new Variable("x", mCFG);
//        Variable y = new Variable("y", mCFG);
//        Variable z = new Variable("z", mCFG);
//
//        ICFGBasicBlockNode B = new CFGBasicBlockNode("B", mCFG);
//        Input i1 = new Input(mCFG);
//        Statement stmt1 = new Statement(mCFG, x, i1);
//        B.addStatement(stmt1);
//        Input i2 = new Input(mCFG);
//        Statement stmt2 = new Statement(mCFG, y, i2);
//        B.addStatement(stmt2);
//        mCFG.addBasicBlockNode(B);
//
//        //  int z = x + y
//        ICFGBasicBlockNode C = new CFGBasicBlockNode("C", mCFG);
//        AddExpression expr1 = new AddExpression(mCFG,x,y);
//        Statement stmt3 = new Statement(mCFG, z, expr1);
//        C.addStatement(stmt3);
//        mCFG.addBasicBlockNode(C);
//
//        //  x = 6
//        ICFGBasicBlockNode G = new CFGBasicBlockNode("G", mCFG);
//        Statement stmt4 = new Statement(mCFG, x, CONSTANT_SIX);
//        G.addStatement(stmt4);
//        mCFG.addBasicBlockNode(G);
//
//        //  x = 4
//        ICFGBasicBlockNode I = new CFGBasicBlockNode("I", mCFG);
//        Statement stmt5 = new Statement(mCFG, x, CONSTANT_FOUR);
//        I.addStatement(stmt5);
//        mCFG.addBasicBlockNode(I);
//
//        //decision nodes
//        //  z>10
//        GreaterThanExpression expr2 = new GreaterThanExpression(mCFG, z, CONSTANT_TEN);
//        ICFGDecisionNode D = new CFGDecisionNode("D", mCFG, expr2);
//        mCFG.addDecisionNode(D);
//
//        //  x>5
//        GreaterThanExpression expr3 = new GreaterThanExpression(mCFG, x, CONSTANT_FIVE);
//        ICFGDecisionNode E = new CFGDecisionNode("E", mCFG, expr3);
//        mCFG.addDecisionNode(E);
//
//        //  y<3
//        LesserThanExpression expr4 = new LesserThanExpression(mCFG, y, CONSTANT_THREE);
//        ICFGDecisionNode F = new CFGDecisionNode("F", mCFG, expr4);
//        mCFG.addDecisionNode(F);
//
//        //  y>5
//        GreaterThanExpression expr5 = new GreaterThanExpression(mCFG, y, CONSTANT_FIVE);
//        ICFGDecisionNode H = new CFGDecisionNode("H", mCFG, expr5);
//        mCFG.addDecisionNode(H);
//
//        //edges
//        ICFEdge AB = new CFEdge("AB", mCFG, A, B);
//        ICFEdge BC = new CFEdge("BC", mCFG, B, C);
//        ICFEdge CD = new CFEdge("CD", mCFG, C, D);
//        ICFEdge DE = new CFEdge("DE", mCFG, D, E);
////        ICFEdge EF = new CFEdge("EF", mCFG, E, F);
////        ICFEdge EH = new CFEdge("EH", mCFG, E, H);
////        ICFEdge FG = new CFEdge("FG", mCFG, F, G);
////        ICFEdge FH = new CFEdge("FH", mCFG, F, H);
////        ICFEdge HI = new CFEdge("HI", mCFG, H, I);
////        //  Nodes which are not connected to anyone will get connected to end node automatically.
////        ICFEdge IW = new CFEdge("IW", mCFG, I, W);
////        ICFEdge HW = new CFEdge("HW", mCFG, H, W);
//        ICFEdge DW = new CFEdge("DW", mCFG, D, W);
//        ICFEdge EW = new CFEdge("EW", mCFG, E, W);
//
//
////        mCFG.addEdge(AB);
////        mCFG.addEdge(BC);
////        mCFG.addEdge(CD);
////        //mCFG.addEdge(DE);
////        mCFG.addEdge(EF);
////        //mCFG.addEdge(EH);
////        mCFG.addEdge(FG);
////        mCFG.addEdge(FH);
////        //mCFG.addEdge(HI);
////        mCFG.addEdge(IW);
////        mCFG.addEdge(HW);
////        mCFG.addEdge(DW);
//
//        //Looping Edge
////        ICFEdge WB = new CFEdge("WB", mCFG, W, B);
////        mCFG.addEdge(WB);
//        // decision edges
//
//        //Target Edges
////        Set<ICFEdge> targets = new HashSet<ICFEdge>();
////        targets.add(DE);
////        targets.add(FH);
////		targets.add(DE);
////		targets.add(EG);
////        targets.add(GH);
////		targets.add(HL);
////		targets.add(DF);
////		targets.add(IJ);
////        targets.add(IK);
//
////        SymTest st = new SymTest(mCFG, targets);
////        TestSequence seq = st.generateTestSequence();
////        Map<IIdentifier, List<Object>> testseq = seq.getTestSequence();
////
////
////        ICFGBasicBlockNode start = mCFG.getStartNode();
////        ICFGBasicBlockNode end = mCFG.getStopNode();
////        Set<ICFEdge> EdgeSet = mCFG.getEdgeSet();
////        Set<ICFGNode> NodeSet = mCFG.getNodeSet();
//
////        for(ICFEdge edge:EdgeSet){
////            System.out.println(edge.getId());
////        }
//
////        System.out.println(start);
////        System.out.println(end);
////        System.out.println(EdgeSet);
////        System.out.println(NodeSet);
//
//        SymTestUtil symTestUtil = new SymTestUtil();
//        List<ICFEdge> path = new ArrayList<>();
//        path.add(AB);
//        path.add(BC);
//        path.add(CD);
//        path.add(DE);
//        path.add(EW);
//
////        path.add(EF);
////        path.add(FH);
////        path.add(HI);
////        path.add(IW);
////
//        SET set = symTestUtil.getSET(path,mCFG);
//
//        set.updateLeafNodeSet();
//        Set<SETNode> leaves = set.getLeafNodes();
//        if (leaves.size() != 1) {
//            Exception e = new Exception(
//                    "SymTest.generateTestSequence : SET should have 1 and only 1 leaf. It has "
//                            + leaves.size());
//            throw e;
//        }
//
//        // The check for leaves(Set) having a single node is already done.
//        SETNode leaf = leaves.iterator().next();
//
//        IExpression exp = leaf.getPathPredicate();
//
//        System.out.println("path predicate = " + exp.toString());
//
//        Set<IIdentifier> symVars = set.getVariables();
//        // Using SMT Solver
//        ISolver solver = new Z3Solver(symVars, exp);
//
//        SolverResult solution = solver.solve();
//
//
//
////        SETBasicBlockNode startNode = set.getStartNode();
////        List<SETEdge> outgoingEdgeList = startNode.getOutgoingEdgeList();
////
////        Set<SETNode> nodeSet = set.getNodeSet();
////        for(SETNode node:nodeSet){
////            System.out.println(node.getCFGNode());
////        }
////        System.out.println();
//
////        System.out.println(startNode.getOutgoingEdgeList());
//
//
////
////        Set<IIdentifier> identifiers = set.getVariables();
////
////        TestSequence ts = new TestSequence(identifiers);
////
////        for(IIdentifier i : identifiers){
////            ts.toString();
////        }
////
////        System.out.println(set.getVariables());
//
////        System.out.println(set);
//    }
//}
