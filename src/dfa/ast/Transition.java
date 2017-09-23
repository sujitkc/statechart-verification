package ast;

import java.util.List;

public class Transition {
  private String name;
  private Name   sourceName;
  private Name   destinationName;
  private State source;
  private State destination;
  private Expression guard;
  private Statement action;

  private State parent;
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

  private Environment getReadEnvironment() {
    if(this.readEnvironment == null) {
      this.readEnvironment = new Environment(this.source.declarations, null);
    }
    return this.readEnvironment;
  }

  private Environment getRWEnvironment() {
    if(this.rwEnvironment == null) {
      this.rwEnvironment = this.destination.getEnvironment();
    }
    return this.rwEnvironment;
  }

  private Environment getEnvironment() {
    if(this.environment == null) {
      this.environment = this.getReadEnvironment().copy(this.getRWEnvironment());
    }
    return this.environment;
  }

  public void setStatechart(Statechart sc) {
    this.statechart = sc;
  }

  public String toString() {
    String s = "\ntransition " + this.name + "{\n";
    s += "source : " + this.source + "\n";
    s += "destination : " + this.destination + "\n";
    s += "guard : " + this.guard + "\n";
    s += "action : " + this.action + "\n";
    s += "\n}";
    return s;
  }
}
