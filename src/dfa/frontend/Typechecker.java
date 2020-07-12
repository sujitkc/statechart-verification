package frontend;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import ast.*;

public class Typechecker {

  private final Statechart statechart;

  public Typechecker(Statechart statechart) {
    this.statechart = statechart;
  }

  private void typecheckVariableDeclarations(State state) throws Exception {
    List<String> noTypeParameterNames = new ArrayList<String>();
    for(Declaration dec : state.declarations) {
      this.typecheckDeclaration(dec, noTypeParameterNames,
        this.statechart.types.size());
    }
    for(State st : state.states) {
      this.typecheckVariableDeclarations(st);
    }
  }

  private void typecheckFunctionDeclarations(
      Statechart statechart) throws Exception {

    for(FunctionDeclaration fdec : statechart.functionDeclarations) {
      Type type = null;
      try {
        type = this.lookupType(fdec.returnTypeName, fdec.typeParameterNames,
          this.statechart.types.size());
      }
      catch(Exception e) {
        throw new Exception(
          "Function Declaration '" + fdec.name + 
          "' didn't typecheck : function return type '" +
          fdec.returnTypeName + "' doesn't exist.");
      }
      if(type == null) {
        throw new Exception("Function declaration " + fdec.name +
          " didn't type check : Return type " + fdec.returnTypeName +
          " not found.");
      }      
      fdec.setReturnType(type);

      DeclarationList args = fdec.getParameterList();
      for(Declaration dec : args) {
        try {
          this.typecheckDeclaration(dec, fdec.typeParameterNames,
            this.statechart.types.size());
        }
        catch(Exception e) {
          throw new Exception(
            "Argument type '" + dec.toString() + "' in function declaration '"
            + fdec.name + "' didn't typecheck : argument type '" +
            dec.typeName + "' doesn't exist.");
        }
      }       
    }
  }

