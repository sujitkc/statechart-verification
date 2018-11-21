package tester;

import expression.IIdentifier;

import java.util.*;

public class TestSequence {
	
	private Map<IIdentifier, List<Object>> mTestSequence = new HashMap<IIdentifier, List<Object>>();
	
	public TestSequence(Set<IIdentifier> vars) {
		for(IIdentifier var : vars)
		this.mTestSequence.put(var, new LinkedList<Object>());
	}
	
	
	public void addInputValue(IIdentifier v, Object val) {
		List<Object> valueList = this.mTestSequence.get(v);
		valueList.add(val);
	}
	
	public Map<IIdentifier, List<Object>> getTestSequence() {
		return this.mTestSequence;
	}
	
	public String toString() {
		String s = "Test Sequence = ";
		for (IIdentifier var : mTestSequence.keySet()) {
			s = s + this.varValuesToString(var) + "\n";
		}
		return s;
	}

	private String varValuesToString(IIdentifier var) {
		String s = "variable '" + var.getName() + "' = {";
		List<Object> values = this.mTestSequence.get(var);
		for(Object value : values) {
			if(value != null) {
				s = s + value.toString() + ", ";
			}
			else {
				s = s + "X" + " ";
			}
		}
		s = s + "}";
		return s;
	}
}
