package utilities.prettyprint;

import stablcfg.*;

public class CFGToDot {

  public String toDot(CFG cfg) {
    String s = "Digraph " + cfg.getName() + " {\n";
    for(CFGNode node : cfg.getNodeSet()) {
      if(node instanceof CFGBasicBlockNode) {
        CFGBasicBlockNode bbnode = (CFGBasicBlockNode)node;
        s += "  " + bbnode.name + "[shape=box,label=\"" + this.replaceTroubleSomeChars(bbnode.statement.toString()) + "\"];\n";
      }
      else if(node instanceof CFGDecisionNode) {
        CFGDecisionNode dnode = (CFGDecisionNode)node;
        s += "  " + dnode.name + "[label=\"" + this.replaceTroubleSomeChars(dnode.condition.toString()) + "\"];\n";
      }
    }

    for(CFEdge edge : cfg.getEdgeSet()) {
      String colour = "black";
      if(edge.getTail() instanceof CFGDecisionNode) {
        if(edge.equals(((CFGDecisionNode)edge.getTail()).getThenEdge())) {
          colour = "green";
        }
        else {
          colour = "red";
        }
      }
      s += "  " + edge.getTail().name + "->" + edge.getHead().name + "[color=\"" + colour +"\"];\n";
    }
    s += "}";
    return s;
  }

  private String replaceTroubleSomeChars(String s) {
    String news = "";
    for(char c : s.toCharArray()) {
      if(c == '\n') {
        news += "\\n";
      }
      else if(c == '\"') {
        news += "\\\"";
      }
      else {
        news += c;
      }
    }
    return news;
  }
}
