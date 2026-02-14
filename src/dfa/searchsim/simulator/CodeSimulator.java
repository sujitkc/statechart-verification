package searchsim.simulator;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.Action;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

import ast.*;
import searchsim.code.*;
import searchsim.cfg.*;
import searchsim.tree.*; 
import searchsim.digraph.*;
import searchsim.simulator.ExternalState; 
////////////////////////////////////////////////////////////////////////////////////////////////////////////
import searchsim.property.Property; 
////////////////////////////////////////////////////////////////////////////////////////////////////////////


public class CodeSimulator{
  private ActionLanguageInterpreter interpreter;
  private Code code;
  private final Map<CFG, CFGCode> cfgMap;
  private ExternalState initState; 
  private Digraph<SimState>internalControlFlowGraph; 
  private Queue<SimState>internalQueue = new LinkedList<>(); 
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Stubborn set integration
  private StubbornSet stubbornSet;
  // Property checking integration
  private Property property;
  // Property violation flag - set to true when property is violated
  private boolean propertyViolated = false;
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*
   * Since the code simulator executes one whole code fragment
   * and returns a digraph of internal nodes, we need to attach 
   * that digraph to the external node on which the transition 
   * code is generated for
   * 
   * Hence we need a small queue and a big queue, the smaller queue
   * will create the graph of internal nodes which contain the respective
   * ready sets, and the bigger queue will contain external nodes on
   * which the code has to be generated and executed
   * 
   * (Note the code might be different for different external states, since
   * the transitions enabled depend on the environment)
   * 
   * Since we are storing partial environment in the internal nodes, a function 
   * has to be implemented which traverses up the state ladder, however only 
   * till it reaches the ExternalState ancestor which contains the full environment
   * 
   *  - K.S.
   */

  public CodeSimulator(Code code, ExternalState init , String mode) {
    this(code, init, mode, null); // Default: no property checking
  }

