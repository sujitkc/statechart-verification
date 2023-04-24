package set;

import java.util.Set;

import program.IProgram;
import ast.Environment;
import set.ISETNode;
import set.ISETDecisionNode;
import set.ISETInstructionNode;
import java.util.Queue;
import ast.Expression;
import java.util.Set;

public interface ISET {
  public void getPathConstraint(ISETNode currentNode); //gets the AND of path constraint from the root of the tree to the current node
  public ISETEdge setPathEdgeAnnotation(ISETDecisionNode parent,ISETNode child); //gets the edge from decision node and child node
  public void getPathEdgeAnnotation(ISETDecisionNode parent,ISETNode child, ISETEdge newEdge); //creates a edge from decision node to instruction node with boolean annotation of true or false 
  public ISETNode getSETRootNode(); //gets the root node of symbolic execution tree
  public ISETNode getParent(ISETNode node); // gets the parent of the current SET node
  public Set<ISETNode> getChildren(ISETNode node); //gets all the children of SET node
  public void setChildren(ISETNode node, Set<ISETNode> childNodes); //sets the children of a given SETnode
}