  public void typecheck() throws Exception {

    this.typecheckTypeDeclarations();
    this.typecheckVariableDeclarations();
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

  /*
  This function tells if there is declared a type of name 
    'tname' before the i-th type declaration.
  This function is meant to be called only during the
    typechecking of Type declarations.
  */ 
  private Type getKnownType(TypeName tname, int i) {
    for(int j = 0; j < i; j++) {
      Type type = this.statechart.types.get(j);
      if(type.name.equals(tname.name) && type.typeParameterNames.size()
          == tname.typeArgumentNames.size()) {
        return type;
      }
    }
    return null;
  }

  /*
    Looks up in the statechart type table for the presence of a type to which
    typeName conforms, and returns a type corresponding to typeName.
  */
  public Type lookupType(
      TypeName typeName,
      List<String> typeParameterNames, int i) throws Exception {
    Type type = null;
    if(typeParameterNames.contains(typeName.name)) {
      if(typeName.typeArgumentNames.size() != 0) {
        throw new Exception("Type lookup failed: type name '" + typeName.name +
          "' matches a type parameter (hence, should be a type variable), " +
          "however it has type arguments."); 
      }
      type = new ast.TypeVariable(typeName.name,
        typeParameterNames.indexOf(typeName.name));
    }
    else {
      type = this.getKnownType(typeName, i).copy();
      if(type == null) {
        throw new Exception("lookupType failed : Type name " + typeName +
          " not found.");
      }
      List<Type> typelist = new ArrayList<Type>();
      for(TypeName targ : typeName.typeArgumentNames) {
        Type ty = null;
        if(typeParameterNames.contains(targ.name)) {
          ty = new ast.TypeVariable(
            targ.name, typeParameterNames.indexOf(targ.name));
        }
        else {
          ty = this.lookupType(targ, typeParameterNames, i);
          if(ty == null) {
            throw new Exception("lookupType failed : Type argument " + targ +
              " not found.");
          }
        }
        typelist.add(ty);
      }
      type.typeArguments = typelist;
    }
    typeName.type = type;
    return type;
  }

  // Overloading lookupType to work with non-polymorphic types
  // with no type parameters.
  public Type lookupType(TypeName typeName) throws Exception {
    return this.lookupType(typeName, new ArrayList<String>(),
      this.statechart.types.size());
  }

  private void typecheckDeclaration(
    Declaration d, List<String> typeParameterNames, int i) throws Exception {
    Type type = this.lookupType(
      d.typeName, typeParameterNames, this.statechart.types.size());
    if(type == null) {
      throw new Exception("Struct typecheck error : type '" + d.typeName +
        "' unknown.");
    }
    if(type.typeArguments.size() > 0) {
      d.setType(type.substantiate(type.typeArguments));
    }
    else {
      d.setType(type);
    }
  }

  private void typecheckStruct(Struct struct, int i) throws Exception {
    for(Declaration d : struct.declarations) {
      this.typecheckDeclaration(d, struct.typeParameterNames, i);
    }
  }
 
  private void typecheckVariableDeclarations() throws Exception {
    this.typecheckVariableDeclarations(this.statechart);
  }
 
  private void typecheckFunctionDeclarations() throws Exception {
    this.typecheckFunctionDeclarations(this.statechart);
  }

  private Declaration getDeclarationInState(
      ListIterator<String> nameIterator, State state) {
    if(nameIterator.hasNext()) {
      String singleName = nameIterator.next(); // name of state
      if(!nameIterator.hasNext()) {
        return null;
      }
      
      String nextName = nameIterator.next(); // iterator.peek - step 1
      nameIterator.previous();               // iterator.peek - step 2

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

  private Type getTypeOfName(
      ListIterator<String> nameIterator,
      Type type) throws Exception {
    if(nameIterator.hasNext()) {
      String singleName = nameIterator.next();
      if(nameIterator.hasNext()) {
        if(type instanceof Struct) {
          Struct structType = (Struct)type;
          String nextName = nameIterator.next(); // iterator.peek - step 1
          nameIterator.previous();               // iterator.peek - step 2
          Declaration d = structType.declarations.lookup(nextName);
          if(d != null) {
            return this.getTypeOfName(nameIterator, d.getType());
          }
          else {
            throw new Exception("no field " + nextName +
            " in the struct type.");
          }
        }
        else {
          throw new Exception("name has more fields but the current type "
          + singleName + " is not a struct.");
        }
      }
      else {
        return type;
      }
    }
    else { // should be unreachable, but needed by the compiler.
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
        System.out.println(env);
        throw new Exception("Name " + name + 
        " didn't type check. Couldn't locate a variable with matching" +
        " description.");
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

  private void typecheckBooleanConstant(BooleanConstant e) throws Exception {
    e.setType(this.lookupType(new TypeName("boolean")));
  }

  private void typecheckIntegerConstant(IntegerConstant e) throws Exception {
    e.setType(this.lookupType(new TypeName("int")));
  }

  private void typecheckStringLiteral(StringLiteral e) throws Exception {
    e.setType(this.lookupType(new TypeName("string")));
  }


  private void typecheckBinaryExpression(
      BinaryExpression b,
      Environment env) throws Exception {
    typecheckExpression(b.left, env);
    typecheckExpression(b.right, env);
    if(
        b.operator.equals("+") ||
        b.operator.equals("*") ||
        b.operator.equals("-") ||
        b.operator.equals("/") ) {

      Type type = this.lookupType(new TypeName("int"));
      if(b.left.getType().equals(type) &&
        b.right.getType().equals(type)) {
        b.setType(type);
      }
      else {
        throw new Exception(
          "typecheckBinaryExpression failed: operand type mismatch between "
          + b.left + " and " + b.right);
      }
    }
    else if(
        b.operator.equals(">=") ||
        b.operator.equals(">")  ||
        b.operator.equals("<=") ||
        b.operator.equals("<")  ||
        b.operator.equals("!=") ||
        b.operator.equals("=") ) {

      TypeName[] allowedTypes = { new TypeName("int"), new TypeName("string") };
      boolean type_set = false; 
      for(TypeName tname : allowedTypes) {
        Type type = this.lookupType(tname);
        if(b.left.getType().equals(type) &&
          b.right.getType().equals(type)) {
          b.setType(this.lookupType(new TypeName("boolean")));
          type_set = true;
          break;
        }
      }
      if(type_set == false) {
        throw new Exception(
          "typecheckBinaryExpression failed: operand type mismatch between "
          + b.left + " and " + b.right);
      }
    }
    else if(
        b.operator.equals("&&") ||
        b.operator.equals("||") ) {

      Type type = this.lookupType(new TypeName("boolean"));
      if(b.left.getType().equals(type) &&
        b.right.getType().equals(type)) {
        b.setType(type);
      }
      else {
        throw new Exception("typecheckBinaryExpression failed: operand type mismatch between " + b.left + " and " + b.right);
      }
    }
  }

  /*
  This function tells if there is declared a function of name 
    'fname'.
  This function is meant to be called only during the
    typechecking of Function calls.
  */ 
  private FunctionDeclaration getKnownFunction(FunctionName fname) throws Exception {
    for(int j = 0; j < this.statechart.functionDeclarations.size(); j++) {
      FunctionDeclaration function = this.statechart.functionDeclarations.get(j);
      if(function.name.equals(fname.name)) {
        if(function.typeParameterNames.size() == fname.typeArgumentNames.size()) {
          return function;
        }
        else {
          throw new Exception("Wrong number of type arguments");
        }
      }
    }
    throw new Exception("Function not found");
  }

  public FunctionDeclaration lookupFunctionName(FunctionName fName) throws Exception {
    FunctionDeclaration func = null;
    try {
      func = this.getKnownFunction(fName);
    }
    catch(Exception e) {
      throw new Exception("lookupFunctionName failed : " + fName +  " " + e.getMessage());
    }
    func = func.copy();
    List<Type> typelist = new ArrayList<Type>();
    for(TypeName targ : fName.typeArgumentNames) {
      Type ty = null;
      ty = this.lookupType(targ);
      if(ty == null) {
        throw new Exception("lookupType failed : Type argument " + targ + " not found.");
      }
      typelist.add(ty);
    }
    func.typeArguments = typelist;
    fName.type = func;
    return func;
  }

  private void typecheckFunctionCall(FunctionCall funcCall, Environment env) throws Exception {
    /*
      typecheck the function name.
      If there's no function declaration of that name: raise error.
      If argument list size (in function call) and parameter list
        size (in function declaration) are not equal, raise error.
      Else:
        for each argument expression arg:
          type check arg.
          If the type of arg doesn't match the type of the
            corresponding formal parameter, then raise error.
          Else OK.
    */

    FunctionDeclaration fdec =
      this.lookupFunctionName(funcCall.name);
    if(fdec == null) {
      throw new Exception(
        "function call didn't typecheck: function by name " +
        funcCall.name + " not found.");
    }
    DeclarationList parameterList = fdec.getParameterList();
    if(parameterList.size() != funcCall.argumentList.size()) {
      throw new Exception("function call " + funcCall.name +
        " didn't typecheck: incorrect number of arguments. " +
        parameterList.size() + " expected; " +
        funcCall.argumentList.size() + " provided.");
    }

    Type returnType = null;
    if(fdec.typeArguments.size() > 0) {
      funcCall.function = fdec.substantiate(fdec.typeArguments);
      returnType = fdec.getReturnType().substantiate(fdec.typeArguments);
    }
    else {
      funcCall.function = fdec;
      returnType = fdec.getReturnType();
    }
    for(int i = 0; i < funcCall.argumentList.size(); i++) {
      Expression arg = funcCall.argumentList.get(i);
      this.typecheckExpression(arg, env);
      Type targ = arg.getType();
      Type paramtype = funcCall.function.getParameterList().get(i).type.substantiate(fdec.typeArguments);
      if(paramtype.equals(targ) == false) {
        throw new Exception(
          "function call didn't typecheck: arguments " +
          arg + " has type " + arg.getType() + ". " +
          paramtype.name + " expected.");
      }
    }
    funcCall.setType(returnType);
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
    else if(exp instanceof StringLiteral) {
      this.typecheckStringLiteral((StringLiteral)exp); 
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

  private void typecheckTrigger(String trigger) throws Exception {
    if(this.statechart.events.contains(trigger) == false) {
      throw new Exception("Typechecking failed for trigger : " + trigger +
        " not found.");
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
    this.typecheckExpression(as.rhs, renv);
    
    if(!as.lhs.getType().equals(as.rhs.getType())) {
      throw new Exception("In assignment " + as + " lhs: " + as.lhs.getType() +
        " and rhs type " + as.rhs.getType() + " don't match.");
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
    this.typecheckTrigger(transition.trigger);
    this.typecheckGuard(transition.guard, transition.getReadEnvironment());
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

    Environment env = state.getEnvironment();
    Environment emptyEnv = Environment.emptyEnvironment;
    typecheckStatement(state.entry, env, env, env, emptyEnv, emptyEnv);
    typecheckStatement(state.exit, env, env, env, emptyEnv, emptyEnv);

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
