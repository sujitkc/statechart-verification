package flatten;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import ast.*;
import analyse.Typechecker;

abstract class Translator {
  abstract Statechart translate(Statechart S) throws Exception;

  protected static Set<State> getAllAtomicDescendents(State state) {
    Set<State> atomicDescendents = new HashSet<State>();
    if(state.states.size() == 0) {
      atomicDescendents.add(state);
    }
    for(State s : state.states) {
      for(State ad : Translator.getAllAtomicDescendents(s)) {
        atomicDescendents.add(ad);
      }
    }
    return atomicDescendents;
  }

  protected static State getAtomicInitialDescendent(State state) {
    if(state.states.size() == 0) {
      return state;
    }
    return Translator.getAtomicInitialDescendent(state.states.get(0));
  }

  protected static String underscoreFullName(String name) {
    String underscoredName = "";

    for(char c : name.toCharArray()) {
      if(c == '.') {
        underscoredName += "_";
      }
      else {
        underscoredName += c;
      }
    }
    return underscoredName;
  }

}

public class Flattener extends Translator {
  private Translator globaliser                = new Globaliser();
  private Translator transitionAtomiser        = new TransitionAtomiser();

  public Statechart translate(Statechart S) throws Exception {
    return 
      this.transitionAtomiser.translate(
      this.globaliser.translate(S)
    );
  }
}

class Globaliser extends Translator {

  Map<Declaration, Declaration> globalDeclarations = new HashMap<Declaration, Declaration>();
  public Statechart translate(Statechart S) throws Exception {
    this.makeGlobalDeclarations(S);
    
    return this.globaliseStatechart(S);
  }

  private void makeGlobalDeclarations(State state) {
    for(Declaration dec : state.declarations) {
      String underscoredName = Translator.underscoreFullName(dec.getFullVName());
      while(this.globalDeclarations.get(underscoredName) != null) {
        underscoredName += "1";
      }
      Declaration newdec = new Declaration(
        underscoredName,
        dec.typeName,
        dec.input);
      this.globalDeclarations.put(dec, newdec);
    }
    for(State s : state.states) {
      this.makeGlobalDeclarations(s);
    }
  }

  private Statechart globaliseStatechart(Statechart statechart) throws Exception {
    DeclarationList declarations = new DeclarationList();
    for(Declaration declaration : this.globalDeclarations.keySet()) {
      declarations.add(this.globalDeclarations.get(declaration));
    }
    List<State> newStates = new ArrayList<State>();
    for(State s : statechart.states) {
      newStates.add(this.globaliseState(s));
    }
    List<Transition> newTransitions = new ArrayList<Transition>();
    for(Transition t : statechart.transitions) {
      newTransitions.add(this.globaliseTransition(t));
    }

    Statechart flattenedSC = new Statechart(
      statechart.name,
      statechart.types,
      statechart.events,
      declarations,
      this.globaliseStatement(statechart.entry),
      this.globaliseStatement(statechart.exit),
      statechart.functionDeclarations,
      newStates,
      newTransitions
    );
    (new Typechecker(flattenedSC)).typecheck();
    System.out.println("Printing analysed flattened Statechart ...");
    System.out.println(flattenedSC);
    System.out.println("Printing analysed flattened Statechart ... done!");

    return flattenedSC;
  }

  private State globaliseState(State state) throws Exception {
    List<State> newStates = new ArrayList<State>();
    for(State s : state.states) {
      newStates.add(this.globaliseState(s));
    }
    List<Transition> newTransitions = new ArrayList<Transition>();
    for(Transition t : state.transitions) {
      newTransitions.add(this.globaliseTransition(t));
    }
    return new State(
      state.name,
      new DeclarationList(),
      this.globaliseStatement(state.entry),
      this.globaliseStatement(state.exit),
      newStates,
      newTransitions
    );
  }

  private Transition globaliseTransition(Transition t) throws Exception {
    return new Transition(t.name,
      t.sourceName,
      t.destinationName,
      t.trigger,
      this.globaliseExpression(t.guard),
      this.globaliseStatement(t.action)
    );
  }

