package see;

import Solver.ISolver;
import Solver.SolverResult;
import Solver.Z3Solver;
import cfg.ICFG;
import cfg.ICFGBasicBlockNode;
import cfg.ICFGDecisionNode;
import cfg.ICFGNode;
import expression.*;
import mycfg.CFGBasicBlockNode;
import mycfg.CFGDecisionNode;
import program.IProgram;
import set.*;
import statement.IStatement;
import utilities.Pair;

import java.util.*;

public class SEENew2 {
    private SET mSET;

    //  initialize newSEE using cfg
    public SEENew2(ICFG cfg) throws Exception {
        if (cfg != null) {
//            System.out.println( "cfg is not null : " + mSET.getNodeSet() );
            this.mSET = new SET(cfg);
            Set<SETNode> nodeSet = mSET.getNodeSet();
            for (SETNode setNode : nodeSet) {
//                System.out.println("Node:" + setNode);
//                System.out.println("CFGNode:"+setNode.getCFGNode());
//            System.out.println("Head:"+setEdge.getHead());
//            System.out.println("Tail:"+setEdge.getTail());
            }
//            System.out.println(mSET.getNodeSet());
        } else {
            throw new Exception("Null CFG");
        }
    }

    public SET getSET() {
        return mSET;
    }

    public SETNode allPathSE(ICFG icfg, int depth) throws Exception {
        //  startnodeCFG
        ICFGBasicBlockNode startNode = icfg.getStartNode();
//        System.out.println(mSET.getNodeSet());
//        System.out.println(startNode);
        SETBasicBlockNode startSETNode = mSET.getStartNode();
//        System.out.println(mSET.getNodeSet());
        //  passing empty environment & startNode
//        SETNode startSETNode = this.singleStep(startNode,null);
//        System.out.println((SETBasicBlockNode)startSETNode);
//        System.out.println();
//        System.out.println(startSETNode);

//        System.out.println(mSET.getStartNode().getIncomingEdge());

        LinkedList<Pair<SETNode, Integer>> setNodeQueue = new LinkedList<>();
        setNodeQueue.add(new Pair<>(startSETNode, 1));
//        System.out.println(setNodeQueue.size());
        while (!setNodeQueue.isEmpty()) {
//            System.out.println(setNodeQueue.size());

            Pair<SETNode, Integer> pair = setNodeQueue.removeFirst();
            System.out.println("NODES:"+pair.getFirst().getCFGNode().getId()+"->");
//            System.out.println(setNodeQueue.size());

            Set<SETNode> nodeSet = mSET.getNodeSet();
//            System.out.println(nodeSet);
            for (SETNode setNode:nodeSet){
                SETEdge incomingEdge = null;
                if(setNode.getIncomingEdge() != null) {
                    incomingEdge = setNode.getIncomingEdge();
                }
//                System.out.println("NodeIncomingEdge:"+incomingEdge);
//                System.out.println("Node:"+setNode);
    //            System.out.println("Head:"+setEdge.getHead());
    //            System.out.println("Tail:"+setEdge.getTail());
            }

            SETNode pairSETNode = pair.getFirst();
//            System.out.println(pairSETNode.getCFGNode());
//            System.out.println(pairSETNode);
            Integer pairDepth = pair.getSecond();
//            System.out.println(pairSETNode.getId());
//            System.out.println(pairDepth);

            if (pairDepth > depth) {
                continue;
            }
            ICFGNode correspondingICFGNode = pairSETNode.getCFGNode();
//            System.out.println("Corresponding ICFGNode:"+correspondingICFGNode);

            if (correspondingICFGNode instanceof ICFGBasicBlockNode) {
                ICFGNode successorNode = ((ICFGBasicBlockNode) correspondingICFGNode).getSuccessorNode();
//                System.out.println("successor:"+successorNode);

                if(successorNode!=null) {
                    SETNode setNode = singleStep(successorNode, pairSETNode);
//                    System.out.println("BasicBlockCFG");
                    setNodeQueue.add(new Pair<>(setNode, pairDepth + 1));
//                    System.out.println("BasicBlockCFGAdded");
//                    System.out.println(setNodeQueue.size());
                }
            }

            if (correspondingICFGNode instanceof ICFGDecisionNode) {
                IExpression condition = ((SETDecisionNode)pairSETNode).getCondition();
//                System.out.println("condition:"+condition);
//                System.out.println("DecisionCFG");
//                System.out.println(pairSETNode.getCFGNode());
                IExpression pathPredicate = ((SETDecisionNode)pairSETNode).getPathPredicate();
//                System.out.println("Path Predicate:"+pathPredicate);
                AndExpression andExpression1 = new AndExpression(this.getSET(), pathPredicate, condition);
//                System.out.println("DecisionCFG");
                Set<IIdentifier> symVars = mSET.getVariables();
//                System.out.println(symVars);

                ISolver solver = new Z3Solver(symVars, andExpression1);
                SolverResult solution = solver.solve();
//                System.out.println(solution.getModel());
                //  if satisfiable
                if (solution.getResult() == true) {
                    ICFGNode thenNode = ((ICFGDecisionNode) correspondingICFGNode).getThenSuccessorNode();
//                    System.out.println("thenNode:"+thenNode);

                    if(thenNode!=null) {
                        SETNode setNode = singleStep(thenNode, pairSETNode);
                        setNodeQueue.add(new Pair<>(setNode, pairDepth + 1));
                    }
                }

                //  here we have to add "Not of the expression"
                NotExpression notExpression = new NotExpression(this.getSET(), condition);
                AndExpression andExpression2 = new AndExpression(this.getSET(), pairSETNode.getPathPredicate(), notExpression);
                Set<IIdentifier> symVars2 = mSET.getVariables();
//                System.out.println(symVars2);
                ISolver solver2 = new Z3Solver(symVars2, andExpression2);
                SolverResult solution2 = solver2.solve();
//                System.out.println(solution.getModel());
                //  if unsatisfiable
                if (solution2.getResult() == true) {
                    ICFGNode elseNode = ((ICFGDecisionNode) correspondingICFGNode).getElseSuccessorNode();
//                    System.out.println("elseNode:"+elseNode);

                    if(elseNode!=null) {
                        SETNode setNode = singleStep(elseNode, pairSETNode);
                        setNodeQueue.add(new Pair<>(setNode, pairDepth + 1));
                    }
                }
            }
        }
        return startSETNode;
    }


