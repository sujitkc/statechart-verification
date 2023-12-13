package searchsim.simulator;

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
import searchsim.code.*;
import searchsim.cfg.*;
import searchsim.tree.*; 

public class CodeSimulator {

  private ActionLanguageInterpreter interpreter;
  private Code code;
  private Map<Declaration, Expression> env;
  private final Map<CFG, CFGCode> cfgMap;
  private final Map<CFGNode, Set<CFGNode>> joinPoints = new HashMap<>();
  private final Set<CFGNode> controlPoints = new HashSet<>();
  /*
   * Queue to store the ready sets 
   * Tree to store MachineStates 
   */

  private Queue<MachineState> BigL; 
  private Tree<MachineState> miniStateTree; //need a digraph in future

  public CodeSimulator(Code code, Map<Declaration, Expression> env, String mode) {
  	
    this.code = code;
    CodeVisitor visitor = new CodeVisitor();
    visitor.visit(code);
    this.env = env;
    this.cfgMap = this.makeCFGMap();
    interpreter = new ActionLanguageInterpreter(mode);
    this.BigL = new LinkedList<>(); 
    
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

  
  private Internode smallSim(CFGNode node , MachineState currMS) throws Exception{
      if(node instanceof CFGAssignmentNode) {
        //System.out.println("Assign");
        return this.simulateAssignmentNode((CFGAssignmentNode)node , currMS);
      }
      else if(node instanceof CFGSkipNode) {
        //System.out.println("Skip"); 
        return this.simulateSkipNode((CFGSkipNode)node);
      }
      else if(node instanceof CFGDecisionNode) {
        //System.out.println("Decision"); 
        return this.simulateDecisionNode((CFGDecisionNode)node , currMS);
      }
      return null; 
  }


  private void generateNewReadySet(MachineState currMS) throws Exception{// machine state param
    Set<CFGNode> currReadySet = currMS.getReadySet(); 
    // for(CFGNode n: currReadySet){
    //   System.out.println(n);
    // }
    //System.out.println("---"); 
    if(currReadySet.size() == 0)
      return ; 

    for(CFGNode iternode : currReadySet)
    {
      Set<CFGNode> newReadySet= new HashSet<CFGNode>(); 
      Map<Declaration , Expression> newEnv = currMS.getCloneEnv();  
      for(CFGNode compnode : currReadySet)
      {
        if(compnode.equals(iternode))
        {
          continue; 
        }
        newReadySet.add(compnode); 
      }
      if(iternode.equals(iternode.getCFG().exitNode)) {
        //System.out.println("exit"); 
        Internode resExit = this.simulateExitNode(iternode , currMS);

        if(resExit != null && resExit.getCP() != null){
          for(CFGNode n : resExit.getCP())
          {
            newReadySet.add(n); 
          }
        }
      }

      Internode resNode = this.smallSim(iternode , currMS);
      if(resNode != null && resNode.getEnv() != null)
      {
        newEnv = resNode.getCloneEnv(); 
      } 
      if(resNode != null && resNode.getCP() != null){
        for(CFGNode n : resNode.getCP())
        {
          newReadySet.add(n); 
        }
      }

      if(newReadySet.size() != 0)
      {
        MachineState newMS = new MachineState(newReadySet , newEnv); 
        //System.out.println(newMS); 
        Tree<MachineState> newMSSubTree = new Tree<MachineState>(newMS); 
        this.miniStateTree.addSubtree(currMS , newMSSubTree); //add machine state subtree
        this.BigL.add(newMS); 
      }
    }
    this.mainSimulate();
  }

  private void mainSimulate() throws Exception {
    if(this.BigL.size() == 0)
      return; 

    MachineState topMS = this.BigL.remove(); 
    this.generateNewReadySet(topMS);
  }

  public void simulate() throws Exception {
    Set<CFGCode> cfgCodes = this.code.getFirstCFGCodeSet();
    for(CFGCode cfgCode : cfgCodes) {
      //System.out.println("Initial control point : " + cfgCode.cfg);
      this.controlPoints.add(cfgCode.cfg.entryNode);
    }

     
    MachineState motherTree = new MachineState(this.controlPoints , this.env); 
    this.miniStateTree = new Tree<MachineState>(motherTree); 
    BigL.add(motherTree); 
    //System.out.println(this.miniStateTree); 
    this.mainSimulate(); 
    
    /*  System.out.println("Printing all control points : ");
    
    
    for(CFGNode n : controlPoints) {
      System.out.println(n.toString());
    }*/
    //while(BigL.isEmpty() == false) {
      //MachineState topMS = BigL.remove(); 
      /*
       * exhaustive creation of ready sets from this.controlPoints ~ the current ready set 
       */

      //this.generateNewReadySet(topMS);
      
      //this.controlPoints.remove(node);
     /*Original code */ 
     /*if(node.equals(node.getCFG().exitNode)) {
        
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
      }*/
      
      /*Changed by Karthika -- earlier the exit node was not being executed*/
      
      // if(node instanceof CFGAssignmentNode) {
      //   this.simulateAssignmentNode((CFGAssignmentNode)node);
      // }
      // else if(node instanceof CFGSkipNode) {
      //   this.simulateSkipNode((CFGSkipNode)node);
      // }
      // else if(node instanceof CFGDecisionNode) {
      //   this.simulateDecisionNode((CFGDecisionNode)node);
      // }
      // if(node.equals(node.getCFG().exitNode)) {
        
      //   simulateExitNode(node);
      // }
     
     // System.out.println("Printing Environment: "+this.env.values()+"-----[[[[[[[[]]]]]]]]");

    //}
     System.out.println(" -- Code Simulation Ends. --");
    
    //System.out.println("Printing Environment: "+this.env.values()+"-----[[[[[[[[]]]]]]]]");

  }

  public Set<MachineState> getEndPoints()
  {
    //System.out.println(this.miniStateTree);
    return this.miniStateTree.getLeafNodes(); 
  }

  public Map<Declaration, Expression> getRandomResultEnv()
  {
    return this.randomSelectEnv(this.miniStateTree.getLeafNodes()); 
  }

  private Internode simulateAssignmentNode(CFGAssignmentNode node , MachineState currMS) throws Exception {
    Map<Declaration, Expression> currEnv = currMS.getCloneEnv(); 
   CFGAssignmentNode assignmentNode = (CFGAssignmentNode)node;
    // return type change for execute method to update environment - changed by karthika
Map<Declaration, Expression> newEnv = interpreter.execute(assignmentNode.assignment, currEnv);
    //if it is exit node, it will not have a successor - changed by karthika
    // for(Map.Entry<Declaration , Expression>entry : newEnv.entrySet())
    //     {
    //         System.out.println(entry.getKey().getFullVName() + " " + entry.getValue() + "\n");
    //     }
    Internode res = new Internode(newEnv); 
    if(assignmentNode.getSuccessor()!=null)
    {
      res.setCP(assignmentNode.getSuccessor()); 
    }
    return res; 
  }
  
  private Internode simulateSkipNode(CFGSkipNode node) {
    System.out.println("Skip ...");
    return null; 
  }

  private Internode simulateExitNode(CFGNode node , MachineState currMS) {
    //System.out.println("Simulating exit node " + node);
    Internode resNode = new Internode(null); 
    Map<Declaration, Expression> currEnv = currMS.getCloneEnv(); 
    //System.out.println("In concurrency"); 
  
    CFGCode code = this.cfgMap.get(node.getCFG());
    Set<CFGCode> nextCodes = code.getNextCFGCodeSet();
    Set<CFGNode> nextNodes = new HashSet<>();
    //System.out.println("====================");
    for(CFGCode nextCode : nextCodes) {
      //System.out.println("cfg of next code = " + nextCode.cfg);
      nextNodes.add(nextCode.cfg.entryNode);
    }
    if(nextNodes.isEmpty()) {
      //System.out.println("No next nodes");
      //System.out.println(" -- Code Simulation Ends. --");
      
      return null;
    }
    for(CFGNode s : nextNodes) {
      //System.out.println(s); 
      // if s is in joinPoints then it has already been visited from at least
      // one of the other running threads.
      if(currMS.getjoinPoints().keySet().contains(s) == false) {
        CFGCode scode = this.cfgMap.get(s.getCFG());
        Set<CFGCode> prevCodes = scode.getPreviousCFGCodeSet();
        Set<CFGNode> sPredecessors = new HashSet<>();
	for(CFGCode prevCode : prevCodes) {
          sPredecessors.add(prevCode.cfg.exitNode);
          //System.out.println("Predecessor added : " + prevCode.cfg);
	}
	currMS.getjoinPoints().put(s, sPredecessors);
      }
      Set<CFGNode> sPredecessors = currMS.getjoinPoints().get(s);
      sPredecessors.remove(node); // this can be done blindly. Failure
         // would mean some bug somewhere else.

      // this means that all predecessors have been processed.
      if(sPredecessors.isEmpty()) {
        currMS.getjoinPoints().remove(s);
        resNode.addCP(s);
      }
    }
    return resNode; 
    //System.out.println("====================");
  }

  private Internode simulateDecisionNode(CFGDecisionNode node , MachineState currMS) throws Exception {
    Map<Declaration , Expression> currEnv = currMS.getCloneEnv(); 
    Expression e = this.interpreter.evaluate(node.condition, currEnv);
    Internode resNode = new Internode(null); 
    
    if(BooleanConstant.True.equals((BooleanConstant)e)) {
      resNode.setCP(node.thenSuccessor);
    }
    else {
      resNode.setCP(node.elseSuccessor);
    }
    return resNode; 
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

  private Map<Declaration, Expression> randomSelectEnv(Set<MachineState>endPoints) {
    int index = new Random().nextInt(endPoints.size());
    int i = 0;
    for(MachineState node : endPoints) {
      if(i == index) {
        return node.getEnv();
      }
      i++;
    }
    return null;
  }
}
