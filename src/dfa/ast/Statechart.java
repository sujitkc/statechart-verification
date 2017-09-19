package ast;

import java.util.List;

public class Statechart {

  private String name;
  private List<State> states;
  private List<Transition> transitions;

  public Statechart(String name, List<State> states, List<Transition> transitions) {
    this.name        = name;
    this.states      = states;
    this.transitions = transitions;
  }

  public String toString() {
    String s = "startchart " + this.name + " {";
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
