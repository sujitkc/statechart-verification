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
    String lastName = name.name.get(name.name.size() - 1);
    Declaration dec = env.lookup(lastName);
    if(dec == null) {
      throw new Exception("Undefined variable " + name.toString() + " looked in : " + env);
    }
    else {
      name.setDeclaration(dec);
      name.setType(dec.getType());
    }
  }

  private void typecheckExpression(Expression exp, Environment env) throws Exception {
    if(exp instanceof Name) {
      typecheckName((Name)exp, env); 
    }
    throw new Exception("Typechecking failed for expression: " + exp.toString());
  }

  private void typecheckGuard(Expression guard, Environment env) throws Exception {
    this.typecheckExpression(guard, env);
  }

  private void typecheckAction(Statement action, Environment renv, Environment env, Environment wrenv) {

  }

  private void typecheckTransition(Transition transition) throws Exception {
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
  }

  private void typecheckCode() throws Exception {
    this.typecheckCode(this.statechart);
  }
}
