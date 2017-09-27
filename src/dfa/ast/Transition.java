package ast;

import java.util.List;

public class Transition {
  public final String name;
  private Name   sourceName;
  private Name   destinationName;
  private State source;
  private State destination;
  public final Expression guard;
  public final Statement action;

  private State state;
  private Statechart statechart;

  private Environment readEnvironment = null;
  private Environment environment = null;
  private Environment rwEnvironment = null;
  
  public Transition(String name, Name src, Name dest, Expression guard, Statement action) {
    this.name = name;
    this.sourceName = src;
    this.destinationName = dest;
    this.guard = guard;
    this.action = action;
  }

  public State getSource() {
    return this.source;
  }

  public State getDestination() {
    return this.destination;
  }

  public State getState() {
    return this.state;
  }

  public Environment getReadEnvironment() {
    if(this.readEnvironment == null) {
      this.readEnvironment = new Environment(this.source.declarations, null);
    }
    return this.readEnvironment;
  }

  public Environment getRWEnvironment() {
    if(this.rwEnvironment == null) {
      this.rwEnvironment = this.destination.getEnvironment();
    }
    return this.rwEnvironment;
  }

  public Environment getEnvironment() {
    if(this.environment == null) {
      this.environment = this.getReadEnvironment().copy(this.getRWEnvironment());
    }
    return this.environment;
  }

  public void setSourceDestinationStates() throws Exception {
    this.source = this.statechart.nameToState(this.sourceName);
    if(this.source == null) {
      throw new Exception("Transition.setSourceDestinationStates : couldn't find state by name '" + this.sourceName + "'.");
    }
    this.destination = this.statechart.nameToState(this.destinationName);
    if(this.destination == null) {
      throw new Exception("Transition.setSourceDestinationStates : couldn't find state by name '" + this.destinationName + "'.");
    }
  }

  public void setStatechart(Statechart sc) {
    this.statechart = sc;
  }

  public void setState(State s) {
    this.state = s;
  }

  public String toString() {
    String s = "\ntransition " + this.name + "{\n";
    s += "source : " + this.source.getFullName() + "\n";
    s += "destination : " + this.destination.getFullName() + "\n";
    s += "guard : " + this.guard + "\n";
    s += "action : " + this.action + "\n";
    s += "\n}";
    return s;
  }
}
