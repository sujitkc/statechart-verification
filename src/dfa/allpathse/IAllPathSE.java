package set;

import java.util.Set;

import program.IProgram;
import cfg.*;
import ast.Environment;
import set.ISETNode;
import set.ISETDecisionNode;
import set.ISETInstructionNode;
import java.util.Queue;
import ast.Expression;
import java.util.Set;

public interface IAllPathSE {
	public ISETDecisionNode createNewSETDecisionNode(ICFGDecisionNode node, int level); //creates SET decision node for CFG decision node
	public ISETInstructionNode createNewSETInstructionNode(ICFGBasicBlockNode node, int level); //creates SET instruction node for CFG basic block node
	public void getPathConstraint(ISETNode currentNode); //gets the AND of path constraint from the root of the tree to the current node
	public boolean IS_SAT(Expression exp); //checks the satisfiability of an expression
	public void enqueue(Queue queue, ISETNode node, int level);
	public ISETNode dequeue(Queue queue);
	public ICFGNode getCFGNode(ISETNode node); //gets the corresponding CFG node for SET node
	public void executeAllPathSE(ISETNode rootNode,int depth); //execution begins here
}
