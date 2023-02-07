package translation;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import ast.*;
import program.*;
import utilities.UniqueVarNameGenerator;

public class StatechartToProgramTranslator {
  protected StatementList flattenStatementList (StatementList list) {
    StatementList result = new StatementList();
    for (Statement stmt: list.getStatements()) {
      if (stmt instanceof StatementList) {
        StatementList stmt_list = flattenStatementList ((StatementList)stmt);
        for (Statement smt: stmt_list.getStatements()) {
          result.add (smt);
        }
      } else {
        result.add (stmt);
      }
    }
    return result;
  }

  private final Statechart statechart;
  private final DeclarationList stateDeclarations = new DeclarationList();
  private final DeclarationList eventDeclarations = new DeclarationList();
  private Declaration stateVarDeclaration;
  private Declaration eventVarDeclaration;
  private Type intType = null; // to be made the type of all state and event variables.
  private Type boolType = null;

  private UniqueVarNameGenerator varNameGenerator = new UniqueVarNameGenerator ();

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

  HashMap<String, String> stateNameMap = new HashMap<>();
  HashMap<String, String> eventNameMap = new HashMap<>();

  public Program translate() {
    StatementList statements = new StatementList();

    this.statechart.declarations.add(this.stateVarDeclaration);
    this.statechart.declarations.add(this.eventVarDeclaration);
    int count = 0;
    for(State state : this.statechart.states) {
      String newStateName = this.varNameGenerator.generate();
      stateNameMap.put (state.getFullName(), newStateName);
      Declaration stateDeclaration = new Declaration(newStateName, new TypeName("int"), false);
      stateDeclaration.setType(this.intType);
      this.statechart.declarations.add(stateDeclaration);
      this.stateDeclarations.add(stateDeclaration);
      statements.add(new AssignmentStatement(new Name(newStateName), new IntegerConstant(count)));
      count++;
    }
    count = 0;
    for(String eventName : this.statechart.events) {
      String event = this.varNameGenerator.generate();
      eventNameMap.put(eventName, event);
      Declaration eventDeclaration = new Declaration(event, new TypeName("int"), false);
      eventDeclaration.setType(this.intType);
      this.statechart.declarations.add(eventDeclaration);
      this.eventDeclarations.add(eventDeclaration);
      statements.add(new AssignmentStatement(new Name(event), new IntegerConstant(count)));
      count++;
    }

  FunctionName assert_name = new FunctionName("stuck_spec");
  FunctionName assert_nd_name = new FunctionName("non_det");

    Statement outerIf = new SkipStatement();
    for(State state : this.statechart.states) {
      Set<Transition> transitions = this.getTransitionsFromSourceState(state);

    Expression guards_or = null;
    for (Transition t: transitions) {
    if (guards_or == null) {
      guards_or = t.guard;
    } else {
      guards_or = new BinaryExpression (guards_or, t.guard, "||");
    }
    }

    HashMap<String, List<Transition>> transitions_for_event = new HashMap<>();
    for (Transition t: transitions) {
      if (transitions_for_event.containsKey(t.trigger)) {
        transitions_for_event.get(t.trigger).add(t);
      } else {
        ArrayList<Transition> list = new ArrayList<>();
        list.add (t);
        transitions_for_event.put(t.trigger, list);
      }
    }

    Statement under_state_eq = new SkipStatement();
    for (String trigger: transitions_for_event.keySet()) {
      List<Transition> edges = transitions_for_event.get(trigger);
      Name event_var_name = new Name ("event");
      event_var_name.setDeclaration(this.eventVarDeclaration);

      Name event_ins_name = new Name (eventNameMap.get(trigger));
      event_ins_name.setType (this.intType);
      event_ins_name.setDeclaration(this.eventDeclarations.lookup(trigger));

      BinaryExpression eveq = new BinaryExpression(event_var_name, event_ins_name, "=");

      Statement under_event_eq = new SkipStatement();
      /* Non-Determinism begin*/
      // All guards for this event (trigger)
      ArrayList<Expression> all_guards = new ArrayList<>();
      for (Transition t: edges) {
        all_guards.add(t.guard);
      }
      // Generate the pairs
      ArrayList<Expression> pairwise_ands = new ArrayList<>();
      int size = all_guards.size();
      for (int i = 0; i < size; i++) {
        for (int j = i+1; j < size; j++) {
          pairwise_ands.add(new BinaryExpression(all_guards.get(i), all_guards.get(j), "&&"));
        }
      }
      Expression or_of_ands = null;
      for (Expression and: pairwise_ands) {
        if (or_of_ands == null) {
          or_of_ands = and;
        } else {
          or_of_ands = new BinaryExpression (or_of_ands, and, "||");
        }
      }
      /* Non-Determinism end */
      for (Transition t: edges) {
        Name state_var_name = new Name ("state");
        state_var_name.setType(this.intType);
        state_var_name.setDeclaration(this.stateVarDeclaration);

        Name destName = new Name (stateNameMap.get (t.getDestination().getFullName()));
        Statement statechange_stmt = new AssignmentStatement(state_var_name, destName);
        StatementList stmt_list = new StatementList();
        stmt_list.add(t.action);
        stmt_list.add(statechange_stmt);


        under_event_eq = new IfStatement(t.guard, stmt_list, under_event_eq);
      }
      StatementList stmt = new StatementList();
      if (or_of_ands != null) {
        ArrayList<Expression> args = new ArrayList<>();
        args.add (or_of_ands);
        stmt.add (new ExpressionStatement(new FunctionCall(assert_nd_name, args)));
      }
      stmt.add(under_event_eq);
      under_state_eq = new IfStatement(eveq, stmt, under_state_eq);
    }

      Name st = new Name("state");
      st.setType(this.intType);
      st.setDeclaration(this.stateVarDeclaration);
      Expression stname = new Name(stateNameMap.get(state.getFullName()));
      stname.setType(this.intType);
      BinaryExpression steq = new BinaryExpression(st, stname, "=");


    if (guards_or != null) {
      StatementList stmt_list = new StatementList ();
      List<Expression> args = new ArrayList<>();
      args.add (guards_or);
      FunctionCall call = new FunctionCall(assert_name, args);
      ExpressionStatement call_stmt = new ExpressionStatement(call);
      stmt_list.add (call_stmt);
      stmt_list.add (under_state_eq);
      under_state_eq = flattenStatementList(stmt_list);
    }

    outerIf = new IfStatement(steq, under_state_eq, outerIf);
    }

    State initState = this.statechart.states.get(0);
    // TODO: change 'state'
    Name stinit = new Name("state");
    stinit.setType(this.intType);
    String initStateName = stateNameMap.get (initState.getFullName());
    assert (initStateName != null);
    AssignmentStatement initialiseState = new AssignmentStatement(stinit, new Name(initStateName));
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
