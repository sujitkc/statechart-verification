package program;

import java.util.List;
import java.util.ArrayList;
import ast.*;

public class Program {

  public final String name;
  public final DeclarationList declarations;
  public final List<Type> types;
  public final List<FunctionDeclaration> functionDeclarations;

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

  public String toString() {
    String s = "Program {\n";
    s += this.declarations;
    s += this.statements.toString();
    s += "}";
    return s;
  }
}
