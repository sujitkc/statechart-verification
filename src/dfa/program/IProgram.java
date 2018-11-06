package program;

import java.util.Set;

import ast.Name;

public interface IProgram {
	public Name addVariable(Name var);
	public Set<Name> getVariables();
	boolean hasVariable(Name var);
}
