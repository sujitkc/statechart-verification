package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class Statechart extends State {

  public final List<Type> types = new ArrayList<Type>();
  public final List<String> events;
  public final List<FunctionDeclaration> functionDeclarations;

  public Statechart(
      String                    name,
      List<Type>                moreTypes,
      List<String>              events,
      DeclarationList           declarations,
      Statement                 entry,
      Statement                 exit,
      List<FunctionDeclaration> functionDeclarations,
      List<State>               states,
      List<Transition>          transitions
      ) throws Exception {
    super(name, declarations, entry, exit, states, transitions);

    this.addType(new BasicType("int"));
    this.addType(new BasicType("boolean"));
    this.addType(new BasicType("string"));

    for(Type t : moreTypes) {
      this.addType(t);
    }

    this.events = events;
    this.functionDeclarations = functionDeclarations;

    this.statechart = null;
    for(State s : this.states) {
      s.setStatechart(this);
    }

    for(Transition t : this.transitions) {
      t.setStatechart(this);
    }

    this.initialiseTransitions();
  }

  private void addType(Type t) {
    if((this.types.contains(t)) == false) {
      this.types.add(t);
    }
  }

  public State lub(State s1, State s2) {
    List<State> a1 = s1.getAllSuperstates();
    List<State> a2 = s2.getAllSuperstates();
    State cca = null;
    while(a1.size() != 0 && a2.size() != 0) {
      if(a1.get(0).equals(a2.get(0))) {
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

  public State nameToState(Name name) {
    if(this.name.equals(name.name.get(0))) {
      return this.nameToState(name, 0);
    }
    return null;
  }

  public String toString() {
    String s = "startchart " + this.name + " {\n";

    s += "types {\n";
    for(Type t : this.types) {
      s += t.toString();
    }
    s += "}\n";

    s += "events {\n";
    for(String event : this.events) {
      s += "\t" + event + ";\n";
    }
    s += "}\n";

    if(this.declarations != null) {
      for(Declaration d : this.declarations) {
        s += d.toString() + "\n";
      }
    }
  
    s += "\nentry : " + this.entry + "\n"; 
    s += "\nexit : " + this.exit + "\n";

    s += "functions {\n";
    for(FunctionDeclaration fdec : this.functionDeclarations) {
      s += fdec.toString() + ";\n";
    }
    s += "}\n";
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
