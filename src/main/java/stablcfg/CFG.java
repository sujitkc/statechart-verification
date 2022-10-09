package stablcfg;

import java.util.Set;

import ast.Declaration;
import ast.DeclarationList;
import ast.Type;
import ast.FunctionDeclaration;

import ast.Name;
import java.util.List;
import utilities.IdGenerator;
import java.util.HashSet;
import java.util.Set;

public class CFG {

  private String name;

  public final DeclarationList declarations;
  public final List<Type> types;
  public final List<FunctionDeclaration> functionDeclarations;

  private CFGBasicBlockNode startNode; 
  private CFGBasicBlockNode stopNode; 
  
  private Set<CFGDecisionNode> decisionNodeList = new HashSet<CFGDecisionNode>();
  private Set<CFGBasicBlockNode> cfgBasicBlockNodeList = new HashSet<CFGBasicBlockNode>();
  private Set<CFEdge> edges = new HashSet<CFEdge>();



// Initialises CFG instance with start & end node
  public CFG(
      String name,
      DeclarationList declarations,
      List<Type> types,
      List<FunctionDeclaration> functionDeclarations,
      CFGBasicBlockNode start,
      CFGBasicBlockNode stop) throws Exception {

    this.name = name;
    this.declarations = declarations;
    this.types = types;
    this.functionDeclarations = functionDeclarations;
    this.startNode = start;
    if(start == null || stop == null) {
      Exception e = new Exception("Can't construct CFG: start or stop node is null.");
      throw e;
    }
    this.stopNode = stop;
    this.cfgBasicBlockNodeList.add(start);
    this.cfgBasicBlockNodeList.add(stop);
    this.name = CFG.generateId();
  }

/*
  public CFG(String name, DeclarationList declarations, CFGBasicBlockNode start, CFGBasicBlockNode stop) throws Exception {
    this.startNode = start;
    if(start == null || stop == null) {
      Exception e = new Exception("Can't construct CFG: start or stop node is null.");
      throw e;
    }

    this.declarations = declarations;
    this.stopNode = stop;
    this.cfgBasicBlockNodeList.add(start);
    this.cfgBasicBlockNodeList.add(stop);
    
    if(IdGenerator.hasId(name)) {
      Exception e = new Exception("Can't construct CFG : something with name '" + name + "' already exists.");
      throw e;
    }
    IdGenerator.addId(name);
    this.name = name;
  }
*/

  private static String generateId() {
    return IdGenerator.generateId("CFG");
  }

  public CFGBasicBlockNode getStartNode() {
    return this.startNode;
  }

  public CFGBasicBlockNode getStopNode() {
    return this.stopNode;
  }

  public CFGDecisionNode addDecisionNode(CFGDecisionNode node) {
    if(this.hasDecisionNode(node)) {
      return null;
    }
    this.decisionNodeList.add(node);
    return node;
  }

  public CFGBasicBlockNode addBasicBlockNode(CFGBasicBlockNode node) {
    if(this.hasBasicBlockNode(node)) {
      return null;
    }
    this.cfgBasicBlockNodeList.add(node);
    return node;
  }

  public boolean hasBasicBlockNode(CFGBasicBlockNode node) {
    return this.cfgBasicBlockNodeList.contains(node);
  }

  public int getNumberOfBasicBlockNodes() {
    return this.cfgBasicBlockNodeList.size();
  }

  public boolean hasDecisionNode(CFGDecisionNode node) {
    return this.decisionNodeList.contains(node);
  }

  public int getNumberOfDecisionNodes() {
    return this.decisionNodeList.size();
  }

  public boolean hasNode(CFGNode node) {
    if(node instanceof CFGDecisionNode) {
      return(this.hasDecisionNode((CFGDecisionNode)node));
    }
    if(node instanceof CFGBasicBlockNode) {
      return(this.hasBasicBlockNode((CFGBasicBlockNode)node));
    }
    return false;
  }

  public int getNumberOfNodes() {
    return this.cfgBasicBlockNodeList.size() + this.decisionNodeList.size();
  }

  public CFEdge addEdge(CFEdge edge) throws Exception {
    CFGNode h = edge.getHead();
    CFGNode t = edge.getTail();
    if(!(this.hasNode(h) && this.hasNode(t))) {
      return null;
    }
    this.edges.add(edge);
    if((t instanceof CFGBasicBlockNode)) {
      CFGBasicBlockNode node = (CFGBasicBlockNode)t;
      node.setOutgoingEdge(edge);
    }
    else {
      CFGDecisionNode node = (CFGDecisionNode)t;
      if(!((node.getThenEdge() == null) || (node.getElseEdge() == null))) {
        return null;
      }
      if(node.getThenEdge() == null) {
        node.setThenEdge(edge);
      }
      else if(node.getElseEdge() == null) {
        node.setElseEdge(edge);
      }
    }
    h.addIncomingEdge(edge);
    return edge;
  }

  public boolean hasEdge(CFEdge edge) {
    return this.edges.contains(edge);
  }

  public int getNumberOfEdges() {
    return this.edges.size();
  }

  public Set<CFGNode> getNodeSet() {
    Set<CFGNode> nodeSet = new HashSet<CFGNode>();
    for(CFGNode n : this.cfgBasicBlockNodeList) {
      nodeSet.add(n);
    }
    for(CFGNode n : this.decisionNodeList) {
      nodeSet.add(n);
    }
    return nodeSet;
  }

  public Set<CFEdge> getEdgeSet() {
    return this.edges;
  }

  public Set<CFGDecisionNode> getDecisionNodeSet() {
    return this.decisionNodeList;
  }

  public Set<CFGBasicBlockNode> getBasicBlockNodeSet() {
    return this.cfgBasicBlockNodeList;
  }
  
  public boolean addDeclaration(Declaration declaration) {
    return this.declarations.add(declaration);
  }
  
  public boolean hasVariable(Name var) {
    if(this.declarations.lookup(var.toString()) == null) {
      return false;
    }
    return true;
  }

  public Set<Name> getVariables() {
    Set<Name> vars = new HashSet<Name>();
    for(Declaration d : this.declarations) {
      vars.add(new Name(d.vname));
    }
    return vars;
  }

  public String getName() {
    return this.name;
  }
  
  public String toString() {
    String s = "CFG = " + this.name;
    s = "Nodes {\n";
    for(CFGNode node : this.getNodeSet()) {
      s = s + node.toString();
    }
    for(CFEdge edge : this.getEdgeSet()) {
      s = s + edge.toString();
    }
    return s;
  }
}
