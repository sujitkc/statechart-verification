package translation;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import ast.*;
import program.*;

public class StatechartToProgramTranslator {

  private final Statechart statechart;
  private final DeclarationList stateDeclarations = new DeclarationList();
  private final DeclarationList eventDeclarations = new DeclarationList();
  private Declaration stateVarDeclaration;
  private Declaration eventVarDeclaration;
  private Type intType = null; // to be made the type of all state and event variables.
  private Type boolType = null;

  public StatechartToProgramTranslator(Statechart statechart) {
    this.statechart = statechart;
    this.stateVarDeclaration = new Declaration("state", new TypeName("int"), false);
    this.eventVarDeclaration = new Declaration("event", new TypeName("int"), true);
    
    for(Type t : this.statechart.types) {
      if(t.name.equals("int")) {
        this.intType = t;
        break;
      }
    }
    if(this.intType != null) {
      this.stateVarDeclaration.setType(this.intType);
      this.eventVarDeclaration.setType(this.intType);
    }
    for(Type t : this.statechart.types) {
      if(t.name.equals("boolean")) {
        this.boolType = t;
        break;
      }
    }
  }

  public Program translate() {
    StatementList statements = new StatementList();

    this.statechart.declarations.add(this.stateVarDeclaration);
    this.statechart.declarations.add(this.eventVarDeclaration);
    int count = 0;
    for(State state : this.statechart.states) {
      Declaration stateDeclaration = new Declaration(state.name, new TypeName("int"), false);
      stateDeclaration.setType(this.intType);
      this.statechart.declarations.add(stateDeclaration);
      this.stateDeclarations.add(stateDeclaration);
      statements.add(new AssignmentStatement(new Name(state.name), new IntegerConstant(count)));
      count++;
    }
    for(String event : this.statechart.events) {
      Declaration eventDeclaration = new Declaration(event, new TypeName("int"), false);
      eventDeclaration.setType(this.intType);
      this.statechart.declarations.add(eventDeclaration);
      this.eventDeclarations.add(eventDeclaration);
      statements.add(new AssignmentStatement(new Name(event), new IntegerConstant(count)));
      count++;
    }
    Statement outerIf = new SkipStatement();
    for(State state : this.statechart.states) {
      Set<Transition> transitions = this.getTransitionsFromSourceState(state);
      Statement stmt = new SkipStatement();
      for(Transition t : transitions) {
        Name ev = new Name("event");
        ev.setDeclaration(this.eventVarDeclaration);
        Name evname = new Name(t.trigger);
        ev.setType(this.intType);
        evname.setType(this.intType);
        evname.setDeclaration(this.eventDeclarations.lookup(t.trigger));
        BinaryExpression eveq = new BinaryExpression(ev, evname, "=");
        BinaryExpression cond = new BinaryExpression(eveq, t.guard, "&&");
        Name sn1 = new Name("state");
        sn1.setType(this.intType);
        sn1.setDeclaration(this.stateVarDeclaration);
        Statement statechange = new AssignmentStatement(sn1, t.destinationName);
        StatementList innerstmtlst = new StatementList();
        innerstmtlst.add(t.action);
        innerstmtlst.add(statechange);
        stmt = new IfStatement(cond, Translator.flattenStatementList(innerstmtlst), stmt);
      }
      Name st = new Name("state");
      st.setType(this.intType);
      st.setDeclaration(this.stateVarDeclaration);
      Expression stname = new Name(state.name);
      stname.setType(this.intType);
      BinaryExpression steq = new BinaryExpression(st, stname, "=");
      outerIf = new IfStatement(steq, stmt, outerIf);
    }

    State initState = this.statechart.states.get(0);
    Name stinit = new Name("state");
    stinit.setType(this.intType);
    AssignmentStatement initialiseState = new AssignmentStatement(stinit, new Name(initState.name));
    statements.add(initialiseState);
    BooleanConstant cond = new BooleanConstant(true);
    cond.setType(this.boolType);
    Statement whileStatement = new WhileStatement(cond, outerIf);
    statements.add(whileStatement);
    statements.add(new HaltStatement());
    return new Program(
      this.statechart.name,
      this.statechart.declarations,
      this.statechart.types,
      this.statechart.functionDeclarations,
      statements); 
  }

  private Set<Transition> getTransitionsFromSourceState(State src) {
    Set<Transition> transitions = new HashSet<Transition>();
//    System.out.println("state name = " + src.getFullName());
    for(Transition t : this.statechart.transitions) {
//      System.out.println("source name = " + t.sourceName);
      if(t.sourceName.toString().equals(src.getFullName())) {
        transitions.add(t);
//        System.out.println("addint transition ...");
      }
    }
    return transitions;
  }
}
