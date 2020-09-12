package translation;

import ast.*;
import program.Program;
import java.io.PrintWriter;
import java.util.HashMap;
import visitor.Visitor;

public class ProgramToCpp implements Visitor {

    Program program = null;
    PrintWriter writer;
    HashMap<String, String> typeNameMap = new HashMap<>();

    public ProgramToCpp(Program program) throws Exception {
        this.program = program;
        this.writer = new PrintWriter("output.cxx", "UTF-8");
    }

    public void translate() throws Exception {
        addTypes();
        addDeclarations();
        addStatements();
        writer.flush();
    }

    // TODO
    void addTypes() {
        this.typeNameMap.put("int", "int");
        this.typeNameMap.put("boolean", "bool");
        for (Type type : this.program.types) {
        }
    }

    void addDeclarations() {
        for (Declaration decl : this.program.declarations) {
            writer.println(this.typeNameMap.get(decl.typeName.toString()) + " " + decl.vname + ";");
        }
    }

    void addStatements() throws Exception {
        writer.print("void run () {\n");
        this.program.statements.visit(this);
        writer.print("}\n");

        writer.print("int main () {\n");
        writer.print("run ();\n}\n");
    }

    // visitor methods
    public void visitAssignmentStatement(AssignmentStatement x) throws Exception {
        // writer.print ("assignment\n");
        x.lhs.visit(this);
        writer.print(" = ");
        x.rhs.visit(this);
        writer.print(";\n");
    }

    public void visitBasicType(BasicType x) throws Exception {
    }

    public void visitBinaryExpression(BinaryExpression expr) throws Exception {
        expr.left.visit(this);
        if (expr.operator == "=") {
            writer.write("==");
        } else {
            writer.write(expr.operator);
        }
        expr.right.visit(this);
    }

    public void visitBooleanConstant(BooleanConstant x) throws Exception {
        writer.print(x.value);
    }

    public void visitDeclaration(Declaration x) throws Exception {
    }

    public void visitDeclarationList(DeclarationList x) throws Exception {
    }

    public void visitExpressionStatement(ExpressionStatement x) throws Exception {
    }

    public void visitFunctionCall(FunctionCall x) throws Exception {
    }

    public void visitFunctionDeclaration(FunctionDeclaration x) throws Exception {
    }

    public void visitFunctionName(FunctionName x) throws Exception {
    }

    public void visitHaltStatement(HaltStatement x) throws Exception {
        writer.print("return;\n");
    }

    public void visitIfStatement(IfStatement x) throws Exception {
        writer.print("if (");
        x.condition.visit(this);
        writer.print(") {\n");
        x.then_body.visit(this);
        writer.print("}\n");
        if (x.else_body != null) {
            writer.print("else {");
            x.else_body.visit(this);
            writer.print("}\n");
        }
    }

    public void visitInstructionStatement(InstructionStatement x) throws Exception {
    }

    public void visitIntegerConstant(IntegerConstant x) throws Exception {
        writer.print(x.value);
    }

    public void visitName(Name x) throws Exception {
        writer.print(x.toString());
    }

    public void visitSkipStatement(SkipStatement x) throws Exception {
    }

    public void visitStatementList(StatementList x) throws Exception {
    }

    public void visitStringLiteral(StringLiteral x) throws Exception {
    }

    public void visitStruct(Struct x) throws Exception {
    }

    public void visitType(Type x) throws Exception {
    }

    public void visitTypeName(TypeName x) throws Exception {
    }

    public void visitTypeVariable(TypeVariable x) throws Exception {
    }

    public void visitUnaryExpression(UnaryExpression x) throws Exception {
    }

    public void visitWhileStatement(WhileStatement x) throws Exception {
        writer.print("while (");
        x.condition.visit(this);
        writer.print(") {\n");
        x.body.visit(this);
        writer.print("}\n");
    }
}