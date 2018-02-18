import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import ast.Statechart;
import ast.State;
import ast.Transition;
import ast.Declaration;
import ast.DeclarationList;
import ast.Expression;
import ast.Type;
import ast.Environment;
import ast.Name;
import ast.Statement;
import ast.IfStatement;
import ast.WhileStatement;
import ast.AssignmentStatement;
import ast.ExpressionStatement;
import ast.StatementList;
import ast.BooleanConstant;
import ast.IntegerConstant;
import ast.BinaryExpression;
import ast.BasicType;
import ast.Struct;
import ast.FunctionDeclaration;
import ast.FunctionCall;

public class Typechecker {

  private final Statechart statechart;

  public Typechecker(Statechart statechart) {
    this.statechart = statechart;
  }

  public void typecheckDeclarations(State state) throws Exception {

    for(Declaration dec : state.declarations) {
      Type type = this.statechart.lookupType(dec.typeName);
      if(type == null) {
        throw new Exception(
          "Declaration '" + dec.toString() + "' in state '" + state.name +
          "' didn't typecheck : declaration type '" +
          dec.typeName + "' doesn't exist.");
      }
      else {
        dec.setType(type);
      }
    }
    for(State st : state.states) {
      typecheckDeclarations(st);
    }
  }

  public void typecheckFunctionDeclarations(Statechart statechart) throws Exception {

    for(FunctionDeclaration fdec : statechart.functionDeclarations) {
      Type type = this.statechart.lookupType(fdec.returnTypeName);
      if(type == null) {
        throw new Exception(
          "Function Declaration '" + fdec.name + 
          "' didn't typecheck : function return type '" +
          fdec.returnTypeName + "' doesn't exist.");
      }
      else {
        fdec.setReturnType(type);

        DeclarationList args = fdec.getArgumentList();
        for(Declaration dec : args) {
          Type argtype = this.statechart.lookupType(dec.typeName);
          if(argtype == null) {
            throw new Exception(
              "Argument type '" + dec.toString() + "' in function declaration '" + fdec.name +
              "' didn't typecheck : argument type '" +
              dec.typeName + "' doesn't exist.");
          }
          else {
            dec.setType(argtype);
          }
        }       
      }
    }
  }

  public void typecheck() throws Exception {

    this.typecheckTypeDeclarations();
    this.typecheckDeclarations();
    this.typecheckFunctionDeclarations();
    this.typecheckState();
  }

  private void typecheckTypeDeclarations() throws Exception {
    for(int i = 0; i < this.statechart.types.size(); i++) {
      Type td = this.statechart.types.get(i);
      if(td instanceof Struct) {
        this.typecheckStruct((Struct)td, i);
      }
    }
  }

  private Type getKnownType(String tname, int i) {
    for(int j = 0; j < i; j++) {
      Type type = this.statechart.types.get(j);
      if(type.name.equals(tname)) {
        return type;
      }
    }
    return null;
  }

  private void typecheckStruct(Struct struct, int i) throws Exception {
    for(Declaration d : struct.declarations) {
      Type type = this.getKnownType(d.typeName, i);
      if(type == null) {
        throw new Exception("Struct typecheck error : type '" + d.typeName + "' unknown in " + struct.name);
      }
      else {
        d.setType(type);
      }
    }
  }
 
  private void typecheckDeclarations() throws Exception {
    this.typecheckDeclarations(this.statechart);
  }
 
  private void typecheckFunctionDeclarations() throws Exception {
    this.typecheckFunctionDeclarations(this.statechart);
  }

  private Declaration getDeclarationInState(ListIterator<String> nameIterator, State state) {
    if(nameIterator.hasNext()) {
      String singleName = nameIterator.next(); // name of state
      if(!nameIterator.hasNext()) {
        return null;
      }
      
      String nextName = nameIterator.next(); // iterator.peek - step 1
      nameIterator.previous(); // iterator.peek - step 2

      State s = state.findSubstateByName(nextName);
      if(s != null) {
        return this.getDeclarationInState(nameIterator, s);
      }
      else {
        return state.declarations.lookup(nextName);
      }
    }
    else {
      return null; // error : empty name
    }
  }

