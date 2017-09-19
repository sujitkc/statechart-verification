package ast;

import java.util.List;
public class State {

  private String name;
  private List<State> states;
  private List<Transition> transitions;

  public State(String name, List<State> states, List<Transition> transitions) {
    this.name        = name;
    this.states      = states;
    this.transitions = transitions;
  }

  public String toString() {
    String s = "state " + this.name + "{\n";
    if(this.states != null) {
      for(State st : this.states) {
        s += st.toString();
      }
    }
    if(this.transitions != null) {
      for(Transition tr : this.transitions) {
        s += tr.toString();
      }
    }
    s += "}";
    return s;    
  }
  
}
