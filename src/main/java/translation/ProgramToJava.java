package translation;

import ast.*;
import program.Program;
import java.io.PrintWriter;
import java.util.HashMap;
import visitor.ExpressionVisitor;
import visitor.Visitor;
import java.util.HashSet;
import utilities.*;

public class ProgramToJava implements ExpressionVisitor, Visitor {

    Program program = null;
    PrintWriter writer;
    HashMap<String, String> typeNameMap = new HashMap<>();
    int indent_level = 0;

    void enter_nest () {
        this.indent_level++;
    }
    void exit_nest () {
        this.indent_level--;
    }

    void print(String what) {
        this.writer.print(what);
    }

	void println (String what) {
		this.writer.println(what);
	}
    void startln(String what) {
        for (int space = 0; space < indent_level; space++) {
			for (int i = 0; i < 4; i++)
            this.writer.print(" ");
        }
        this.writer.print(what);
    }

    public ProgramToJava(Program program) throws Exception {
        this.program = program;
        this.writer = new PrintWriter("javaoutput.jxx", "UTF-8");
    }

    public void addClassDec(){
    	println("class NumericExample{");
    }
    public void endClassDec(){
    	println("}");
    }
    public void translate() throws Exception {
	addHeaders();
        addClassDec();
        addFunctions();
        addTypes();
        addDeclarations();
        addSymbolicMethod();
        addStatements();
        endClassDec();
        writer.flush();
    }

	public void addHeaders() {
		println("package demo;");
		println("import gov.nasa.jpf.symbc.Debug;");
		println("import java.util.Random;");
		println("import java.io.*;");

		
	}
	public void addFunctions(){
			println("static int N = 5;\n"+
        "static int[] events = new int[N];");
		
		println(" static void non_det(boolean bnd) { \n"+
"        assert !bnd; \n"+
"    }\n"+

  "  static void stuck_spec(boolean bss) {\n"+
  "      assert !bss;\n"+
  "  }\n"+
  "  static int toInt(boolean b){\n"+
  "  return b ? 1 : 0;\n"+
  "  }\n"
);

	println(
	"  static int toInt(int b){\n"+
  "  return b;\n"+
  "  }\n"
	
	);

	}
    void addTypes() {
        this.typeNameMap.put("int", "int");
        this.typeNameMap.put("boolean", "bool");
	    // TODO
        for (Type type : this.program.types) {
        }
    }

	HashSet<String> variable_names = new HashSet<>();
	HashSet<String> symbolic_array_stmt = new HashSet<>();

    void addDeclarations() {
        for (Declaration decl : this.program.declarations) {
            writer.println("static "+this.typeNameMap.get(decl.typeName.toString()) + " " + decl.vname + ";");
            writer.println("static "+this.typeNameMap.get(decl.typeName.toString()) + "[] sym_" + decl.vname + "= new "+ decl.typeName.toString()+"[N];");
             symbolic_array_stmt.add("sym_"+decl.vname+"[i] = Debug.makeSymbolicInteger(\"sym_"+decl.vname +"\"+ i);");
			variable_names.add(decl.vname);
        }
       
		// for (Declaration decl: this.program.other_declarations) {
        //     writer.println(this.typeNameMap.get(decl.typeName.toString()) + " " + decl.vname + ";");
		// }
    }
    
    void addSymbolicMethod(){
    	writer.println(" static void makeSymbolic(){");
    	writer.println("for (int i = 0; i < N; i++) {");
    	for (String stmt : symbolic_array_stmt) {
    		writer.println(stmt);
    	}
    	writer.println("}\n}");
    }

