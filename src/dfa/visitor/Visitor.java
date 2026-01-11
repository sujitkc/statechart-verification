package visitor;

import ast.*;

public interface Visitor {
    public void visitAssignmentStatement(AssignmentStatement x) throws Exception;
    public void visitBasicType(BasicType x) throws Exception;
    public void visitBinaryExpression(BinaryExpression x) throws Exception;
    public void visitBooleanConstant(BooleanConstant x) throws Exception;
    public void visitDeclaration(Declaration x) throws Exception;
    public void visitDeclarationList(DeclarationList x) throws Exception;
    public void visitExpressionStatement(ExpressionStatement x) throws Exception;
    public void visitFunctionCall(FunctionCall x) throws Exception;
    public void visitFunctionDeclaration(FunctionDeclaration x) throws Exception;
    public void visitFunctionName(FunctionName x) throws Exception;
    public void visitHaltStatement(HaltStatement x) throws Exception;
    public void visitIfStatement(IfStatement x) throws Exception;
    public void visitInstructionStatement(InstructionStatement x) throws Exception;
    public void visitIntegerConstant(IntegerConstant x) throws Exception;
    public void visitName(Name x) throws Exception;
    public void visitSkipStatement(SkipStatement x) throws Exception;
    public void visitStatementList(StatementList x) throws Exception;
    public void visitStringLiteral(StringLiteral x) throws Exception;
    public void visitStruct(Struct x) throws Exception;
    public void visitType(Type x) throws Exception;
    public void visitTypeName(TypeName x) throws Exception;
    public void visitTypeVariable(TypeVariable x) throws Exception;
    public void visitUnaryExpression(UnaryExpression x) throws Exception;
    public void visitWhileStatement(WhileStatement x) throws Exception;
}