    public SETNode singleStep(ICFGNode icfgNode, SETNode prevSetNode) throws Exception {
        //  for returning SETNode
        SETNode returnSETNode = null;

        //  naya edge banao
        //  head is null, for just now, it will be initialised below soon.
        SETEdge newSETEdge = new SETEdge(mSET, prevSetNode, null);

        ICFGNode predCFGNode = prevSetNode.getCFGNode();


        /*
        Case 1: prev node -> Basic & curr node -> Basic
        Case 2: prev node -> Decision & curr node -> Basic
        Case 3: prev node -> Basic & curr node -> Decision
        Case 4: prev node -> Decision & curr node -> Decision
         */

        if(icfgNode instanceof ICFGBasicBlockNode){
            if(predCFGNode instanceof ICFGBasicBlockNode){
                returnSETNode = addNewSETBasicBlockNode(icfgNode, newSETEdge);
            }

            if(predCFGNode instanceof ICFGDecisionNode){
                returnSETNode = addNewSETBasicBlockNode(icfgNode,newSETEdge);

                ICFGDecisionNode predDecNode = (ICFGDecisionNode)predCFGNode;
                if(predDecNode.getThenEdge().getHead() == icfgNode){
                    ((SETDecisionNode)prevSetNode).setThenEdge(newSETEdge);
                }

                else if(predDecNode.getElseEdge().getHead() == icfgNode){
                    ((SETDecisionNode)prevSetNode).setElseEdge(newSETEdge);
                }
            }
        }

        else if(icfgNode instanceof ICFGDecisionNode){
            if(predCFGNode instanceof ICFGBasicBlockNode){
                returnSETNode = addNewSETDecisionNode(icfgNode, newSETEdge);
            }

            if(predCFGNode instanceof ICFGDecisionNode){
                returnSETNode = addNewSETDecisionNode(icfgNode,newSETEdge);

                ICFGDecisionNode predDecNode = (ICFGDecisionNode)predCFGNode;
                if(predDecNode.getThenEdge().getHead() == icfgNode){
                    ((SETDecisionNode)prevSetNode).setThenEdge(newSETEdge);
                }

                else if(predDecNode.getElseEdge().getHead() == icfgNode){
                    ((SETDecisionNode)prevSetNode).setElseEdge(newSETEdge);
                }
            }
        }
        //  else case for escaping null pointer exception
        return returnSETNode;
    }

