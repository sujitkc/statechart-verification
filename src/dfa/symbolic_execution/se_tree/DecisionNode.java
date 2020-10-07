package symbolic_execution.se_tree;

import ast.*;

import java.util.Map;

public class DecisionNode extends SETNode{
    
    public final Expression expression;
    public final Map<Declaration, Expression> environment;
    
    public DecisionNode(Expression e, SETNode p, Map<Declaration, Expression> m)
    {
        super(p);
		if(p==null) 
			this.depth=1;
		else 
			this.depth=p.depth+1;
        this.environment = m;
        this.expression = e;
		//System.out.println("Decision node created at depth : "+this.depth +" : "+e+ " with parent : "+p);
		if(p instanceof InstructionNode && ((InstructionNode)p).declaration!=null)
			System.out.println(this.depth+": DN Created : "+(e.toString()).replace("\n", "")+" >> Par: "+(((InstructionNode)p).declaration.toString()).replace(System.getProperty("line.separator"), ""));
		else if(p instanceof InstructionNode && ((InstructionNode)p).statement!=null)
			System.out.println(this.depth+": DN Created : "+(e.toString()).replace("\n", "")+" >> Par: "+(((InstructionNode)p).statement.toString()).replace(System.getProperty("line.separator"), ""));
		else if(p instanceof DecisionNode)
			System.out.println(this.depth+": DN created : "+(e.toString()).replace("\n", "")+" >> Par: "+(((DecisionNode)p).expression.toString()).replace(System.getProperty("line.separator"), ""));
		else if(p instanceof StateEntryNode)
			System.out.println(this.depth+": IN created : "+(e.toString()).replace("\n", "")+" >> Par: Entry - "+(((StateEntryNode)p).state).getFullName());
		else if(p instanceof StateExitNode)
			System.out.println(this.depth+": IN created : "+(e.toString()).replace("\n", "")+" >> Par: Exit - "+(((StateExitNode)p).state).getFullName());
		
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
