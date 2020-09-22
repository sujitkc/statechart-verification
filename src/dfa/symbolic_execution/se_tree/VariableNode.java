package symbolic_execution.se_tree;

import ast.*;

import java.util.Map;

public class VariableNode extends SETNode{
    
    public final Declaration declaration;
    public final Expression expression;


    public VariableNode(SETNode p, Declaration d, Expression e){
        super(p);
        this.declaration = d;
        this.expression = e;
    }

    public Declaration getDeclaration(){
        return this.declaration;
    }

}
