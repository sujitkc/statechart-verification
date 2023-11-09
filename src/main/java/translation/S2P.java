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
		stateVarDecl.setType (this._intType);
		eventVarDecl.setType (this._intType);

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
			decl.setType(this._intType);
			_program.declarations.add(decl);

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
			decl.setType (this._intType);
			_program.declarations.add(decl);

			Name lhs = new Name (state.name);
			lhs.setDeclaration(decl);
			lhs.setType (this._intType);
			state_var_name_map.put (state.name, lhs);

			Expression rhs = new IntegerConstant(rep);

			// Initialize variable with value
			Statement stmt = new AssignmentStatement(lhs, rhs);
			_program.statements.add(stmt);
			_program.statements.add(state.entry);
			rep += 1;
		}

		// Set init state
		String init_state_name = this._statechart.states.get(0).name;
		Statement init_state_assignment = new AssignmentStatement(this._stateName, state_var_name_map.get(init_state_name));
		_program.statements.add (init_state_assignment);
	}

	private void processTransitions () {
		FunctionName stuck_spec_name = new FunctionName("stuck_spec");
		FunctionName non_det_name = new FunctionName("non_det");

		Statement outerIf = new SkipStatement();
		for (State state: _statechart.states) {
			Name stateName = this.state_var_name_map.get(state.name);
			Expression state_equal_expr = new BinaryExpression(this._stateName, stateName, "="); // state=s1
			StatementList state_equals_then = new StatementList();
			List<Transition> transitions = this.getStateTransitionFrom(state);

			List<Expression> guard_list = new ArrayList<> (); // g1, g2, ...
			for (Transition transition: transitions) {
				guard_list.add (transition.guard);
			}
			// Instrumentation
			// stuck specification
			if (guard_list.size() > 0) {
				// !((g1||g2||g3||false))
				Expression ors = new BooleanConstant(false);
				for (Expression guard: guard_list) {
					ors = new BinaryExpression(ors, guard, "||");
				}

				Expression not_of_ors = new UnaryExpression(ors, UnaryExpression.Operator.NOT);
				ArrayList<Expression> args = new ArrayList<>();
				args.add(not_of_ors);

				FunctionCall stuck_spec_call = new FunctionCall(stuck_spec_name, args);
				ExpressionStatement call_stmt = new ExpressionStatement(stuck_spec_call);
				state_equals_then.add(call_stmt);
			} else {
				// Intended to be stuck
			}

			// Non Determinism
			if (guard_list.size () > 1) { // With a single/no transition, Non Determinism is not possible
		Expression expr = new BooleanConstant (false);
		for (Expression guard: guard_list) {
			expr = new BinaryExpression(expr, guard, "+");
		}

		expr = new BinaryExpression (expr, new IntegerConstant(1), ">");

		ArrayList<Expression> args = new ArrayList<>();
		args.add(expr);

				FunctionCall non_det_call = new FunctionCall(non_det_name, args);
				ExpressionStatement call_stmt = new ExpressionStatement(non_det_call);
				state_equals_then.add(call_stmt);
			}

			// TODO: Undefined Variable Access

 			for (Transition transition: transitions) {
				Name triggerName = this.event_var_name_map.get(transition.trigger);

				BinaryExpression event_equal_condition = new BinaryExpression(this._eventName, triggerName, "="); // event=e1
				Expression condition = new BinaryExpression (event_equal_condition, transition.guard, "&&"); // (event=e1) && (t.guard)

				Name destName = this.state_var_name_map.get(transition.getDestination().name);
				Statement state_change_stmt = new AssignmentStatement(this._stateName, destName); // state=s2
				StatementList then_stmt = new StatementList();
				then_stmt.add(state_change_stmt);
				then_stmt.add(transition.action); // Add action block

				Statement stmt = new IfStatement(condition, then_stmt, new SkipStatement()); // if (event=e1) {state=s2} else {}
				state_equals_then.add(stmt);
			}

			outerIf = new IfStatement(state_equal_expr, state_equals_then, outerIf); // if(state=s1) {if(event=e1 && g1) {state=s2} else {} if(event=e2 && g2) {state=s3} else {}}
		}

		FunctionName input_func_name = new FunctionName("input");
		FunctionCall input_func_call = new FunctionCall(input_func_name, new ArrayList<>());
		// event := input()
		AssignmentStatement stmt = new AssignmentStatement(this._eventName, input_func_call);


		
		
		StatementList while_body = new StatementList ();
		while_body.add (stmt);
		
		//Declarations with input to be added properly
		for(int i=0;i<_program.declarations.size();i++){
			if(_program.declarations.get(i).input){
				Name x=new Name(_program.declarations.get(i).vname);
				x.setDeclaration(_program.declarations.get(i));
				AssignmentStatement stmt1 = new AssignmentStatement(x, input_func_call);
				while_body.add (stmt1);
			}
		}
		while_body.add(outerIf);

		_program.statements.add (new WhileStatement(new BooleanConstant(true), while_body));
	}

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
