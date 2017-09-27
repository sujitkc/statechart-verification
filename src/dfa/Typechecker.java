import ast.Statechart;
import ast.State;
import ast.Transition;
import ast.Declaration;
import ast.Expression;
import ast.Type;
import ast.Environment;
import ast.Statement;

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

  private void typecheckGuard(Expression guard, Environment env) {

  }

  private void typecheckAction(Statement action, Environment renv, Environment env) {

  }

  private void typecheckTransition(Transition transition) {
    this.typecheckGuard(transition.guard, transition.getRWEnvironment());
    this.typecheckAction(transition.action,
      transition.getReadEnvironment(),
      transition.getRWEnvironment());
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
