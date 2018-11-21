package see;

import cfg.*;
import expression.IExpression;
import expression.IIdentifier;
import expression.Type;
import mycfg.CFGDecisionNode;
import set.*;
import statement.IStatement;

import java.util.List;

public class SEENew {

    private SET mSET;

    /**
     * Constructor to check whether cfg is null or not
     */

    public SEENew(ICFG cfg) throws Exception {
        if (cfg != null) {
            this.mSET = new SET(cfg);
        } else {
            throw new Exception("Null CFG");
        }
    }


    public SET getSET() {
        return mSET;
    }

//    /**
//     * The function expands the SET along the specified set of CFG edges
//     *
//     * @param cfgEdges
//     * @throws Exception
//     */
//    public void expandSET(List<ICFEdge> cfgEdges) throws Exception {
//        for (ICFEdge edge : cfgEdges) {
//            /* Shyam added below line */
//            System.out.println(edge);
//            /* Shyam added above line */
//            makeSETproperly(edge);
//        }
//    }

//    private void makeSETproperly(ICFEdge edge) {
//
//    }

    public IExpression symEval(ICFGNode icfgNode, IExpression iExpression){

        if(icfgNode instanceof ICFGBasicBlockNode) {
            ICFGBasicBlockNode icfgBasicBlockNode = (ICFGBasicBlockNode) icfgNode;
            IStatement statement = icfgBasicBlockNode.getStatement();
            IExpression value = statement.getRHS();
            return value;
        }
        else if(icfgNode instanceof ICFGDecisionNode) {
            ICFGDecisionNode icfgDecisionNode = (ICFGDecisionNode) icfgNode;
            IExpression value = icfgDecisionNode.getCondition();
            return value;
        }
        else{
            return null;
        }
    }



    //  ICFGNode or CFGNode or ICFG or CFG? SETNode or SET
//    /**
//     * The basic symbolic execution step of individual instruction
//     * @param icfgNode s :    single instruction / decision node
//     * @param setNode n' :     symbolic execution tree node
//     * @return returnSETNode s' : new SET node
//     */
    public SETNode singleStep(ICFGNode icfgNode, SETNode setNode) throws Exception {

        //  initializing setNode aisehi
        SETNode returnSETNode = null;

        //  if s is an instruction
        if(icfgNode instanceof ICFGBasicBlockNode){

            // Assumption that, each icfgnode has only 1 instruction
            IStatement statement = ((ICFGBasicBlockNode) icfgNode).getStatement();
            // if s is an instruction, then s' is also an instruction
            SETBasicBlockNode setBasicBlockNode = new SETBasicBlockNode(mSET, (ICFGBasicBlockNode) icfgNode);

            IIdentifier LHS = statement.getLHS();
            IExpression RHS = statement.getRHS();

            IExpression valueRHS = null;

            // initializes with node & its type
            SETExpressionVisitor visitor = new SETExpressionVisitor(setNode,
                    LHS.getType());

            visitor.visit(RHS); //  what does visit do?
            valueRHS = visitor.getValue(); // e' is value.

            // Symbolic values mapping
            IIdentifier var = LHS;
            setBasicBlockNode.setValue(var, valueRHS);

            returnSETNode = setBasicBlockNode;
        }

        if(icfgNode instanceof ICFGDecisionNode){

            IExpression condition = ((ICFGDecisionNode) icfgNode).getCondition();

            // if s is a decision node, then s' is also a decision node
            SETDecisionNode setDecisionNode = new SETDecisionNode(condition, mSET, (ICFGDecisionNode) icfgNode);

            SETExpressionVisitor visitor = new SETExpressionVisitor(setNode,
                    Type.BOOLEAN);

            if (condition == null) {
                throw new Exception("Null Expression");
            } else {
                System.out.println(
                        "Condition: "+condition
                );
                visitor.visit(condition);
                //only expr in IExpression form
                IExpression value = visitor.getValue();
                setDecisionNode.setCondition(value);
            }

            returnSETNode = setDecisionNode;
        }



        return returnSETNode;
    }

//    public void addNewSETDecisionNode(ICFGNode newNode, SETEdge newSETEdge)
//            throws Exception {
//        CFGDecisionNode decisionNode = (CFGDecisionNode) newNode;
//        SETDecisionNode newSETNode = new SETDecisionNode(
//                decisionNode.getCondition(), mSET, decisionNode);
//        this.mSET.addDecisionNode(newSETNode);
//        newSETEdge.setHead(newSETNode);
//        newSETEdge.setHead(newSETNode);
//
//        newSETNode.setIncomingEdge(newSETEdge);
//        this.mSET.addEdge(newSETEdge);
//        this.computeExpression(newSETNode);
//    }
//
//    private void addNewSETBasicBlockNode(ICFGNode newNode, SETEdge newSETEdge) throws Exception {
//        SETBasicBlockNode newSETNode = new SETBasicBlockNode(mSET,
//                (CFGBasicBlockNode) newNode);
//        this.mSET.addBasicBlockNode(newSETNode);
//        newSETEdge.setHead(newSETNode);
//        newSETNode.setIncomingEdge(newSETEdge);
//        this.mSET.addEdge(newSETEdge);
//        this.computeStatementList(newSETNode);
//    }

    private void computeStatementList(SETBasicBlockNode node) throws Exception {
        ICFGBasicBlockNode cfgBasicBlockNode = (ICFGBasicBlockNode) node
                .getCFGNode();
        IStatement statement = cfgBasicBlockNode.getStatement();

        IIdentifier LHS = statement.getLHS();
        IExpression RHS = statement.getRHS();

        SETExpressionVisitor visitor = new SETExpressionVisitor(node,
                statement.getLHS().getType());

        IExpression value = null;

        visitor.visit(statement.getRHS());
        value = visitor.getValue();

        IIdentifier var = statement.getLHS();
        node.setValue(var, value);
    }

    private void computeExpression(SETDecisionNode node) throws Exception {
        SETExpressionVisitor visitor = new SETExpressionVisitor(node,
                Type.BOOLEAN);
        CFGDecisionNode cfgNode = (CFGDecisionNode) node.getCFGNode();
        if (node.getCondition() == null) {
            throw new Exception("Null Expression");
        } else {
            visitor.visit(cfgNode.getCondition());
            IExpression value = visitor.getValue();
            node.setCondition(value);
        }
    }
}
