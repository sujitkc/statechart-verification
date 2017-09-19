package ast;

import java.util.List;

public class Transition {
  private String name;
  private String source;
  private String destination;

  public Transition(String name, String src, String dest) {
    this.name = name;
    this.source = src;
    this.destination = dest;
  }

  public String toString() {
    String s = "\ntransition " + this.name + "{\n";
    s += "source : " + this.source + "\n";
    s += "destination : " + this.destination + "\n";
    s += "\n}";
    return s;
  }
}
