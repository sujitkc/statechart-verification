package ast;

import java.util.List;
import java.util.ArrayList;

public class State {

  public final String name;
  public final List<State> states;
  public final List<Transition> transitions;
  public final List<Declaration> declarations;

  protected Statechart statechart = null;
  protected State superstate = null;
  protected Environment environment = null;


  public State(
      String            name,
      List<State>       states,
      List<Transition>  transitions,
      List<Declaration> declarations) {

    this.name         = name;
    this.states       = states;
    this.transitions  = transitions;
    this.declarations = declarations;

    for(State st : this.states) {
      st.setSuperstate(this);
    }
  }

  public void setSuperstate(State s) {
    this.superstate = s;
  }

  public void setStatechart(Statechart sc) {
    this.statechart = sc;
    for(State s : this.states) {
      s.setStatechart(sc);
    }

    for(Transition t : this.transitions) {
      t.setStatechart(sc);
    }
  }

  /*
    Recursively sets up the source and destination States of transitions.
    Preconditions:
      The statechart should be setup.
  */
  protected void initialiseTransitions() {    
 
    for(State s : this.states) {
      s.initialiseTransitions();
    }

    for(Transition t : this.transitions) {
      t.setSourceDestinationStates();
    }
  }

  public State getSuperstate() {
    return this.superstate;
  }

  public List<State> getAllSuperstates() {
    if(this.superstate != null) {
      List<State> superstates = this.superstate.getAllSuperstates();
      superstates.add(this.superstate);
      return superstates;
    }
    return new ArrayList<State>();
  }

  public Boolean isAncestor(State s) {
    List<State> ancestors = s.getAllSuperstates();
    return ancestors.contains(this);
  }

  public Environment getEnvironment() {
    if(this.environment == null) { 
      if(this.superstate != null) {
        this.environment = new Environment(this.declarations, this.superstate.getEnvironment());
      }
      else {
        this.environment = new Environment(this.declarations, null);
      }
    }
    return this.environment;
  }  

  public String toString() {
    String s = "state " + this.name + "{\n";
     
    if(this.declarations != null) {
      for(Declaration d : this.declarations) {
        s += d.toString() + "\n";
      }
    }
   
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
