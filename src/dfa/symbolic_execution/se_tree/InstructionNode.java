package symbolic_execution.se_tree;

import ast.*;

import java.util.*;
import visitors.SETExpressionVisitor;

public class InstructionNode extends SETNode{

    public final Statement statement;
    public Map<Declaration, Expression> environment;
	private Map<Name, Expression> mValues = new HashMap<Name, Expression>();

	public final Declaration declaration;
	public String symval="";
    public InstructionNode(Statement s, SETNode p, Map<Declaration, Expression> m, Set<String> symvars)
    {
        super(p);
		if(p==null) 
			this.depth=1;
		else 
			this.depth=p.depth+1;
        this.statement = s;
        this.environment = m;
		this.declaration=null;
		//this.symval=null;
		if(p instanceof InstructionNode && ((InstructionNode)p).declaration!=null)
			System.out.println(this.depth+": IN created : "+(s.toString()).replace("\n", "")+" >> Par: "+(((InstructionNode)p).declaration.toString()).replace("\n", ""));
		else if(p instanceof InstructionNode && ((InstructionNode)p).statement!=null)
			System.out.println(this.depth+": IN created : "+(s.toString()).replace("\n", "")+" >> Par: "+(((InstructionNode)p).statement.toString()).replace("\n", ""));
		else if(p instanceof DecisionNode)
			System.out.println(this.depth+": IN created : "+(s.toString()).replace("\n", "")+" >> Par: "+(((DecisionNode)p).expression.toString()).replace("\n", ""));
		else if(p instanceof StateEntryNode)
			System.out.println(this.depth+": IN created : "+(s.toString()).replace("\n", "")+" >> Par: Entry - "+(((StateEntryNode)p).state).getFullName());
		else if(p instanceof StateExitNode)
			System.out.println(this.depth+": IN created : "+(s.toString()).replace("\n", "")+" >> Par: Exit - "+(((StateExitNode)p).state).getFullName());
		
		if(s instanceof AssignmentStatement){
			//System.out.println("Assign :  "+ ((AssignmentStatement)s).lhs +" with "+((AssignmentStatement)s).rhs);
			SETExpressionVisitor visitor = new SETExpressionVisitor(this,
					(((AssignmentStatement)s).lhs).getType());
			if(((AssignmentStatement)s).rhs instanceof FunctionCall){
				if((((FunctionCall)((AssignmentStatement)s).rhs).name.getName()).equalsIgnoreCase("input"))
				{	
					
					this.mValues.put(((AssignmentStatement)s).lhs,visitor.visit((FunctionCall)((AssignmentStatement)s).rhs));
					visitor.symvars=symvars;
					System.out.println(mValues);
					//creating symbolic variable name
					//String symvar="symvar";
					//symvar+=(((AssignmentStatement)s).lhs).name;
					//SETExpressionVisitor sev=new SETExpressionVisitor();
					//this.m.putAll(SETExpressionVisitor.visit(((AssignmentStatement)s).lhs,symvars));
					//this.env.put(((Name)(((AssignmentStatement)s).lhs).name).getDeclaration(),this.symval);
					//System.out.println("Created symbolic value here : "+this.symval);
				}
				
				else{
					System.out.println("Some expression found ::: "+(((AssignmentStatement)s).rhs).getClass());
					this.symval=null;
				}
			}
			else if((((AssignmentStatement)s).rhs) instanceof UnaryExpression){
							this.symval=null;
							
							System.out.println("Unary expression found");

				}
			else if((((AssignmentStatement)s).rhs) instanceof BinaryExpression){
							BinaryExpression b=(BinaryExpression)(((AssignmentStatement)s).rhs);
							visitor.visit(b);
							this.mValues.put(((AssignmentStatement)s).lhs,visitor.visit((BinaryExpression)((AssignmentStatement)s).rhs));
							System.out.println(mValues);
							this.symval=null;
							//System.out.println("Binary expression found with variables : "+((BinaryExpression)((AssignmentStatement)s).rhs).left+" : "+((BinaryExpression)((AssignmentStatement)s).rhs).right+" : "+((BinaryExpression)((AssignmentStatement)s).rhs).operator);
				}
			else{
				System.out.println("Some expression found : "+(((AssignmentStatement)s).rhs).getClass());
				this.symval=null;
				}
		}
		else if(s instanceof ExpressionStatement){
					this.symval=null;
		}
		else
			this.symval=null;

		
		// As per the type of the statement - get the symbolic value and add it to the environment
		//as per the variable on lhs, get and add the corresponding declaration as well

    }
	public InstructionNode(Declaration d, SETNode p, Map<Declaration, Expression> m)
    {
        super(p);
		if(p==null) 
			this.depth=1;
		else 
			this.depth=p.depth+1;
        this.statement = null;
        this.environment = m;
		this.declaration=d;
		this.symval=null;
		if(p instanceof InstructionNode && ((InstructionNode)p).declaration!=null)
			System.out.println(this.depth+": IN created : "+(d.toString()).replace("\n", "")+" >> Par: "+(((InstructionNode)p).declaration.toString()).replace("\n", ""));
		else if(p instanceof InstructionNode && ((InstructionNode)p).statement!=null)
			System.out.println(this.depth+": IN created : "+(d.toString()).replace("\n", "")+" >> Par: "+(((InstructionNode)p).statement.toString()).replace("\n", ""));
		else if(p instanceof DecisionNode)
			System.out.println(this.depth+": IN created : "+(d.toString()).replace("\n", "")+" >> Par: "+(((DecisionNode)p).expression.toString()).replace("\n", ""));
		else if(p instanceof StateEntryNode)
			System.out.println(this.depth+": IN created : "+(d.toString()).replace("\n", "")+" >> Par: Entry - "+(((StateEntryNode)p).state).getFullName());
		else if(p instanceof StateExitNode)
			System.out.println(this.depth+": IN created : "+(d.toString()).replace("\n", "")+" >> Par: Exit - "+(((StateExitNode)p).state).getFullName());
		
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