  private Statement globaliseStatement(Statement s) throws Exception {
    if(s instanceof AssignmentStatement) {
      return globaliseAssignmentStatement((AssignmentStatement)s);
    }
    else if(s instanceof StatementList) {
      return globaliseStatementList((StatementList)s);
    }
    else if(s instanceof ExpressionStatement) {
      return globaliseExpressionStatement((ExpressionStatement)s);
    }
    else if(s instanceof IfStatement) {
      return globaliseIfStatement((IfStatement)s);
    }
    else if(s instanceof WhileStatement) {
      return globaliseWhileStatement((WhileStatement)s);
    }
    else {
      throw new Exception("Globaliser.globaliseStatement : unknown error for " + s);
    }
  }

  private ExpressionStatement globaliseExpressionStatement(ExpressionStatement s) throws Exception {
    return new ExpressionStatement(this.globaliseExpression(s.expression));
  }

  private IfStatement globaliseIfStatement(IfStatement s) throws Exception {
    return new IfStatement(
      this.globaliseExpression(s.condition),
      this.globaliseStatement(s.then_body),
      this.globaliseStatement(s.else_body)
    );
  }

  private WhileStatement globaliseWhileStatement(WhileStatement s) throws Exception {
    return new WhileStatement(
      this.globaliseExpression(s.condition),
      this.globaliseStatement(s.body)
    );
  }

  private AssignmentStatement globaliseAssignmentStatement(AssignmentStatement s) throws Exception {
    Name oldName = s.lhs;
    Declaration oldDeclaration = oldName.getDeclaration();
    Declaration newDeclaration = globalDeclarations.get(oldDeclaration);
    if(newDeclaration == null) {
      throw new Exception("No global declaration for " + newDeclaration.vname + " found.");
    }
    Name newName = new Name(newDeclaration.vname);

    Expression oldExpression = s.rhs;
    Expression newExpression = this.globaliseExpression(oldExpression);

    return new AssignmentStatement(newName, newExpression);
  }

  private Expression globaliseExpression(Expression e) throws Exception {
    if(e instanceof BinaryExpression) {
      return this.globaliseBinaryExpression((BinaryExpression)e);
    }
    else if(e instanceof BooleanConstant) {
      return this.globaliseBooleanConstant((BooleanConstant)e);
    }
    else if(e instanceof FunctionCall) {
      return this.globaliseFunctionCall((FunctionCall)e);
    }
    else if(e instanceof IntegerConstant) {
      return this.globaliseIntegerConstant((IntegerConstant)e);
    }
    else if(e instanceof Name) {
      return this.globaliseName((Name)e);
    }
    else if(e instanceof StringLiteral) {
      return this.globaliseStringLiteral((StringLiteral)e);
    }
    else {
      throw new Exception("Globaliser.globaliseExpression : unknown error for " + e);
    }

  }
  private BinaryExpression globaliseBinaryExpression(BinaryExpression e) throws Exception {
    return new BinaryExpression(
      this.globaliseExpression(e.left),
      this.globaliseExpression(e.right),
      e.operator
    );
  }

  private BooleanConstant globaliseBooleanConstant(BooleanConstant e) throws Exception {
    return e;
  }

  private FunctionCall globaliseFunctionCall (FunctionCall e) throws Exception {
    List<Expression> newArgumentList = new ArrayList<Expression>();
    for(Expression arg : e.argumentList) {
      newArgumentList.add(this.globaliseExpression(arg));
    }
    return new FunctionCall(e.name, newArgumentList);
  }

  private IntegerConstant globaliseIntegerConstant(IntegerConstant e) throws Exception {
    return e;
  }

  private Name globaliseName(Name e) throws Exception {
    Declaration declaration = e.getDeclaration();
    String fullVName = declaration.getFullVName();
    Declaration globalisedName = this.globalDeclarations.get(declaration);
    return new Name(globalisedName.vname);
  }

  private StringLiteral globaliseStringLiteral(StringLiteral e) throws Exception {
    return e;
  }

  private UnaryExpression globaliseUnaryExpression(UnaryExpression e) throws Exception {
    return new UnaryExpression(
      this.globaliseExpression(e.expression),
      e.operator
    );
  }

  private StatementList globaliseStatementList(StatementList oldStatementList) throws Exception {
    StatementList newStatementList = new StatementList();

    for(Statement statement : oldStatementList.getStatements()) {
      newStatementList.add(globaliseStatement(statement));
    }
    return newStatementList;
  }
}

