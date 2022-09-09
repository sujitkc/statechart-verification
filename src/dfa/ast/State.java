package ast;

import java.util.List;
import java.util.ArrayList;

public class State {

  public final String name;
  public final List<State> states;
  public final List<Transition> transitions;
  public final DeclarationList declarations;
  public final Statement entry;
  public final Statement exit;
  public final BooleanConstant history;
  //public final BooleanConstant shell;
  //public final BooleanConstant region;
  //public final BooleanConstant fin;
  

  protected Statechart  statechart  = null;
  protected State       superstate  = null;
  protected Environment environment = null;


  public State(
      String            name,
      DeclarationList   declarations,
      Statement         entry,
      Statement         exit,
      List<State>       states,
      List<Transition>  transitions,
      BooleanConstant   history
     ) {
/* BooleanConstant   region,
      BooleanConstant   shell,
      BooleanConstant   fin*/
    this.name         = name;
    this.declarations = declarations;
    this.entry        = entry;
    this.exit         = exit;
    this.states       = states;
    this.transitions  = transitions;
    this.history      = history;
    //this.shell        = shell;
    //this.region       = region;
    //this.fin          = fin;

    for(State st : this.states) {
      st.setSuperstate(this);
    }
    for(Transition t : this.transitions) {
      t.setState(this);
    }
    this.declarations.setState(this);
  }


  public String getFullName() {
    if(this.superstate == null) {
      return this.name;
    }
    else {
      return this.superstate.getFullName() + "." + this.name;
    }
  }

  /* 
    Takes a fully-qualified name and returns a State which has this
    name. Returns null if such a State is not found.
  */
  protected State nameToState(Name name, int i) {
    if(i == name.name.size() - 1) {
      return this;
    }
    else {
      for(State s : this.states) {
        if(s.name.equals(name.name.get(i + 1))) {
          return s.nameToState(name, i + 1);
        }
      }
      return null;
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
  protected void initialiseTransitions() throws Exception {    
 
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

  public State findSubstateByName(String name) {
    for(State s : this.states) {
      if(s.name.equals(name)) {
        return s;
      }
    }
    return null;
  }

  // returns the list of all superstates (this excluded), starting with the
  // top-level statechart.
  public List<State> getAllSuperstates() {
    if(this.superstate != null) {
      List<State> superstates = this.superstate.getAllSuperstates();
      superstates.add(this.superstate);
      return superstates;
    }
    return new ArrayList<State>();
  }

  // returns true if this is a proper (non-reflexive) ancestor of State s.
  public Boolean isAncestor(State s) {
    List<State> ancestors = s.getAllSuperstates();
    return ancestors.contains(this);
  }

  public Environment getEnvironment() {
    if(this.environment == null) { 
      if(this.superstate != null) {
        this.environment = new Environment(this.declarations,
          this.superstate.getEnvironment());
      }
      else {
        this.environment = new Environment(this.declarations, null);
      }
    }
    return this.environment;
  }  

  public BooleanConstant maintainsHisotry() {
    return this.history;
  }

  public BooleanConstant isShellState() {
    if(this instanceof ast.Shell)
      return new BooleanConstant(true);
    else
      return new BooleanConstant(false);
  }

  public BooleanConstant isRegionState() {
    if(this.getSuperstate() instanceof ast.Shell)
      return new BooleanConstant(true);
    else
      return new BooleanConstant(false);
  }

  /*public BooleanConstant isFinalState() {
    return this.fin;
  }*/

  public String toString() {
    String s = "\nstate " + this.name + "{\n";
     
    if(this.declarations != null) {
      for(Declaration d : this.declarations) {
        s += d.toString() + "\n";
      }
    }
  
    s += "\nentry : " + this.entry + "\n"; 
    s += "\nexit : " + this.exit + "\n";
 
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
    s += maintainsHisotry();
    s += "}\n";
    return s;    
  }
}
