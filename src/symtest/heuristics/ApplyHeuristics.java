package heuristics;

import Solver.SolverResult;
import cfg.ICFEdge;
import cfg.ICFG;
import graph.IGraph;
import graph.IPath;
import tester.CFGToGraphConvertor;

import java.util.Set;

public interface ApplyHeuristics {
	
	public SolverResult performHeuristics(IGraph graph,
                                          Set<ICFEdge> mTargets, IPath graphpath, ICFG cfg,
                                          CFGToGraphConvertor mConvertor) throws Exception ;

}
