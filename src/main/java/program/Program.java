package program;

import java.util.List;
import java.util.HashMap;
import ast.*;
import utilities.*;


public class Program {
  public final String name;
  public final DeclarationList declarations; // Non-symbolic declarations only
  public DeclarationList other_declarations; // relevant to translation process
  public final List<Type> types;
  public final List<FunctionDeclaration> functionDeclarations;
  public HashMap<String, Pair<String, Integer>> eventNameMap;

  public final StatementList statements;
  public Program(
      String name,
      DeclarationList declarations,
      List<Type> types,
      List<FunctionDeclaration> functionDeclarations,
      StatementList statements) {
    this.name = name;
    this.declarations = declarations;
    this.types = types;
    this.functionDeclarations = functionDeclarations;
    this.statements = statements;
  }

  public void set_other_declarations (DeclarationList other_declarations) {
	  this.other_declarations = other_declarations;
  }
  public void setEventNameMap (HashMap<String, Pair<String, Integer>> eventNameMap) {
	  this.eventNameMap = eventNameMap;
  }

  public String toString() {
    String s = "Program {\n";
    s += this.declarations;
	s += this.other_declarations;
    s += this.statements.toString();
    s += "}";
    return s;
  }
}
