package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class Statechart extends State {

  public final List<Type> types = new ArrayList<Type>();

  public Statechart(
      String            name,
      List<State>       states,
      List<Transition>  transitions,
      List<Declaration> declarations) {
    super(name, states, transitions, declarations);

    this.statechart = null;
    for(State s : this.states) {
      s.setStatechart(this);
    }

    for(Transition t : this.transitions) {
      t.setStatechart(this);
    }

    this.types.add(new BasicType("int"));
    this.types.add(new BasicType("boolean"));
  }

  public State lub(State s1, State s2) {
    List<State> a1 = s1.getAllSuperstates();
    List<State> a2 = s2.getAllSuperstates();

    State cca = null;
    while(a1.size() != 0 && a2.size() == 0) {
      if(a1.get(0) == a2.get(0)) {
        cca = a1.get(0);
        a1.remove(0);
        a2.remove(0);
      }
      else {
        break;
      }
    }
    return cca;
  }

  public Type lookupType(String typeName) {
    for(Type type : this.types) {
      if(type.name.equals(typeName)) {
        return type;
      }
    }
    return null;
  }
  /* 
    Takes a fully-qualified name and returns a State which has this
    name. Returns null if such a State is not found.
  */
  public State nameToState(Name name) {
    State state = null;
    List<State> nextStates = new ArrayList<State>();
    nextStates.add(this);
    ListIterator<String> it = name.name.listIterator();
    while(it.hasNext()) {
      String id = it.next();
      State nextState = null;
      String n = it.next();
      for(State st : nextStates) {
        if(n == st.name) {
          nextState = st;  
        }
      }
      if(nextState == null) {
        return null;
      }
      state = nextState;
      nextStates = state.states;
    }
    return state;
  }

  public String toString() {
    String s = "startchart " + this.name + " {";

    if(this.declarations != null) {
      for(Declaration d : this.declarations) {
        s += d.toString() + "\n";
      }
    }
 
    for(State st : this.states) {
      s += st.toString();
    }

    for(Transition tr : this.transitions) {
      s += tr.toString();
    }
    s += "}";
    return s;
  }
}
