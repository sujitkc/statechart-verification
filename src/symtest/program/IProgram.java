package program;

import expression.IIdentifier;

import java.util.Set;

public interface IProgram {
	public IIdentifier addVariable(IIdentifier var);
	public Set<IIdentifier> getVariables();
	boolean hasVariable(IIdentifier var);
}
