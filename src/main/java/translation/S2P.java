package translation;

import ast.*;
import program.Program;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class S2P {
	private Statechart _statechart;
	// Basic types set in the statechart
	Type _intType, _booleanType;
	// To be returned by translate()
	Program _program;

	// 'state' and 'event' variables
	Name _eventName, _stateName;

	public S2P () {}

	// Translate flattened statechart to Program
	public Program translate (Statechart statechart) {
		_program = new Program("", new DeclarationList(), new ArrayList<>(), new ArrayList<>(), new StatementList());
		this._statechart = statechart;
		this.setBasicTypes();
		this.addDeclarations();
		this.processEvents();
		this.processStates();
		this.processTransitions();

		return _program;
	}

	private void addDeclarations () {
		// Declarations of the statechart itself
		_program.declarations.addAll(_statechart.declarations);

		// init 'state' and 'event' variable declarations
		Declaration stateVarDecl = new Declaration("state", new TypeName("int"), false);
		Declaration eventVarDecl = new Declaration("event", new TypeName("int"), true); // 'event' is an input

		this._eventName = new Name("event");
		_eventName.setDeclaration(eventVarDecl);

		this._stateName = new Name("state");
		_stateName.setDeclaration(stateVarDecl);

		// Add them to the program decls
		_program.declarations.add(stateVarDecl);
		_program.declarations.add(eventVarDecl);
	}

	private void setBasicTypes() {
		this._intType = null;
		this._booleanType = null;

		for (Type type: this._statechart.types) {
			if (type.name.equals("int")) this._intType = type;
			else if (type.name.equals("boolean")) this._booleanType = type;

			if (this._intType != null && this._booleanType != null) break;
		}
	}

	private HashMap<String, Name> event_var_name_map;

	private void processEvents () {
		event_var_name_map = new HashMap<>();
		int rep = 0; // To represent individual events as integer values
		for (String event: _statechart.events) {
			Declaration decl = new Declaration(event, new TypeName("int"), false);

			Name lhs = new Name(event);
			lhs.setDeclaration(decl);
			event_var_name_map.put (event, lhs);

			Expression rhs = new IntegerConstant(rep);

			// Intialise variable with value
			Statement stmt = new AssignmentStatement(lhs, rhs);
			_program.statements.add(stmt);

			rep += 1;
		}
	}

	private HashMap<String, Name> state_var_name_map;

	private void processStates () {
		state_var_name_map = new HashMap<>();
		int rep = 0;
		for (State state: _statechart.states) {
			Declaration decl = new Declaration(state.name, new TypeName("int"), false);

			Name lhs = new Name (state.name);
			lhs.setDeclaration(decl);
			state_var_name_map.put (state.name, lhs);

			Expression rhs = new IntegerConstant(rep);

			// Initialize variable with value
			Statement stmt = new AssignmentStatement(lhs, rhs);
			_program.statements.add(stmt);

			rep += 1;
		}
	}

	private void processTransitions () {
		Statement outerIf = new SkipStatement();
		for (State state: _statechart.states) {
			Name stateName = this.state_var_name_map.get(state.name);
			Expression state_equal_expr = new BinaryExpression(this._stateName, stateName, "=");
			StatementList state_equals_then = new StatementList();
			List<Transition> transitions = this.getStateTransitionFrom(state);
 			for (Transition transition: transitions) {
				Name triggerName = this.event_var_name_map.get(transition.trigger);

				BinaryExpression condition = new BinaryExpression(this._eventName, triggerName, "=");

				Name destName = this.state_var_name_map.get(transition.getDestination().name);
				Statement state_change_stmt = new AssignmentStatement(this._stateName, destName);
				Statement then_stmt = state_change_stmt;

				Statement stmt = new IfStatement(condition, then_stmt, new SkipStatement());
				state_equals_then.add(stmt);
			}
			outerIf = new IfStatement(state_equal_expr, state_equals_then, outerIf);
		}
		StatementList while_body = new StatementList (new ExpressionStatement(this._eventName));
		while_body.add(outerIf);

		_program.statements.add (new WhileStatement(new BooleanConstant(true), while_body));
	}

	// TODO
	private void instrument() {}

	private List<Transition> getStateTransitionFrom (State state) {
		List<Transition> transitions = new ArrayList<>();
		for (Transition transition: this._statechart.transitions) {
			if (transition.getSource().getFullName().equals (state.getFullName())) {
				transitions.add (transition);
			}
		}
		return transitions;
	}
}
