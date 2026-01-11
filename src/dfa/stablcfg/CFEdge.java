package stablcfg;

import utilities.IdGenerator;

public class CFEdge {

  CFGNode tail = null;
  CFGNode head = null;
  private String name;

  public CFEdge(CFGNode tail, CFGNode head) {
    this.tail = tail;
    this.head = head;
    this.name = CFEdge.generateId();
  }

  private static String generateId() {
    return IdGenerator.generateId("CFEdge");
  }

  public CFEdge(String name, CFGNode tail, CFGNode head) throws Exception {
    this.tail = tail;
    this.head = head;
    if(IdGenerator.hasId(name)) {
      Exception e = new Exception("Can't construct CFEdge : something with name '" + name + "' already exists.");
      throw e;      
    }
    IdGenerator.addId(name);
    this.name = name;
  }

  public CFGNode getHead() {
    return this.head;
  }

  public CFGNode getTail() {
    return this.tail;
  }

  public String getName() {
    return this.name;
  }
  
  public String toString() {
    return "CFEdge " + this.name + " tail = " + this.tail.name + " head = " + this.head.name + "\n";
  }

  public void setHead(CFGNode node) {
    this.head = node;
  }

  public void setTail(CFGNode node) {
    this.tail = node;
  }  

  public void setEdgeAnnotation(boolean bool){}

  public boolean getEdgeAnnotation(){
    return false;
  }
}
