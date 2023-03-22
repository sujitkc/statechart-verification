package simulator2.simulator;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;

import ast.*;
import simulator2.code.*;
import simulator2.cfg.*;

public class CodeSimulator {

  private ActionLanguageInterpreter interpreter = new ActionLanguageInterpreter();
  private Code code;
  private Map<Declaration, Expression> env;
  private final Map<CFG, CFGCode> cfgMap;
  private final Map<CFGNode, Set<CFGNode>> joinPoints = new HashMap<>();
  private final Set<CFGNode> controlPoints = new HashSet<>();

  public CodeSimulator(Code code, Map<Declaration, Expression> env) {
    this.code = code;
    CodeVisitor visitor = new CodeVisitor();
    visitor.visit(code);
    this.env = env;
    this.cfgMap = this.makeCFGMap();
  }

  private Map<CFG, CFGCode> makeCFGMap() {
    Map<CFG, CFGCode> map = new HashMap<>();
    Queue<Code> queue = new LinkedList<>();
    queue.add(this.code);
    while(queue.isEmpty() == false) {
      Code code = queue.remove();
      if(code instanceof CFGCode) {
        CFGCode cfgCode = (CFGCode)code;
        map.put(cfgCode.cfg, cfgCode);
      }
      else if(code instanceof SequenceCode) {
        SequenceCode sequenceCode = (SequenceCode)code;
        queue.addAll(sequenceCode.codes);
      }
      else if(code instanceof ConcurrentCode) {
        ConcurrentCode concurrentCode = (ConcurrentCode)code;
        queue.addAll(concurrentCode.codes);
      }
    }
    return map;
  }

  public void simulate() throws Exception {
    Set<CFGCode> cfgCodes = this.code.getFirstCFGCodeSet();
    for(CFGCode cfgCode : cfgCodes) {
      System.out.println("Initial control point : " + cfgCode.cfg);
      this.controlPoints.add(cfgCode.cfg.entryNode);
    }
    for(CFGNode n : controlPoints) {
      System.out.println(n);
    }
    while(controlPoints.isEmpty() == false) {
      CFGNode node = this.randomSelect();
      System.out.println("executing " + node);
      this.controlPoints.remove(node);
      if(node.equals(node.getCFG().exitNode)) {
        simulateExitNode(node);
      }
      else if(node instanceof CFGAssignmentNode) {
        this.simulateAssignmentNode((CFGAssignmentNode)node);
      }
      else if(node instanceof CFGSkipNode) {
        this.simulateSkipNode((CFGSkipNode)node);
      }
      else if(node instanceof CFGDecisionNode) {
        this.simulateDecisionNode((CFGDecisionNode)node);
      }
    }
  }

  private void simulateAssignmentNode(CFGAssignmentNode node) throws Exception {
   CFGAssignmentNode assignmentNode = (CFGAssignmentNode)node;
    ActionLanguageInterpreter.execute(assignmentNode.assignment, this.env);
    this.controlPoints.add(assignmentNode.getSuccessor());
  }
  
  private void simulateSkipNode(CFGSkipNode node) {
    System.out.println("Skip ...");
  }

  private void simulateExitNode(CFGNode node) {
    System.out.println("Simulating exit node " + node);
    CFGCode code = this.cfgMap.get(node.getCFG());
    Set<CFGCode> nextCodes = code.getNextCFGCodeSet();
    Set<CFGNode> nextNodes = new HashSet<>();
    for(CFGCode nextCode : nextCodes) {
      System.out.println("next code ka cfg = " + nextCode.cfg);
      nextNodes.add(nextCode.cfg.entryNode);
    }
    if(nextNodes.isEmpty()) {
      System.out.println("No next nodes");
      return;
    }
    for(CFGNode s : nextNodes) {
      // if s is in joinPoints then it has already been visited from at least
      // one of the other running threads.
      if(this.joinPoints.keySet().contains(s) == false) {
        CFGCode scode = this.cfgMap.get(s.getCFG());
        Set<CFGCode> prevCodes = scode.getPreviousCFGCodeSet();
        Set<CFGNode> sPredecessors = new HashSet<>();
	for(CFGCode prevCode : prevCodes) {
          sPredecessors.add(prevCode.cfg.exitNode);
          System.out.println("prev code ka cfg = " + prevCode.cfg);
	}
	this.joinPoints.put(s, sPredecessors);
      }
      Set<CFGNode> sPredecessors = this.joinPoints.get(s);
      sPredecessors.remove(node); // this can be done blindly. Failure
         // would mean some bug somewhere else.

      // this means that all predecessors have been processed.
      if(sPredecessors.isEmpty()) {
        this.joinPoints.remove(s);
        this.controlPoints.add(s);
      }
    }
  }

  private void simulateDecisionNode(CFGDecisionNode node) throws Exception {
    Expression e = this.interpreter.evaluate(node.condition, this.env);
    if(BooleanConstant.True.equals((BooleanConstant)e)) {
      this.controlPoints.add(node.thenSuccessor);
    }
    else {
      this.controlPoints.add(node.elseSuccessor);
    }
  }

  private CFGNode randomSelect() {
    int index = new Random().nextInt(this.controlPoints.size());
    int i = 0;
    for(CFGNode node : this.controlPoints) {
      if(i == index) {
        return node;
      }
      i++;
    }
    return null;
  }
}