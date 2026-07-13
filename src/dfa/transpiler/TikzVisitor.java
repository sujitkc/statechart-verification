package transpiler;

import com.sun.source.tree.ExpressionStatementTree;

import ast.*;
import visitor.*;


public class TikzVisitor implements Visitor {

    private StringBuilder sb = new StringBuilder();


    public String getOutput() {

        return this.sb.toString();
    }


    public void dispatchExpression(Expression expr) throws Exception {

        if (expr == null) return;

        switch (expr) {

            case BinaryExpression x -> visitBinaryExpression(x);
            case BooleanConstant  x -> visitBooleanConstant(x);
            case IntegerConstant  x -> visitIntegerConstant(x);
            case FunctionCall     x -> visitFunctionCall(x);
            case StringLiteral    x -> visitStringLiteral(x);
            case UnaryExpression  x -> visitUnaryExpression(x);
            case Name             x -> visitName(x);
            default                 -> throw new Exception("Unsupported Expression: ./src/dfa/transpiler/TikzVisitor");
        }
    }


    public void dispatchStatement(Statement stmt) throws Exception {

        if (stmt == null) return;

        switch (stmt) {

            case AssignmentStatement  x -> visitAssignmentStatement(x);
            case ExpressionStatement  x -> visitExpressionStatement(x);
            case HaltStatement        x -> visitHaltStatement(x);
            case IfStatement          x -> visitIfStatement(x);
            case SkipStatement        x -> visitSkipStatement(x);
            case StatementList        x -> visitStatementList(x);
            case WhileStatement       x -> visitWhileStatement(x);
            case InstructionStatement x -> visitInstructionStatement(x);
            default                     -> throw new Exception("Unsupported Statement: ./src/dfa/transpiler/TikzVisitor");
        }
    }

    
    public void dispatchType(Type type) throws Exception {

        if (type == null) return;

        switch (type) {

            case BasicType    x -> visitBasicType(x);
            case Struct       x -> visitStruct(x);
            case TypeVariable x -> visitTypeVariable(x);
            default             -> visitType(type);
        }
    }


    public void dispatchMisc(Object obj) throws Exception {

        if (obj == null) return;

        switch (obj) {

            case Declaration         x -> visitDeclaration(x);
            case DeclarationList     x -> visitDeclarationList(x);
            case FunctionDeclaration x -> visitFunctionDeclaration(x);
            case FunctionName        x -> visitFunctionName(x);
            case TypeName            x -> visitTypeName(x);
            default                    -> throw new Exception("Unsupported Declaration: ./src/dfa/transpiler/TikzVisitor");
        }
    }


    @Override
    public void visitAssignmentStatement(AssignmentStatement x) throws Exception {

        this.dispatchExpression((Expression)x.lhs);
        this.sb.append(" \\leftarrow ");
        this.dispatchExpression(x.rhs);
        this.sb.append("; ");
    }


    @Override public void visitBasicType(BasicType x)                       throws Exception {}

    @Override
    public void visitBinaryExpression(BinaryExpression x) throws Exception {

        this.sb.append("(");
        this.dispatchExpression(x.left);

        this.sb.append(" " +
        
            switch (x.operator) {

                case "&&" -> "\\land";
                case "||" -> "\\lor";
                case "!=" -> "\\neq";
                case "<=" -> "\\leq";
                case ">=" -> "\\geq";
                case "="  -> "=";
                default   -> throw new Exception("Unsupported Operator: ./src/dfa/transpiler/TikzVisitor");
            }

            + " ");

        this.dispatchExpression(x.right);
        this.sb.append(")");
    }


    @Override
    public void visitBooleanConstant(BooleanConstant x) throws Exception {

        this.sb.append(x.value);
    }

    @Override public void visitDeclaration(Declaration x)                   throws Exception {}
    @Override public void visitDeclarationList(DeclarationList x)           throws Exception {}

    @Override
    public void visitExpressionStatement(ExpressionStatement x) throws Exception {

        this.dispatchExpression(x.expression);
        this.sb.append("; ");
    }


    @Override
    public void visitFunctionCall(FunctionCall x) throws Exception {

        this.sb.append("\\text{" + x.name.name + "}(");

        for (int i = 0; i < x.argumentList.size(); i++) {

            dispatchExpression(x.argumentList.get(i));

            if (i < x.argumentList.size() - 1)
                this.sb.append(", ");
        }

        this.sb.append(")");
    }

    @Override public void visitFunctionDeclaration(FunctionDeclaration x)   throws Exception {}
    @Override public void visitFunctionName(FunctionName x)                 throws Exception {}

    @Override
    public void visitHaltStatement(HaltStatement x) throws Exception {

        this.sb.append("\\text{instruction}; ");
    }

    @Override
    public void visitIfStatement(IfStatement x) throws Exception {

        this.sb.append("\\text{if } (");
        this.dispatchExpression(x.condition);
        this.sb.append(") \\{ ");
        this.dispatchStatement(x.then_body);
        this.sb.append(" \\} \\text{ else } \\{ ");
        this.dispatchStatement(x.else_body);
        this.sb.append(" \\} ");
    }

    @Override
    public void visitIntegerConstant(IntegerConstant x) throws Exception {

        this.sb.append(x.value);
    }

    @Override public void visitInstructionStatement(InstructionStatement x) throws Exception {}

    @Override
    public void visitName(Name x) throws Exception {

        this.sb.append(String.join(".", x.name));
    }

    @Override public void visitSkipStatement(SkipStatement x)               throws Exception {}

    @Override
    public void visitStatementList(StatementList x) throws Exception {

        for (Statement s : x.getStatements())
            this.dispatchStatement(s);
    }


    @Override
    public void visitStringLiteral(StringLiteral x) throws Exception {

        this.sb.append("\\text{\"" + x.value + "\"}");
    }

    @Override public void visitStruct(Struct x)                             throws Exception {}
    @Override public void visitType(Type x)                                 throws Exception {}
    @Override public void visitTypeName(TypeName x)                         throws Exception {}
    @Override public void visitTypeVariable(TypeVariable x)                 throws Exception {}

    @Override public void visitUnaryExpression(UnaryExpression x) throws Exception {

        this.sb.append(x.operator);
        dispatchExpression(x.expression);
    }

    @Override
    public void visitWhileStatement(WhileStatement x) throws Exception {

        this.sb.append("\\text{while } (");
        this.dispatchExpression(x.condition);
        this.sb.append(") \\{ ");
        this.dispatchStatement(x.body);
        this.sb.append(" \\} ");
    }
}