    void addStatements() throws Exception {
	    // All the statements go into the run() function, which is called from main()
		// Event name map
		// println("char const* reverse_map[" + this.program.eventNameMap.size() + "];");
		// for (Pair<String, Integer> pair: this.program.eventNameMap.values()) {
		// 	println("reverse_map["+pair.getSecond()+"]="+"\""+pair.getFirst()+"\";");
		// }

        println("public static void run(){");
        enter_nest();
        // println("for (int i = 0; i < N; i++) {");
        // enter_nest();
		// println("event = events[i];");
		// Guaranteed to be a while
	println("makeSymbolic();");
        program.statements.visit(this);
        exit_nest();
        println("}");

        println("public static void main (String args[]) {");
        enter_nest();
		// TODO: Make 'N' a user input
	/*	println("int N = 5; int events[N];");
		println("klee_make_symbolic(events, sizeof events, \"events\");");
		for (String variable_name: variable_names) {
			System.out.println ("varname: " + variable_name);
			println("klee_make_symbolic(&" + variable_name + ", sizeof " + variable_name + ",\"" + variable_name + "\");");
		}
        println("run(events, N);");
        println("return 0;");*/
        println("run();");
        
        println("}");
        exit_nest();
    }

    // visitor methods:

	private static boolean checkIfInputExpr (Expression expr) {
		if (expr instanceof FunctionCall) {
			FunctionCall call_expr = (FunctionCall) expr;
			return call_expr.name.name.equals ("input");
		}
		return false;
	}

    public void visitAssignmentStatement(AssignmentStatement x) throws Exception {
        // writer.print ("assignment\n");
		if (ProgramToJava.checkIfInputExpr(x.rhs)) {
			return;
		}
        x.lhs.visit(this);
        this.print(" = ");
        x.rhs.visit(this);
        this.println(";");
    }

    public void visitBasicType(BasicType x) throws Exception {
    }

    public void visitBinaryExpression(BinaryExpression expr) throws Exception {
		print("(");
        expr.left.visit(this);
        if (expr.operator == "=") {
            print("==");
        } else {
            print(expr.operator);
        }
        expr.right.visit(this);
		print(")");
    }

    public void visitBooleanConstant(BooleanConstant x) throws Exception {
        print (x.value ? "true": "false");
    }

    public void visitDeclaration(Declaration x) throws Exception {
    }

    public void visitDeclarationList(DeclarationList x) throws Exception {
    }

    public void visitExpressionStatement(ExpressionStatement x) throws Exception {
		x.expression.visit(this);
		println(";");
    }

    public void visitFunctionCall(FunctionCall x) throws Exception {
		if (x.name.name.equals ("input")) {
			return;
		}
		startln (x.name.name);
		print("(");
		int n = x.argumentList.size();
		for (Expression arg: x.argumentList) {
			arg.visit (this);
			n--;
			if (n != 0) print(", ");
		}
		print(")");
    }

    public void visitFunctionDeclaration(FunctionDeclaration x) throws Exception {
    }

    public void visitFunctionName(FunctionName x) throws Exception {
		startln(x.name);
    }

    public void visitHaltStatement(HaltStatement x) throws Exception {
        println("return;");
    }

    public void visitIfStatement(IfStatement x) throws Exception {
        print("if (");
        x.condition.visit(this);
        println(") {");
        enter_nest();
        x.then_body.visit(this);
        exit_nest();
        println("}");
        if (x.else_body != null) {
            println("else {");
            enter_nest();
            x.else_body.visit(this);
            exit_nest();
            println("}");
        }
    }

    public void visitInstructionStatement(InstructionStatement x) throws Exception {
    }

    public void visitIntegerConstant(IntegerConstant x) throws Exception {
        print(String.valueOf(x.value));
    }

    public void visitName(Name x) throws Exception {
        writer.print(x.toString());
    }

    public void visitSkipStatement(SkipStatement x) throws Exception {
    }

    public void visitStatementList(StatementList x) throws Exception {
	    for (Statement stmt: x.getStatements()) {
            stmt.visit(this);
	    }
    }

    public void visitStringLiteral(StringLiteral x) throws Exception {
        print ("std::string(\"");
        print (x.value);
        print ("\")");
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
		if (x.operator == UnaryExpression.Operator.NOT) {
			println("!");
		}
		println("(");
		x.expression.visit(this);
		println(")");
    }

    public void visitWhileStatement(WhileStatement x) throws Exception {
		println("for (int i = 0; i < N; i++) {");
		enter_nest();
		println("event = events[i];");
        x.body.visit(this);
        exit_nest();
        println("}");
    }
}
