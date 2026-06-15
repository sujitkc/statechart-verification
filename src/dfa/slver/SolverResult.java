package Solver;

import ast.Name;

import java.util.Map;

public class SolverResult {
  private final boolean mResult;
  private final Map<Name, Object> mModel;
  
  public SolverResult(boolean result, Map<Name, Object> model) {
    this.mResult = result;
    this.mModel = model;
  }
  public SolverResult(Map<Name, Object> model) {
    this.mModel = model;
    this.mResult = false;
  }
  
  public boolean getResult() {
    return this.mResult;
  }
  
  public Map<Name, Object> getModel() {
    return this.mModel;
  }
  
  public String toString() {
    String s = "";
    if(this.mResult == false) {
      return "unsatisfiable\n";
    }
    s = "satisfiable\n";
    for(Name var : this.mModel.keySet()) {
      s = s + "(" + var.getName() + ", " + this.mModel.get(var).toString() + ")\n";
    }
    return s;
  }
}
