package translation;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import ast.*;
import frontend.*;

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

  protected static Set<Transition> getAllTransitions(State state) {
    Set<Transition> alltransitions = new HashSet<Transition>();

    for(Transition t : state.transitions) {
      alltransitions.add(t);
    }
    for(State s : state.states) {
      Set<Transition> transitions = getAllTransitions(s);
      for(Transition t : transitions) {
        alltransitions.add(t);
      }
    }
    return alltransitions;
  }

  protected static String underscoreFullName(String name) {
    return name.replace ('.', '-');
  }

  // returns a list of all ancestors of substate all the way upto and excluding
  // superstate. The highest level superstate is the first in the list.
  protected static List<State> getAllAncestorsExcluding(State substate, State superstate) {
    if(substate.getSuperstate() == superstate) {
      List<State> ancestors = new ArrayList<State>();
      ancestors.add(substate);
      return ancestors;
    }
    List<State> ancestors = Translator.getAllAncestorsExcluding(substate.getSuperstate(), superstate);
    ancestors.add(substate);
    return ancestors;
  }

  protected static List<State> getAllAncestorsIncluding(State substate, State superstate) {
    return Translator.getAllAncestorsExcluding(substate, superstate.getSuperstate());
  }

  protected static Name getFullStateName(State state) {
    List<State> sourceAncestors = Translator.getAllAncestorsExcluding(state, null);
    List<String> sourceNames = new ArrayList<String>();
    for(State sourceAncestor : sourceAncestors) {
      sourceNames.add(sourceAncestor.name);
    }
    return new Name(sourceNames);
  }

  protected static StatementList flattenStatementList(StatementList list) {
    StatementList newlist = new StatementList();
    for(Statement statement : list.getStatements()) {
      if(statement instanceof StatementList) {
        StatementList slist = flattenStatementList((StatementList)statement);
        for(Statement s : slist.getStatements()) {
          newlist.add(s);
        }
      }
      else { // atomic statement
        newlist.add(statement);
      }
    }
    return newlist;
  }

  static String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  static int length = validCharacters.length();
  protected static String generateRandomString () {
    int len = 6;
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < len; i++) {
      builder.append(validCharacters.charAt((int)(Math.random()*length)));
    }
    return builder.toString();
  }
}

public class Flattener extends Translator {
  private Translator globaliser             = new Globaliser();
  private Translator transitionAtomiser     = new TransitionAtomiser();
  private Translator hierarchyPurger        = new HierarchyPurger();

  public Statechart translate(Statechart S) throws Exception {
    return 
      this.hierarchyPurger.translate(
      this.transitionAtomiser.translate(
      this.globaliser.translate(S)
    ));
  }
}

class Globaliser extends Translator {

  Map<Declaration, Declaration> globalDeclarations = new HashMap<Declaration, Declaration>();
  Set<String> globalVariableNames = new HashSet<>();
  public Statechart translate(Statechart S) throws Exception {
    this.makeGlobalDeclarations(S);
    
    return this.globaliseStatechart(S);
  }

  public String generateUniqueVarname () {
    String varName = Translator.generateRandomString();
    while (this.globalVariableNames.contains (varName)) {
      varName = Translator.generateRandomString();
    }
    return varName;
  }

  private void makeGlobalDeclarations(State state) {
    for(Declaration dec : state.declarations) {
      // generate new variable name for each declaration
      String newName = this.generateUniqueVarname();
      Declaration newdec = new Declaration(
        newName,
        dec.typeName,
        dec.input);
      this.globalDeclarations.put(dec, newdec);
    }
    for(State s : state.states) {
      this.makeGlobalDeclarations(s);
    }
  }

