package Solver;

import expression.IIdentifier;

import java.util.Map;

public class SolverResult {
	private final boolean mResult;
	private final Map<IIdentifier, Object> mModel;
	
	public SolverResult(boolean result, Map<IIdentifier, Object> model) {
		this.mResult = result;
		this.mModel = model;
	}
	public SolverResult(Map<IIdentifier, Object> model) {
		this.mModel = model;
		this.mResult = false;
	}
	
	public boolean getResult() {
		return this.mResult;
	}
	
	public Map<IIdentifier, Object> getModel() {
		return this.mModel;
	}
	
	public String toString() {
		String s = "";
		if(this.mResult == false) {
			return "unsatisfiable\n";
		}
		s = "satisfiable\n";
		for(IIdentifier var : this.mModel.keySet()) {
			s = s + "(" + var.getName() + ", " + this.mModel.get(var).toString() + ")\n";
		}
		return s;
	}
}