  private Type getTypeOfName(ListIterator<String> nameIterator, Type type) {
    if(nameIterator.hasNext()) {
      String singleName = nameIterator.next();
      if(nameIterator.hasNext()) {
        if(type instanceof Struct) {
          Struct structType = (Struct)type;
          String nextName = nameIterator.next();
          nameIterator.previous();
          Declaration d = structType.declarations.lookup(nextName);
          if(d != null) {
            return this.getTypeOfName(nameIterator, d.getType());
          }
          else {
            // error: no field of the given name in the struct type
            return null;
          }
        }
        else {
          // error: name has more fields but the current type is not a struct.
          return null;
        }
      }
      else {
        return type;
      }
    }
    else {
      return null;
    }
  }

  private void typecheckName(Name name, Environment env) throws Exception {
    Declaration dec = null;
    Type type = null;
    if(name.name.size() > 0) {
      ListIterator<String> nameIterator = name.name.listIterator();
      if(name.name.get(0).equals(this.statechart.name)) {
       /*
        Form of the name: sc.s1.s2.....sn.v.f1.f2.....fm.
        Here,
          sc: statechart
          s1 belongs to states(sc)
          for 1 <= i < n - 1, s_i = state(s_(i+1))
          v:T belongs to vars(sn).
       */
        dec = this.getDeclarationInState(nameIterator, this.statechart);
      }
      else { // Not fully qualified variable name.
      /*
      Form of the name: v.f1.f2.....fm.
      Here,
        v:T belongs to env.
      */

       dec = env.lookup(name.name.get(0));
      }
      if(dec == null) {
        throw new Exception("Name " + name + " didn't type check. Couldn't locate a variable with matching description.");
      }
      List<Declaration> decs = env.getAllDeclarations();
      if(!decs.contains(dec)) {
        throw new Exception("name didn't type check. The name exists but is not in scope.");
      }
      /*
      Here,
        if m != 0 then
          T is struct type
          f_1:T_1 belongs to fields(T) where T_1 is a struct type
          f_2:T_2 belongs to fields(T_1) where T_2 is a struct type
              ...       ...       ...
              ...       ...       ...
          f_m:T_m belongs to fields(T_(m-1)) where T_m is any arbitrary type.
        else
          T is any type.
     */
      type = this.getTypeOfName(nameIterator, dec.getType());
    }
    else {
      throw new Exception("Empty variable name.");
    }

    name.setDeclaration(dec);
    name.setType(type);
  }

  private void typecheckBooleanConstant(BooleanConstant b) {
    b.setType(this.statechart.lookupType("boolean"));
  }

  private void typecheckIntegerConstant(IntegerConstant b) {
    b.setType(this.statechart.lookupType("int"));
  }

  private void typecheckBinaryExpression(BinaryExpression b, Environment env) throws Exception {
    typecheckExpression(b.left, env);
    typecheckExpression(b.right, env);
    if(
        b.operator.equals("+") ||
        b.operator.equals("*") ||
        b.operator.equals("-") ||
        b.operator.equals("/") ) {

      Type type = this.statechart.lookupType("int");
      if(b.left.getType().equals(type) &&
        b.right.getType().equals(type)) {
        b.setType(type);
      }
      else {
        throw new Exception("typecheckBinaryExpression failed: operant type mismatch between " + b.left + " and " + b.right);
      }
   }
    else if(
        b.operator.equals(">=") ||
        b.operator.equals(">") ||
        b.operator.equals("<=") ||
        b.operator.equals("<") ||
        b.operator.equals("!=") ||
        b.operator.equals("=") ) {

      Type type = this.statechart.lookupType("int");
      if(b.left.getType().equals(type) &&
        b.right.getType().equals(type)) {
        b.setType(this.statechart.lookupType("boolean"));
      }
      else {
        throw new Exception("typecheckBinaryExpression failed: operant type mismatch between " + b.left + " and " + b.right);
      }
    }
    else if(
        b.operator.equals("&&") ||
        b.operator.equals("||") ) {

      Type type = this.statechart.lookupType("boolean");
      if(b.left.getType().equals(type) &&
        b.right.getType().equals(type)) {
        b.setType(type);
      }
      else {
        throw new Exception("typecheckBinaryExpression failed: operant type mismatch between " + b.left + " and " + b.right);
      }
    }
  }

