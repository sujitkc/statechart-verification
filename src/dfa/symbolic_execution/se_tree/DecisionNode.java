package symbolic_execution.se_tree;

import ast.*;

import java.util.Map;

public class DecisionNode extends SETNode{
    
    public final Expression expression;
    public final Map<Declaration, Expression> environment;
    
    public DecisionNode(Expression e, SETNode p, Map<Declaration, Expression> m)
    {
        super(p);
        this.environment = m;
        this.expression = e;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public Map<Declaration, Expression> getEnvironment()
    {
        return this.environment;
    }

    // Has a variable called value which is of the type SymbolicExpression
}