// Translator for transitions with non-atomic sources
class TransitionAtomiser extends Translator {
  public Statechart translate(Statechart S) throws Exception {
    return this.translateStatechart(S);
  }

  // returns a list of all ancestors of substate all the way upto and excluding
  // superstate. The highest level superstate is the first in the list.
  private List<State> getAllAncestorsExcluding(State substate, State superstate) {
    if(substate.getSuperstate() == superstate) {
      List<State> ancestors = new ArrayList<State>();
      ancestors.add(substate);
      return ancestors;
    }
    List<State> ancestors = this.getAllAncestorsExcluding(substate.getSuperstate(), superstate);
    ancestors.add(substate);
    return ancestors;
  }

  private List<State> getAllAncestorsIncluding(State substate, State superstate) {
    return this.getAllAncestorsExcluding(substate, superstate.getSuperstate());
  }

  private Statement getExitStatements(State source, State superstate) {
    List<State> ancestors = this.getAllAncestorsExcluding(source, superstate);
    StatementList exitStatements = new StatementList();
    ListIterator<State> li = ancestors.listIterator(ancestors.size());
    State s;
    while(li.hasPrevious()) {
      s = li.previous();
      exitStatements.add(s.exit);
    }
    return exitStatements;
  }

  private Statement getEntryStatements(State destination, State superstate) {
    List<State> ancestors = this.getAllAncestorsExcluding(destination, superstate);
    StatementList entryStatements = new StatementList();
    for(State ancestor : ancestors) {
      entryStatements.add(ancestor.entry);
    }
    return entryStatements;
  }

  private Name getFullStateName(State state) {
    List<State> sourceAncestors = this.getAllAncestorsExcluding(state, null);
    List<String> sourceNames = new ArrayList<String>();
    for(State sourceAncestor : sourceAncestors) {
      sourceNames.add(sourceAncestor.name);
    }
    return new Name(sourceNames);
  }

  private List<Transition> translateTransitions(State state) {
    List<Transition> newTransitions = new ArrayList<Transition>();
    for(Transition t : state.transitions) {
      Set<State> atomicSources = Translator.getAllAtomicDescendents(t.getSource());
      State atomicDestination = Translator.getAtomicInitialDescendent(t.getDestination());
      Statement entryStatements = this.getEntryStatements(atomicDestination, state);
      Name destinationName = this.getFullStateName(atomicDestination);
      for(State src : atomicSources) {
        Statement exitStatements = this.getExitStatements(src, state);
        StatementList action = new StatementList();
        action.add(exitStatements);
        action.add(t.action);
        action.add(entryStatements);


        newTransitions.add(
          new Transition(
            t.name + '_' + Translator.underscoreFullName(src.getFullName())
              + '_' + Translator.underscoreFullName(atomicDestination.getFullName()),
            this.getFullStateName(src),
            destinationName,
            t.trigger,
            t.guard,
            action
          )
        );
      }
    }
    return newTransitions;
  }

  private Statechart translateStatechart(Statechart statechart) throws Exception {
    List<State> newStates = new ArrayList<State>();
    for(State s : statechart.states) {
      newStates.add(this.translateState(s));
    }
    List<Transition> newTransitions = this.translateTransitions(statechart);
    Statechart newStatechart = new Statechart(
      statechart.name,
      statechart.types,
      statechart.events,
      statechart.declarations,
      new StatementList(),
      new StatementList(),
      statechart.functionDeclarations,
      newStates,
      newTransitions
    );
    (new Typechecker(newStatechart)).typecheck();
    System.out.println("Printing analysed XT1 Statechart ...");
    System.out.println(newStatechart);
    System.out.println("Printing analysed XT1 Statechart ... done!");

    return newStatechart;
  }

  private State translateState(State state) throws Exception {
    List<State> newStates = new ArrayList<State>();
    for(State s : state.states) {
      newStates.add(this.translateState(s));
    }
    List<Transition> newTransitions = this.translateTransitions(state);
    return new State(
      state.name,
      new DeclarationList(),
      new StatementList(),
      new StatementList(),
      newStates,
      newTransitions
    );
  }
}