  private void typecheckFunctionCall(FunctionCall funcCall, Environment env) throws Exception {
    /*
      typecheck the function name.
      If there's no function declaration of that name: raise error.
      Else:
        for each argument expression arg:
          type check arg.
          If the type of arg doesn't match the type of the corresponding formal parameter, then raise error.
          Else OK.
    */

    FunctionDeclaration fdec = this.statechart.lookupFunctionDeclaration(funcCall.name);
    if(fdec == null) {
      throw new Exception("function call didn't typecheck: function by name "+ funcCall.name + " not found.");
    }
    DeclarationList parameterList = fdec.getArgumentList();
    if(parameterList.size() != funcCall.argumentList.size()) {
      throw new Exception("function call " + funcCall.name + " didn't typecheck: incorrect number of arguments. " +
        parameterList.size() + " expected; " +
        funcCall.argumentList.size() + " provided.");
      
    }
    for(int i = 0; i < funcCall.argumentList.size(); i++) {
      Expression arg = funcCall.argumentList.get(i);
      this.typecheckExpression(arg, env);
      Type targ = arg.getType();
      Declaration param = parameterList.get(i);
      if(param.getType().equals(targ) == false) {
        throw new Exception("function call didn't typecheck: arguments " +
           arg + " has type " + arg.getType() + ". " +
           param.getType().name + " expected.");
      }
    }
    funcCall.setType(fdec.getReturnType());
  }

  private void typecheckExpression(Expression exp, Environment env) throws Exception {
    if(exp instanceof Name) {
      this.typecheckName((Name)exp, env); 
    }
    else if(exp instanceof BooleanConstant) {
      this.typecheckBooleanConstant((BooleanConstant)exp); 
    }
    else if(exp instanceof IntegerConstant) {
      this.typecheckIntegerConstant((IntegerConstant)exp); 
    }
    else if(exp instanceof BinaryExpression) {
      this.typecheckBinaryExpression((BinaryExpression)exp, env); 
    }
    else if(exp instanceof FunctionCall) {
      this.typecheckFunctionCall((FunctionCall)exp, env);
    }
    else {
      throw new Exception("Typechecking failed for expression: " + exp.toString() +
        " of type " + exp.getClass().getName() + ".");
    }
  }

  private void typecheckGuard(Expression guard, Environment env) throws Exception {
    this.typecheckExpression(guard, env);
    if(guard.getType().name != "boolean") {
      throw new Exception("Typechecking failed for guard : " + guard.toString() +
        " of type " + guard.getType().name + ". Should be boolean.");
    }
  }

  private void typecheckStatementList(
    StatementList sl,
    Environment renv,
    Environment wenv,
    Environment rwenv,
    Environment roenv,
    Environment woenv) throws Exception {
      List<Statement> statements = sl.getStatements();
      for(Statement st : statements) {
        this.typecheckStatement(st, renv, wenv, rwenv, roenv, woenv);
      }
  }

  private void typecheckAssignmentStatement (
      Statement s,
      Environment renv,
      Environment wenv,
      Environment rwenv,
      Environment roenv,
      Environment woenv) throws Exception {
      AssignmentStatement as = (AssignmentStatement)s;

    this.typecheckName(as.lhs, wenv);
    this.typecheckExpression(as.rhs, rwenv);

    if(!as.lhs.getType().equals(as.rhs.getType())) {
      throw new Exception("assignment lhs and rhs types don't match.");
    }
  }

  private void typecheckIfStatement (
      IfStatement is,
      Environment renv,
      Environment wenv,
      Environment rwenv,
      Environment roenv,
      Environment woenv) throws Exception {

    this.typecheckExpression(is.condition, renv);
    this.typecheckStatement(is.then_body, renv, wenv, rwenv, roenv, woenv);
    this.typecheckStatement(is.else_body, renv, wenv, rwenv, roenv, woenv);

    if(!is.condition.getType().name.equals("boolean")) {
      throw new Exception("Type checking failed in the if : condition '" + is.condition + "' didn't type check to a boolean.");
    }
  }