  public CodeSimulator(Code code, ExternalState init , String mode, Property prop) {
    this.code = code;
    CodeVisitor visitor = new CodeVisitor();
    visitor.visit(code);
    this.initState = init; 
    this.cfgMap = this.makeCFGMap();
    interpreter = new ActionLanguageInterpreter(mode);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Property checking integration
    this.property = prop;
    // Stubborn set integration with property-aware dependencies
    if (this.property != null) {
        this.stubbornSet = new StubbornSet(this.cfgMap, this.property);
    } else {
        this.stubbornSet = new StubbornSet(this.cfgMap);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //this.internalControlFlowGraph = new Digraph<SimState>(this.initState); 
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

  private Internode simulateAssignmentNode(CFGAssignmentNode node , MachineState currMS) throws Exception {
    CFGAssignmentNode assignmentNode = (CFGAssignmentNode)node;
    Set<Declaration> depVarSet = ActionLanguageInterpreter.getDependentVarSet(assignmentNode.assignment.rhs);
    Map<Declaration, Expression> currEnv = currMS.getDependendentEnvironment(depVarSet); 

    Map<Declaration, Expression> newEnv = interpreter.partialEval(assignmentNode.assignment, currEnv);
    
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

  private Internode simulateExitNode(CFGNode node , MachineState currMS, Map<CFGNode , Set<CFGNode>>joinPoints) {

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

    // System.out.println(currMS + " " + node + " " + currMS.getjoinPoints()); 
    // System.out.println(nextNodes);
    // System.out.println("BREAK");

    //System.out.println(currMS.getReadySet() + " " + nextNodes); 
    if(nextNodes.isEmpty()) {
      //System.out.println("No next nodes");
      //System.out.println(" -- Code Simulation Ends. --");
      
      return null;
    }
    for(CFGNode s : nextNodes) {
      //System.out.println(s); 
      // if s is in joinPoints then it has already been visited from at least
      // one of the other running threads.
      if(joinPoints.keySet().contains(s) == false) {
        CFGCode scode = this.cfgMap.get(s.getCFG());
        Set<CFGCode> prevCodes = scode.getPreviousCFGCodeSet();
        Set<CFGNode> sPredecessors = new HashSet<>();
	for(CFGCode prevCode : prevCodes) {
          sPredecessors.add(prevCode.cfg.exitNode);
          //System.out.println("Predecessor added : " + prevCode.cfg);
	}
	joinPoints.put(s , sPredecessors);
      }
      Set<CFGNode> sPredecessors = joinPoints.get(s);
      sPredecessors.remove(node); // this can be done blindly. Failure
         // would mean some bug somewhere else.

      // this means that all predecessors have been processed.
      if(sPredecessors.isEmpty()) {
        joinPoints.remove(s); 
        resNode.addCP(s);
        System.out.println(resNode);
      }
    }
    return resNode; 
    //System.out.println("====================");
  }

  private Internode simulateDecisionNode(CFGDecisionNode node , MachineState currMS) throws Exception {
    Set<Declaration> depVarSet = ActionLanguageInterpreter.getDependentVarSet(node.condition);
    Map<Declaration , Expression> currEnv = currMS.getDependendentEnvironment(depVarSet); 

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

  private Internode smallSim(CFGNode node , MachineState currMS) throws Exception{
    if(node instanceof CFGAssignmentNode) {
      return this.simulateAssignmentNode((CFGAssignmentNode)node , currMS);
    }
    else if(node instanceof CFGSkipNode) {
      return this.simulateSkipNode((CFGSkipNode)node);
    }
    else if(node instanceof CFGDecisionNode) {
      return this.simulateDecisionNode((CFGDecisionNode)node , currMS);
    }
    return null; 
  }


  private void generateNewReadySet(MachineState currMS) throws Exception{// machine state param
    Set<CFGNode> currReadySet = currMS.getReadySet(); 
    if(currReadySet.size() == 0)
      return ; 

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Apply stubborn set reduction
    Set<CFGNode> explorationSet = this.stubbornSet.buildStubbornSetWithNES(currReadySet);
    // Store the stubborn set in the current machine state
    currMS.setStubbornSet(explorationSet);

    for(CFGNode iternode : explorationSet) // Use reduced set for exploration
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
      Set<CFGNode> newReadySet= new HashSet<CFGNode>(); 
      Map<Declaration , Expression> newEnv = currMS.getCloneEnv();  
      Map<CFGNode , Set<CFGNode>>newJPSet = currMS.getjoinPointsClone(); 

      // Compute new ready set from ready set
      for(CFGNode compnode : currReadySet)
      {
        if(compnode.equals(iternode))
        {
          continue; 
        }
        newReadySet.add(compnode); 
      }


      if(iternode.equals(iternode.getCFG().exitNode)) {
        Internode resExit = this.simulateExitNode(iternode , currMS, newJPSet);

        if(resExit != null && resExit.getCP() != null){
          for(CFGNode n : resExit.getCP())
          {
            //for SkipNodes, immediately process their exit since they do nothing
            //this is needed to maintain proper join synchronization
            if(n instanceof CFGSkipNode){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
              //SkipNode entry = exit, so process it as exit immediately
              Internode skipExit = this.simulateExitNode(n, currMS, newJPSet);
              if(skipExit != null && skipExit.getCP() != null) {
                for(CFGNode skipNext : skipExit.getCP()) {
                    newReadySet.add(skipNext);
                }
              }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
              continue; 
            }
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
            //handle SkipNodes - they still need to be processed for synchronization
            if(n instanceof CFGSkipNode){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
              //since SkipNode entry = exit, process it as exit immediately
              Internode skipExit = this.simulateExitNode(n, currMS, newJPSet);
              if(skipExit != null && skipExit.getCP() != null) {
                for(CFGNode skipNext : skipExit.getCP()) {
                  newReadySet.add(skipNext);
                }
              }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
              continue; 
            }
          newReadySet.add(n); 
        }
      }
      MachineState newMS = new MachineState(newReadySet, newEnv); 
      newMS.setParent(currMS);
      newMS.addJoinPoints(newJPSet);
      // check property on the new state
      if (this.property != null) {
        Map<Declaration, Expression> fullEnv = newMS.collectEnv(newEnv);
        boolean satisfied = this.property.evaluate(fullEnv);
        newMS.setPropertyStatus(satisfied);
        newMS.setPropertyName(this.property.getName());
        
        // If property is violated, print trace and stop execution
        if (!satisfied) {
          this.internalControlFlowGraph.addChild(currMS, newMS); // Add the violating state to graph
          this.propertyViolated = true; // Set the violation flag
          printTraceToState(newMS);
          System.out.println("Stopping execution due to property violation.");
          return; // Stop further exploration
        }
      }
      this.internalControlFlowGraph.addChild(currMS , newMS); 
      this.internalQueue.add(newMS); 
    }
    // this.mainSimulate();
  }

  private void mainSimulate() throws Exception {
    // if(this.internalQueue.size() == 0)
    //   return; 

    // MachineState topMS = (MachineState)this.internalQueue.remove(); 
    // this.generateNewReadySet(topMS);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    while(this.internalQueue.size() != 0 && !this.propertyViolated) {
      MachineState topMS = (MachineState)this.internalQueue.remove(); 
      this.generateNewReadySet(topMS);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  }

  // Print the trace from initial state to the given state by backtracking through parent states
  private void printTraceToState(MachineState violatingState) {
    System.out.println("\n========================================");
    System.out.println("PROPERTY VIOLATION DETECTED!");
    System.out.println("Property: " + violatingState.getPropertyName() + " = false");
    System.out.println("========================================\n");
    
    // Collect the trace by backtracking from violating state to initial state
    List<MachineState> trace = new ArrayList<>();
    SimState current = violatingState;
    
    while (current != null) {
      if (current instanceof MachineState) {
        trace.add(0, (MachineState)current); // Add at beginning to reverse order
      }
      current = current.getParent();
    }
    
    // Print the trace
    System.out.println("Trace from initial state to violating state:");
    System.out.println("---------------------------------------------");
    for (int i = 0; i < trace.size(); i++) {
      MachineState state = trace.get(i);
      System.out.println("\nState " + i + ":");
      System.out.println(state.toString());
      
      if (i == trace.size() - 1) {
        System.out.println(">>> VIOLATION OCCURS HERE <<<");
      }
    }
    System.out.println("\n========================================");
    System.out.println("Total states in trace: " + trace.size());
    System.out.println("========================================\n");
  }

  public void simulate() throws Exception {
    Set<CFGCode> cfgCodes = this.code.getFirstCFGCodeSet();
    Set<CFGNode> initReadySet = new HashSet<CFGNode>(); 
    for(CFGCode cfgCode : cfgCodes) {
      initReadySet.add(cfgCode.cfg.entryNode);
    }

    MachineState motherTree = new MachineState(initReadySet, new HashMap<>()); 
    motherTree.setParent(this.initState);
    // check property on the initial state
    if (this.property != null) {
      Map<Declaration, Expression> fullEnv = motherTree.collectEnv(new HashMap<>());
      boolean satisfied = this.property.evaluate(fullEnv);
      motherTree.setPropertyStatus(satisfied);
      motherTree.setPropertyName(this.property.getName());
      
      // If property is violated in initial state, print trace and stop
      if (!satisfied) {
        this.internalControlFlowGraph = new Digraph<SimState>(motherTree);
        this.propertyViolated = true; // Set the violation flag
        printTraceToState(motherTree);
        System.out.println("Stopping execution due to property violation in initial state.");
        return; // Stop execution
      }
    }
    this.internalControlFlowGraph = new Digraph<SimState>(motherTree); 
    internalQueue.add(motherTree); 
    this.mainSimulate(); 

    System.out.println(" -- Code Simulation Ends. --");
    
  }

  public Digraph<SimState>getInternalDigraph(){
    return this.internalControlFlowGraph; 
  }

  // if a property violation was detected during simulation
  public boolean isPropertyViolated() {
    return this.propertyViolated;
  }
}
