import java.util.List;
import java.util.ArrayList;

import ast.Statechart;
import ast.State;
import ast.Transition;
import ast.Declaration;
import ast.Expression;
import ast.Type;
import ast.Environment;
import ast.Statement;
import ast.Name;

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

  public void typecheck() throws Exception {
    this.typecheckDeclarations();
    this.typecheckCode();
  }

  private void typecheckDeclarations() throws Exception {
    this.typecheckDeclarations(this.statechart);
  }

  private void typecheckName(Name name, Environment env) throws Exception {
    Declaration dec = null;
    if(name.name.size() > 1) {
      String lastName = name.name.get(name.name.size() - 1);
      List<String> stateName = new ArrayList<String>(name.name.subList(0, name.name.size() - 1));
      State state = this.statechart.nameToState(new Name(stateName));
      if(state != null) {
        dec = state.declarations.lookup(lastName);
      }
      else {
        throw new Exception("Undefined variable " + name.toString() + "; no state by name " + stateName + " found.");
      }
    }
    else if(name.name.size() == 1) {
      dec = env.lookup(name.name.get(0));
    }
    else {
      throw new Exception("Empty variable name.");
    }

    if(dec == null) {
      throw new Exception("Undefined variable " + name.toString() + " looked in : " + env);
    }
    else {
      name.setDeclaration(dec);
      name.setType(dec.getType());
    }
  }

  private void typecheckExpression(Expression exp, Environment env) throws Exception {
    if(exp instanceof ast.Name) {
      typecheckName((Name)exp, env); 
    }
    else {
      throw new Exception("Typechecking failed for expression: " + exp.toString() +
        " of type " + exp.getClass().getName() + ".");
    }
  }

  private void typecheckGuard(Expression guard, Environment env) throws Exception {
    System.out.println("Typing checking guard " + guard.toString() + " in env = " + env.toString() + "\n");
    this.typecheckExpression(guard, env);
    if(guard.getType().name != "boolean") {
      throw new Exception("Typechecking failed for guard : " + guard.toString() +
        " of type " + guard.getType().name + ". Should be boolean.");
    }
  }

  private void typecheckAction(
    Statement action,
    Environment renv,
    Environment env,
    Environment wrenv) {

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
    this.typecheckAction(transition.action,
      transition.getReadOnlyEnvironment(),
      transition.getRWEnvironment(),
      transition.getWriteOnlyEnvironment());
  }

  private void typecheckCode(State state) throws Exception {
    for(Transition t : state.transitions) {
      this.typecheckTransition(t);
    }

    for(State s : state.states) {
      this.typecheckCode(s);
    }
  }

  private void typecheckCode() throws Exception {
    this.typecheckCode(this.statechart);
  }
}
