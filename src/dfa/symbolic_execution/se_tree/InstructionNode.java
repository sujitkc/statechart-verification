package symbolic_execution.se_tree;

import ast.*;

import java.util.Map;

public class InstructionNode extends SETNode{

    public final Statement statement;
    public Map<Declaration, Expression> environment;
	public final Declaration declaration;
    public InstructionNode(Statement s, SETNode p, Map<Declaration, Expression> m)
    {
        super(p);
        this.statement = s;
        this.environment = m;
		this.declaration=null;
    }
	public InstructionNode(Declaration d, SETNode p, Map<Declaration, Expression> m)
    {
        super(p);
        this.statement = null;
        this.environment = m;
		this.declaration=d;
    }
	
    public Statement getStatement()
    {
        return this.statement;
    }

    public Map<Declaration, Expression> getEnvironment()
    {
        return this.environment;
    }

    // A map of <variable, SymbolicExpression>
    
}