  private void typecheckWhileStatement (
      WhileStatement ws,
      Environment renv,
      Environment wenv,
      Environment rwenv,
      Environment roenv,
      Environment woenv) throws Exception {

    this.typecheckExpression(ws.condition, renv);
    this.typecheckStatement(ws.body, renv, wenv, rwenv, roenv, woenv);

    if(!ws.condition.getType().name.equals("boolean")) {
      throw new Exception("Type checking failed in the while loop : condition '" + ws.condition + "' didn't type check to a boolean.");
    }
  }

  private void typecheckExpressionStatement (
      ExpressionStatement es,
      Environment renv,
      Environment wenv,
      Environment rwenv,
      Environment roenv,
      Environment woenv) throws Exception {

    this.typecheckExpression(es.expression, renv);
  }

  private void typecheckStatement(
    Statement s,
    Environment renv,
    Environment wenv,
    Environment rwenv,
    Environment roenv,
    Environment woenv) throws Exception {
    if(s instanceof AssignmentStatement) {
      this.typecheckAssignmentStatement(s, renv, wenv, rwenv, roenv, woenv);
    }
    else if(s instanceof StatementList) {
      this.typecheckStatementList((StatementList)s, renv, wenv, rwenv, roenv, woenv);
    }
    else if(s instanceof IfStatement) {
      this.typecheckIfStatement((IfStatement)s, renv, wenv, rwenv, roenv, woenv);
    }
    else if(s instanceof WhileStatement) {
      this.typecheckWhileStatement((WhileStatement)s, renv, wenv, rwenv, roenv, woenv);
    }
    else if(s instanceof ExpressionStatement) {
      this.typecheckExpressionStatement((ExpressionStatement)s, renv, wenv, rwenv, roenv, woenv);
    }
    else {
      throw new Exception("statement '" + s + "' didn't typecheck: Unknown statement type.");

    }

  }

  private void typecheckTransition(Transition transition) throws Exception {
    State lub = this.statechart.lub(transition.getSource(), transition.getDestination());
    if(lub == null) {
      throw new Exception("Typechecking failed for transition : " + transition.name +
        ". Null LUB ");
    }
    if(transition.getState() != lub) {
      throw new Exception("Typechecking failed for transition : " + transition.name +
        ". Should be placed in state " + lub.name + " but placed in state " +
        transition.getState().name + ".");
    }
    this.typecheckGuard(transition.guard, transition.getRWEnvironment());
    this.typecheckStatement(transition.action,
      transition.getReadEnvironment(),
      transition.getWriteEnvironment(),
      transition.getRWEnvironment(),
      transition.getReadOnlyEnvironment(),
      transition.getWriteOnlyEnvironment());
  }

  private void checkNameDuplication(State state) throws Exception {
    Set<String> names = new HashSet<String>();
    for(Declaration d : state.declarations) {
      if(names.contains(d.vname)) {
        throw new Exception("Duplicate name '" + d.vname + "' in state " + state.name);
      }
      else {
        names.add(d.vname);
      }
    }
    for(State s : state.states) {
      if(names.contains(s.name)) {
        throw new Exception("Duplicate name '" + s.name + "' in state " + state.name);
      }
      else {
        names.add(s.name);
      }
    }
    for(Transition t : state.transitions) {
      if(names.contains(t.name)) {
        throw new Exception("Duplicate name '" + t.name + "' in state " + state.name);
      }
      else {
        names.add(t.name);
      }
    }
  }

  private void typecheckState(State state) throws Exception {
    checkNameDuplication(state);
    for(Transition t : state.transitions) {
      this.typecheckTransition(t);
    }

    for(State s : state.states) {
      this.typecheckState(s);
    }
  }

  private void typecheckState() throws Exception {
    this.typecheckState(this.statechart);
  }
}
