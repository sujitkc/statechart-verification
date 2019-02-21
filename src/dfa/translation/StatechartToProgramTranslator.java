package translation;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import ast.*;
import program.*;

public class StatechartToProgramTranslator {

  private Statechart statechart;

  public StatechartToProgramTranslator(Statechart statechart) {
    this.statechart = statechart;
  }

  public Program translate() {
    StatementList statements = new StatementList();

/*
    for(Transition t : this.statechart.transitions) {
      System.out.println(t.name + " : " + t.sourceName);
    }
*/
    Statement outerIf = new SkipStatement();
    for(State state : this.statechart.states) {
      Set<Transition> transitions = this.getTransitionsFromSourceState(state);
      Statement stmt = new SkipStatement();
      for(Transition t : transitions) {
        Expression ev = new Name("event");
        Expression evname = new Name(t.trigger);
        BinaryExpression eveq = new BinaryExpression(ev, evname, "=");
        BinaryExpression cond = new BinaryExpression(eveq, t.guard, "&&");
        stmt = new IfStatement(cond, t.action, stmt);
      }
      Expression st = new Name("state");
      Expression stname = new Name(state.name);
      BinaryExpression steq = new BinaryExpression(st, stname, "=");
      outerIf = new IfStatement(steq, stmt, outerIf);
    }
    Statement whileStatement = new WhileStatement(new BooleanConstant(true), outerIf);
    statements.add(whileStatement);
    statements.add(new HaltStatement());
    return new Program(
      this.statechart.name,
      this.statechart.declarations,
      this.statechart.types,
      this.statechart.events,
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
