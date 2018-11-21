package tester;

import Solver.ISolver;
import Solver.SolverResult;
import Solver.YicesSolver;
import cfg.ICFEdge;
import cfg.ICFG;
import expression.IExpression;
import expression.IIdentifier;
import see.SEENew2;
import set.SET;
import set.SETNode;

import java.util.List;
import java.util.Set;

/**
 * Contains the common functionalities required for SymTest
 * @author pavithra
 *
 */
public class SymTestUtil {
	
	public static SolverResult solveSequence(SET set) throws Exception {
		set.updateLeafNodeSet();
		Set<SETNode> leaves = set.getLeafNodes();
		if (leaves.size() != 1) {
			Exception e = new Exception(
					"SymTest.generateTestSequence : SET should have 1 and only 1 leaf. It has "
							+ leaves.size());
			throw e;
		}

		// The check for leaves(Set) having a single node is already done.
		SETNode leaf = leaves.iterator().next();

		IExpression exp = leaf.getPathPredicate();
		/**
		 * Changed by Shyam - only next line
		 */

//		System.out.println(exp);


		System.out.println("path predicate = " + exp.toString());
		Set<IIdentifier> symVars = set.getVariables();
		// Using SMT Solver
		ISolver solver = new YicesSolver(symVars, exp);
//		ISolver solver = new DRealSolver(symVars, exp);
		SolverResult solution = solver.solve();
		return solution;
	}
	
	public static SET getSET(List<ICFEdge> path, ICFG mCFG) throws Exception {
		SEENew2 mSEE = new SEENew2(mCFG);
//		mSEE.expandSET(path);
		return mSEE.getSET();
	}
	
	public static int getLongestSatisfiablePrefix(List<ICFEdge> path, ICFG cfg)
			throws Exception {
		return binsearch(path, 0, path.size(), cfg);
	}

	private static int binsearch(List<ICFEdge> path, int start, int end,
			ICFG cfg) throws Exception {
		if (start >= (end-1)) {
			return start;
		}
		int mid = (start + end) / 2;
		if (isSAT(path.subList(0, (mid + 1)), cfg))
			return binsearch(path, mid, end, cfg);
		else
			return binsearch(path, start, mid, cfg);

	}

	private static boolean isSAT(List<ICFEdge> path, ICFG cfg) throws Exception {
		SET set = getSET(path, cfg);
		// Solve the predicate
		SolverResult solution = solveSequence(set);

		return solution.getResult();
	}
	
}