  private Statechart globaliseStatechart(Statechart statechart) throws Exception {
    DeclarationList declarations  = new DeclarationList (this.globalDeclarations.values());
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
      newTransitions,
      new BooleanConstant(false)
    );
    (new Typechecker(flattenedSC)).typecheck();

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
      newTransitions,
      state.history
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
      throw new Exception("No global declaration for " + oldName + " found.");
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
    else if (e instanceof UnaryExpression) {
      return this.globaliseUnaryExpression ((UnaryExpression)e);
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
    // String fullVName = declaration.getFullVName();
    Declaration globalisedName = this.globalDeclarations.get(declaration);
    assert (globalisedName != null);
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
// This translator converts all transitions such that they emerge and end at atomic states.
class TransitionAtomiser extends Translator {
  public Statechart translate(Statechart S) throws Exception {
    return this.translateStatechart(S);
  }


  private Statement getExitStatements(State source, State superstate) {
    List<State> ancestors = Translator.getAllAncestorsExcluding(source, superstate);
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
    List<State> ancestors = Translator.getAllAncestorsExcluding(destination, superstate);
    StatementList entryStatements = new StatementList();
    for(State ancestor : ancestors) {
      entryStatements.add(ancestor.entry);
    }
    return entryStatements;
  }

  private List<Transition> translateTransitions(State state) {
    List<Transition> newTransitions = new ArrayList<Transition>();
    for(Transition t : state.transitions) {
      Set<State> atomicSources = Translator.getAllAtomicDescendents(t.getSource());
      State atomicDestination = Translator.getAtomicInitialDescendent(t.getDestination());
      Statement entryStatements = this.getEntryStatements(atomicDestination, state);
      Name destinationName = Translator.getFullStateName(atomicDestination);
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
            Translator.getFullStateName(src),
            destinationName,
            t.trigger,
            t.guard,
      // TODO:
            flattenStatementList(action)
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
      newTransitions,
      statechart.history
    );
    (new Typechecker(newStatechart)).typecheck();

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
      newTransitions,
      state.history
    );
  }
}

class HierarchyPurger extends Translator {
  public Statechart translate(Statechart S) throws Exception {
    return translateStatechart(S);
  }

  // Basic idea is to just get rid of the non-atomic states.
  // Retain all atomic states.
  // Create transitions.
  private Statechart translateStatechart(Statechart statechart) throws Exception {
    Set<State> atomicStates = Translator.getAllAtomicDescendents(statechart);
    State istate = getAtomicInitialDescendent(statechart);
    atomicStates.remove(istate);
    List<State> newStates = new ArrayList<State>();
    newStates.add(
      new State(
        Translator.underscoreFullName(istate.getFullName()),
        new DeclarationList(),
        istate.entry,
        istate.exit,
        new ArrayList<State>(),
        new ArrayList<Transition>(),
        istate.history
      )
    );
    for(State s : atomicStates) {
      String stateName = Translator.underscoreFullName((s.getFullName()));
      newStates.add(
        new State(
          stateName,
          new DeclarationList(),
          s.entry,
          s.exit,
          new ArrayList<State>(),
          new ArrayList<Transition>(),
          s.history
        )
      );
    }

    List<Transition> newTransitions = new ArrayList<Transition>();
    Set<Transition> oldTransitions = Translator.getAllTransitions(statechart);
    for(Transition t : oldTransitions) {
        Name sourceName = new Name(statechart.name);
        sourceName.add(Translator.underscoreFullName(Translator.getFullStateName(t.getSource()).toString()));
        Name destinationName = new Name(statechart.name);
        destinationName.add(Translator.underscoreFullName(Translator.getFullStateName(t.getDestination()).toString()));
        newTransitions.add(
          new Transition(
            t.name,
            sourceName,
            destinationName,
            t.trigger,
            t.guard,
            t.action
          )
        );
    }
    Statechart newStatechart = new Statechart(
      statechart.name,
      statechart.types,
      statechart.events,
      statechart.declarations,
      new StatementList(),
      new StatementList(),
      statechart.functionDeclarations,
      newStates,
      newTransitions,
      statechart.history
    );
    (new Typechecker(newStatechart)).typecheck();
    return newStatechart;
  }
}
