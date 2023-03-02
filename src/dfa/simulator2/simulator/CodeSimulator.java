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

  public void simulate() {
    Set<CFGCode> cfgCodes = this.code.getFirstCFGCodeSet();
    for(CFGCode cfgCode : cfgCodes) {
      this.controlPoints.add(cfgCode.cfg.entryNode);
    }
    for(CFGNode n : controlPoints) {
      System.out.println(n);
    }
    while(controlPoints.isEmpty() == false) {
      CFGNode node = this.randomSelect();
      this.controlPoints.remove(node);
      if(node instanceof CFGAssignmentNode) {
	CFGAssignmentNode assignmentNode = (CFGAssignmentNode)node;
        this.interpreter.execute(assignmentNode.assignment, this.env);
	this.controlPoints.add(assignmentNode.getSuccessor());
      }
      else if(node instanceof CFGSkipNode) {
        this.simulateSkipNode((CFGSkipNode)node);
      }
      else if(node instanceof CFGDecisionNode) {
        simulateDecisionNode((CFGDecisionNode)node);
      }
    }
  }

  private void simulateSkipNode(CFGSkipNode node) {
    if(node.equals(node.getCFG().exitNode)) {
      CFGCode code = this.cfgMap.get(node.getCFG());
      Set<CFGCode> nextCodes = code.getNextCFGCodeSet();

      Set<CFGNode> nextNodes = new HashSet<>();
      for(CFGCode nextCode : nextCodes) {
        nextNodes.add(nextCode.cfg.entryNode);
      }
      if(nextNodes.size() > 1) {
        for(CFGNode nextNode : nextNodes) {
          this.controlPoints.add(nextNode);
	}
      }
      else if(nextNodes.size() == 1) {
        List<CFGNode> list = new ArrayList<>(nextNodes);
        CFGNode nextNode = list.get(0);
        if(this.joinPoints.keySet().contains(nextNode) == false) {
    //      this.joinPoints.put(nextNode,);
          
        }
      }
    }
    else {
      System.out.println("Skip ...");
    }
  }

  private void simulateDecisionNode(CFGDecisionNode node) {
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
