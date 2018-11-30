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

public class SEE {
    private SET mSET;

    //  initialize newSEE using cfg
    public SEE(ICFG cfg) throws Exception {
        if (cfg != null) {
            this.mSET = new SET(cfg);
        } else {
            throw new Exception("Null CFG");
        }
    }

    public SET getSET() {
        return mSET;
    }

    public SETNode allPathSE(ICFG icfg, int depth) throws Exception {
//        startnodeCFG
        ICFGBasicBlockNode startNode = icfg.getStartNode();
        SETBasicBlockNode startSETNode = mSET.getStartNode();
//        passing empty environment & startNode
        LinkedList<Pair<SETNode, Integer>> setNodeQueue = new LinkedList<>();
        setNodeQueue.add(new Pair<>(startSETNode, 1));
        while (!setNodeQueue.isEmpty()) {
            Pair<SETNode, Integer> pair = setNodeQueue.removeFirst();
            System.out.println("NODES:"+pair.getFirst().getCFGNode().getId()+"->");
            Set<SETNode> nodeSet = mSET.getNodeSet();

            SETNode pairSETNode = pair.getFirst();
            Integer pairDepth = pair.getSecond();

            if (pairDepth > depth) {
                continue;
            }
            ICFGNode correspondingICFGNode = pairSETNode.getCFGNode();

            if (correspondingICFGNode instanceof ICFGBasicBlockNode) {
                ICFGNode successorNode = ((ICFGBasicBlockNode) correspondingICFGNode).getSuccessorNode();

                if(successorNode!=null) {
                    SETNode setNode = singleStep(successorNode, pairSETNode);
                    setNodeQueue.add(new Pair<>(setNode, pairDepth + 1));
                }
            }

            if (correspondingICFGNode instanceof ICFGDecisionNode) {
                IExpression condition = ((SETDecisionNode)pairSETNode).getCondition();
                System.out.println("condition:"+condition);
                IExpression pathPredicate = ((SETDecisionNode)pairSETNode).getPathPredicate();
                AndExpression andExpression1 = new AndExpression(this.getSET(), pathPredicate, condition);
                Set<IIdentifier> symVars = mSET.getVariables();

                ISolver solver = new Z3Solver(symVars, andExpression1);
                SolverResult solution = solver.solve();
                //  if satisfiable
                if (solution.getResult() == true) {
                    ICFGNode thenNode = ((ICFGDecisionNode) correspondingICFGNode).getThenSuccessorNode();
                    if(thenNode!=null) {
                        SETNode setNode = singleStep(thenNode, pairSETNode);
                        setNodeQueue.add(new Pair<>(setNode, pairDepth + 1));
                    }
                }

                //  here we have to add "Not of the expression"
                NotExpression notExpression = new NotExpression(this.getSET(), condition);
                AndExpression andExpression2 = new AndExpression(this.getSET(), pairSETNode.getPathPredicate(), notExpression);
                Set<IIdentifier> symVars2 = mSET.getVariables();
                ISolver solver2 = new Z3Solver(symVars2, andExpression2);
                SolverResult solution2 = solver2.solve();
                //  if unsatisfiable
                if (solution2.getResult() == true) {
                    ICFGNode elseNode = ((ICFGDecisionNode) correspondingICFGNode).getElseSuccessorNode();
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
        this.mSET.addBasicBlockNode(newSETNode);
        newSETEdge.setHead(newSETNode);
        newSETNode.setIncomingEdge(newSETEdge);
        this.mSET.addEdge(newSETEdge);
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
            //  add symbolic variable to SET
//              if input, then symbolic variable
            SETExpressionVisitor visitor = new SETExpressionVisitor(node,
                    LHS.getType());

            visitor.visit(RHS);

            IExpression value = null;
            //  get the symbolic expression by visiting the RHS (top of stack)
            value = visitor.getValue();

            IIdentifier var = LHS;
            //  set symbolic expression into the node
            node.setValue(var, value);
            Map<IIdentifier, IExpression> map = node.getValues();

        } catch (NullPointerException npe) {
        }


    }

    public void computeExpression(SETDecisionNode node) throws Exception {
        SETExpressionVisitor visitor = new SETExpressionVisitor(node,
                Type.BOOLEAN);
        CFGDecisionNode cfgNode = (CFGDecisionNode) node.getCFGNode();
        IExpression conditionCFG = cfgNode.getCondition();
        IExpression conditionSET = node.getCondition();
        if (conditionSET == null) {
            throw new Exception("Null Expression");
        } else {
            visitor.visit(conditionCFG);
            IExpression value = visitor.getValue();
            node.setCondition(value);
        }
    }

}