    //  function to add SETBasicBlockNode
    public SETBasicBlockNode addNewSETBasicBlockNode(ICFGNode newNode, SETEdge newSETEdge) throws Exception {
        SETBasicBlockNode newSETNode = new SETBasicBlockNode(mSET,
                (CFGBasicBlockNode) newNode);
//        System.out.println("new node:" + newSETNode);
        this.mSET.addBasicBlockNode(newSETNode);
//        System.out.println(mSET.getNodeSet());
//        System.out.println("newNodeSet:"+mSET.getEdgeSet());
        newSETEdge.setHead(newSETNode);
        newSETNode.setIncomingEdge(newSETEdge);
        this.mSET.addEdge(newSETEdge);
//        System.out.println(mSET.getEdgeSet());
        this.computeStatementList(newSETNode);
        return newSETNode;
    }

    //  function to add SETDecisionNode

    public SETDecisionNode addNewSETDecisionNode(ICFGNode newNode, SETEdge newSETEdge) throws Exception {
        CFGDecisionNode decisionNode = (CFGDecisionNode) newNode;
        SETDecisionNode newSETNode = new SETDecisionNode(
                decisionNode.getCondition(), mSET, decisionNode);
        this.mSET.addDecisionNode(newSETNode);
        newSETEdge.setHead(newSETNode);
        newSETNode.setIncomingEdge(newSETEdge);
        this.mSET.addEdge(newSETEdge);
        this.computeExpression(newSETNode);
        return newSETNode;
    }

    public void computeStatementList(SETBasicBlockNode node) throws Exception {
        ICFGBasicBlockNode cfgBasicBlockNode = (ICFGBasicBlockNode) node
                .getCFGNode();
//        System.out.println(cfgBasicBlockNode);
        IStatement statement = cfgBasicBlockNode.getStatement();

        /**TODO
         * add condition for stopNode, won't have LHS & RHS
         */
        try {
            IIdentifier LHS = statement.getLHS();   //  x
            IExpression RHS = statement.getRHS();   //  input
//            System.out.println(LHS);
//            System.out.println(RHS);

//            System.out.println("var name : " +  LHS.getName());

            //  add symbolic variable to SET
//              if input, then symbolic variable
//            System.out.println(mSET.getVariables());
//            mSET.addVariable(LHS);
//            System.out.println(mSET.getVariables());
//            Variable x = new Variable(LHS.getName(),mSET));
            SETExpressionVisitor visitor = new SETExpressionVisitor(node,
                    LHS.getType());

            visitor.visit(RHS);

//            Stack<IExpression> newStack = visitor.getStack();
//            System.out.println("stack:"+visitor.getStack());

            IExpression value = null;
            //  get the symbolic expression by visiting the RHS (top of stack)
            value = visitor.getValue();
//            System.out.println(LHS + " "+ value);

            IIdentifier var = LHS;
            //  set symbolic expression into the node
            node.setValue(var, value);
            Map<IIdentifier, IExpression> map = node.getValues();
//        for (Map.Entry<IIdentifier,IExpression> entry : map.entrySet())
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());
//        System.out.println(map);

        } catch (NullPointerException npe) {
//            System.out.println(node.getCFGNode());
//            System.out.println("End node reached");
        }


    }

    public void computeExpression(SETDecisionNode node) throws Exception {
        SETExpressionVisitor visitor = new SETExpressionVisitor(node,
                Type.BOOLEAN);
        CFGDecisionNode cfgNode = (CFGDecisionNode) node.getCFGNode();
        IExpression conditionCFG = cfgNode.getCondition();
//        System.out.println("CFGCondition:"+conditionCFG);
        IExpression conditionSET = node.getCondition();
//        System.out.println("SETCondition:"+conditionSET);
        if (conditionSET == null) {
            throw new Exception("Null Expression");
        } else {
            visitor.visit(conditionCFG);
            IExpression value = visitor.getValue();
//            System.out.println("CONDITION:"+value);
            node.setCondition(value);
        }
    }

}
