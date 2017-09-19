package ast;

import java.util.List;

public class Transition {
  private String name;
  private Name   source;
  private Name   destination;
  private Expression guard;
  private Statement action;

  public Transition(String name, Name src, Name dest, Expression guard, Statement action) {
    this.name = name;
    this.source = src;
    this.destination = dest;
    this.guard = guard;
    this.action = action;
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
