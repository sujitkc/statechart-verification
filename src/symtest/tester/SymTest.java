package tester;

import Solver.SolverResult;
import cfg.ICFEdge;
import cfg.ICFG;
import cfg.ICFGDecisionNode;
import cfg.ICFGNode;
import exceptions.UnSatisfiableException;
import expression.IExpression;
import expression.IIdentifier;
import expression.Variable;
import graph.IEdge;
import graph.IGraph;
import graph.INode;
import graph.IPath;
import heuristics.ApplyHeuristics;
import mygraph.Path;
import set.SET;
import set.SETBasicBlockNode;
import set.SETNode;
import utilities.Pair;

import java.util.*;

public class SymTest {

	private static final int MAXIMUM_ITERATIONS = 5;
	public ICFG mCFG;
	public CFGToGraphConvertor mConvertor;
	public IGraph mGraph;
	public Set<ICFEdge> mTargets;
	public ICFGNode mTarget;
	public SET set;
	public Set<ApplyHeuristics> heuristics;

	
	/**
	 * Initialises the cfg and the targets to be processed.
	 * 
	 * @param cfg
	 * @param targets
	 */
	public SymTest(ICFG cfg, Set<ICFEdge> targets) {
		this(cfg, targets, null);
	}

	
	/**
	 * This constructor is to be used to add heuristics.
	 * @param cfg
	 * @param targets
	 * @param heuristics
	 */
	public SymTest(ICFG cfg, Set<ICFEdge> targets, Set<ApplyHeuristics> heuristics) {
		this.mCFG = cfg;
		this.mTargets = targets;
		this.mTarget = this.mCFG.getStopNode();
		this.mConvertor = new CFGToGraphConvertor(this.mCFG);
		if (heuristics != null) {
			this.heuristics = heuristics;
		}
	}

	
	/**
	 * This part holds the core algorithm of SymTest
	 * It performs a series of steps as follows:
	 * Find the shortest path(syntactically)
	 * Construct the SET
	 * Check if it is solvable(semantically feasible), then find a satisfiable solution.
	 * Else if heuristics are available, try the heuristics and return a solution if possible.
	 * Else find the longest viable prefix
	 * 		Update the stack
	 * 		Backtrack to the last decision and change the decision taken (if else edge was taken earlier, try for the other edge;
	 * 																	 If both edges were already explored, repeat the process) 
	 * Repeat the process with the new preferred edge. 
	 * @return
	 */
	public TestSequence generateTestSequence() {
		TestSequence testseq = null;
		try {
			Set<IEdge> targets = convertTargetEdgesToGraphEdges(this.mTargets);
			Stack<Pair<IEdge, Boolean>> stack = new Stack<Pair<IEdge, Boolean>>();
			// Initialise the stack with start edge
			IEdge startEdge = this.mConvertor.getGraphEdge(mCFG.getStartNode()
					.getOutgoingEdgeList().get(0));
			
			stack.push(new Pair<IEdge, Boolean>(startEdge, true));
			ArrayList<IEdge> prefix = new ArrayList<IEdge>();

			IPath completePath = new Path(mGraph);
			Set<IEdge> currentTargets = new HashSet<IEdge>(targets);
			while ((!stack.isEmpty()) && !(stack.peek().getFirst().equals(startEdge) && !stack.peek()
					.getSecond())) {
				// Obtain the path that traverses the targets.
				IPath path;
				if (!hasEncounteredMaximumIterations(completePath)) {
					FindCFPathAlgorithm algorithm = new FindCFPathAlgorithm(
							this.mGraph, currentTargets,
							this.mConvertor.getGraphNode(this.mTarget));

					if (stack.size() != 1) {

						path = algorithm.findCFPath(stack.peek().getFirst()
								.getHead(), currentTargets);
					} else {
						prefix.add(startEdge);
						path = algorithm.findCFPath(stack.peek().getFirst()
								.getHead(), currentTargets);
					}
				} else {
					// If maximum iterations are done, it is only an empty path
					// that gets added
					path = new Path(mGraph);
				}
				completePath.setPath(addprefix(prefix, path.getPath()));
				ArrayList<ICFEdge> cfPath = convertPathEdgesToCFGEdges(completePath);
				// Construct the Symbolic Execution Tree
				set = SymTestUtil.getSET(cfPath, this.mCFG);

				/*
				Adding code here just to check whether SET has incoming edges or not on its node
				 */
				Set<SETNode> nodeSet = set.getNodeSet();
				for(SETNode node : nodeSet){
					System.out.println("SET node Incoming Edge:" + node.getIncomingEdge());
				}


				// Solve the predicate
				SolverResult solution;
				try {

					solution = SymTestUtil.solveSequence(set);
					return (this.convert(set.getLeafNodes().iterator().next(),
							solution));
				} catch (UnSatisfiableException e) {
					System.out.println("Unsatisfiable");
				}

				// Add heuristics
				if (this.heuristics != null) {
					for (ApplyHeuristics heuristic : heuristics) {
						try {
							solution = heuristic.performHeuristics(mGraph,
									mTargets, completePath, mCFG, mConvertor);
							return (this.convert(set.getLeafNodes().iterator()
									.next(), solution));

						} catch (UnSatisfiableException e) {
							e.printStackTrace();
						}
					}
				}
				if (!hasEncounteredMaximumIterations(completePath)) {
					// Get Longest Viable Prefix(LVP)
					int satisfiableIndex = SymTestUtil
							.getLongestSatisfiablePrefix(cfPath, mCFG);
					List<IEdge> satisfiablePrefix = new ArrayList<IEdge>();
					satisfiablePrefix.addAll(completePath.getPath().subList(
							(completePath.getPath().size() - 1)
									- path.getPath().size(),
							satisfiableIndex + 2));

					updatestack(stack, satisfiablePrefix);
					prefix.clear();

					prefix.addAll(completePath.getPath().subList(
							0,
							completePath.getPath().indexOf(
									stack.peek().getFirst())));
				} else {
					prefix.clear();
					prefix.addAll(completePath.getPath().subList(
							0,
							completePath.getPath().lastIndexOf(
									stack.peek().getFirst())));
				}

				backtrack(stack);
				if (!stack.isEmpty()) {
					// Add the updated edge
					prefix.add(stack.peek().getFirst());
				}
				currentTargets.removeAll(prefix);

			}
			System.out.println("Unsatisfiable finally");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testseq;
	}

	/**
	 * This function pushes only the outgoing edge of decision node contained in the path into the stack. 
	 * @param stack
	 * @param path that hets added newly
	 */
	private void updatestack(Stack<Pair<IEdge, Boolean>> stack, List<IEdge> path) {
		for (IEdge e : path) {
			// Push only decision node's edges
			if (this.mConvertor.getCFEdge(e).getTail() instanceof ICFGDecisionNode) {
				stack.push(new Pair<IEdge, Boolean>(e, true));
			}
		}

	}

	/**
	 * Checks if the maximum iterations are explored. This avoids infinite loops.
	 * The number of iterations are hardcoded as a constant. 
	 * @param completePath
	 * @return
	 */
	private boolean hasEncounteredMaximumIterations(IPath completePath) {
		int count = 0;
		if (!mTarget.getOutgoingEdgeList().isEmpty()) {
		count = Collections.frequency(completePath.getPath(),
				mConvertor.getGraphEdge(mTarget.getOutgoingEdgeList().get(0)));
		if (count >= MAXIMUM_ITERATIONS)
			return true;
		else
			return false;
		} return false;
	}

	/**
	 * Concatenates the new path with the prefix to create the complete path, to be fed to the SEE(Symbolic Execution Engine)
	 * @param prefix
	 * @param path
	 * @return complete path
	 */
	private List<IEdge> addprefix(ArrayList<IEdge> prefix, ArrayList<IEdge> path) {
		List<IEdge> completePath = new ArrayList<IEdge>();
		completePath.addAll(prefix);
		completePath.addAll(path);
		return completePath;
	}

	/**
	 * Backtracks the stack by one step
	 * if the topmost element has (edge, true), It pushes the other edge of the decision node as (otheredge, false)
	 * if the topmost element has (edge, false), it pops the element and backtracks another step.
	 * Continues till only the start edge is in the stack.
	 * @param stack
	 * @return
	 */
	public Stack<Pair<IEdge, Boolean>> backtrack(
			Stack<Pair<IEdge, Boolean>> stack) {
		if (!stack.isEmpty()) {
			Pair<IEdge, Boolean> topmostPair = stack.pop();
			if (stack.isEmpty()) {
				return stack;
			}

			// Push the other edge of the node with a false
			if (topmostPair.getSecond()) {
				IEdge newEdge = null;
				IEdge oldEdge = topmostPair.getFirst();
				newEdge = getOtherEdge(oldEdge);
				stack.push(new Pair<IEdge, Boolean>(newEdge, false));
				return stack;
			} else
				return backtrack(stack);
		} else
			return stack;
	}

	private IEdge getOtherEdge(IEdge oldEdge) {
		IEdge newEdge = null;
		INode node = oldEdge.getTail();

		if (oldEdge.equals(node.getOutgoingEdgeList().get(0))) {
			newEdge = node.getOutgoingEdgeList().get(1);
		} else if (oldEdge.equals(node.getOutgoingEdgeList().get(1))) {
			newEdge = node.getOutgoingEdgeList().get(0);
		}
		return newEdge;
	}

	public Set<IEdge> convertTargetEdgesToGraphEdges(Set<ICFEdge> targetEdges)
			throws Exception {
		// gets
		this.mGraph = this.mConvertor.getGraph();
		Set<IEdge> targets = new HashSet<IEdge>();
		for (ICFEdge edge : targetEdges) {
			targets.add(this.mConvertor.getGraphEdge(edge));
		}
		return targets;
	}

	public ArrayList<ICFEdge> convertPathEdgesToCFGEdges(IPath path) {
		ArrayList<IEdge> list = path.getPath();
		ArrayList<ICFEdge> cfPath = new ArrayList<ICFEdge>();
		// Convert the graph edges to cfg edges.
		for (IEdge e : list) {
			cfPath.add(this.mConvertor.getCFEdge(e));
		}
		return cfPath;
	}

	/**
	 * Maps the obtained satisfiable solution with symbolic variables to actual variables  
	 * @param leaf
	 * @param solution
	 * @return
	 * @throws Exception
	 */
	private TestSequence convert(SETNode leaf, SolverResult solution)
			throws Exception {
		TestSequence ts = null;
		if (solution.getResult()) {
			SETNode currNode = leaf;
			Stack<Pair<IIdentifier, IIdentifier>> stack = new Stack<Pair<IIdentifier, IIdentifier>>();
			while (true) {
				if (currNode instanceof SETBasicBlockNode) {
					SETBasicBlockNode bb = (SETBasicBlockNode) currNode;
					Map<IIdentifier, IExpression> values = bb.getValues();
					for (IIdentifier var : values.keySet()) {
						IExpression val = values.get(var);
						if (val instanceof Variable
								&& val.getProgram().equals(set)) {
							stack.push(new Pair<IIdentifier, IIdentifier>(var,
									(IIdentifier) values.get(var)));
						}
					}
				}
				if (currNode.getIncomingEdge() == null) {
					break;
				}
				currNode = currNode.getPredecessorNode();
			}
			Map<IIdentifier, List<Object>> map = new HashMap<IIdentifier, List<Object>>();
			Map<IIdentifier, Object> concValues = solution.getModel();
			while (!stack.isEmpty()) {
				Pair<IIdentifier, IIdentifier> pair = stack.pop();
				IIdentifier var = pair.getFirst();
				IIdentifier symval = pair.getSecond();
				if (!map.keySet().contains(var)) {
					map.put(var, new ArrayList<Object>());
				}
				if (!concValues.keySet().contains(symval)) {
					if (!set.hasVariable(symval)) {
						Exception e = new Exception(
								"SymTest.convert : Symbolic value '"
										+ symval.getName()
										+ "' not present in the solver output.");
						throw e;
					} else {

					}
				}
				Object concreteValue = concValues.get(symval);
				List<Object> values = map.get(var);
				values.add(concreteValue);
			}
			ts = new TestSequence(map.keySet());
			for (IIdentifier v : map.keySet()) {
				for (Object value : map.get(v)) {
					ts.addInputValue(v, value);
				}
			}
		} else {
			throw new UnSatisfiableException();
		}
		return ts;
	}


}
