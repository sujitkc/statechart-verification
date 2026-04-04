package simulator2.simulator;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Comparator;
import java.util.TreeSet;

import ast.*;

import simulator2.*;
import simulator2.tree.*;
import simulator2.cfg.*;
import simulator2.code.*;
import com.code_intelligence.jazzer.api.FuzzerSecurityIssueMedium;
class FirstComparator implements Comparator<Name> {
	@Override public int compare(Name e1, Name e2)
	{
		return (e1.getDeclaration().vname).compareTo(e2.getDeclaration().vname);
	}
}
public class Simulator {

  public static int eventindex=0;
  public final Statechart statechart;
  private final Set<Transition> allTransitions;
  private final Map<Declaration, Expression> valueEnvironment;
  public final Tree<State> stateTree;
  public final Map<Statement, CFG> CFGs = new HashMap<>();
  private final ASTToCFG converter = new ASTToCFG();
  private Set<State> configuration;


public static void fuzzerInitialize() {
    // Optional initialization to be run before the first call to fuzzerTestOneInput.
  }


  public Simulator(Statechart statechart) throws Exception {
    this(statechart, new HashSet<State>());
  }

  public Simulator(Statechart statechart, Set<State> configuration) throws Exception {
    this.statechart = statechart;
    this.allTransitions = this.getAllTransitions();
    this.valueEnvironment = this.makeValueEnvironment();
    this.stateTree = this.getStateTree(this.statechart);
    this.makeCFGs(this.statechart);
    this.configuration = configuration;
  }
 public void printCurrentExecutionInfo(String event){
 	System.out.println(".........................");
      	System.out.println(eventindex++ +" : Consuming event : "+event);
	String con="Current configuration : [";
	for(State s : this.configuration){
		con+=s.name+", ";
	}
	con+="]";
	
	System.out.println(con);
	
	System.out.println("Current Environment : ");
	 this.valueEnvironment.forEach((k,v) -> System.out.println(""
                + k + " = " + v));
	
	
	
 	}
 public String getSimulationMode(){
 	System.out.println("Enter the preffered mode of simulation \n 1. Random(Default) \n 2. Interactive \n Enter 1 or 2 : ");
    Scanner in=new Scanner(System.in);
    String str=in.nextLine();
    String mode="random";
    if(str.equals("2"))
    	mode="interactive";
    return mode;
 }
 public String setRandomSimulationMode(){
 	return "random";
 }
  public Set<State> simulate(List<String> events) throws Exception {
    System.out.println ("==== Statechart Simulation begins ===");
    printCurrentExecutionInfo(" initializing statechart");
    
    
    //String mode=getSimulationMode();
    String mode=setRandomSimulationMode();
  
    Set<State> newConfiguration = new HashSet<>();
    this.configuration =  this.getEntrySubTree(this.statechart).getLeafNodes();
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+configuration);
    Tree<State> subtree = this.getEntrySubTree(statechart);
    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.entry);
        }
      },
      subtree);

    Code code = this.getDestinationCode(CFGTree);
    
    CodeSimulator codeSimulator = new CodeSimulator(code, this.valueEnvironment, mode);
    codeSimulator.simulate();

    for(String event : events) {
      
      newConfiguration=this.simulationStep(event);
      
    }
    return newConfiguration;
  }

  public Set<State> simulationStep(String event) throws Exception {
  /*
   * Compute the set of all enabled transitions
   * Check for non-determinism. Abort if found.
   * For each enabled transition, compute code
   * Compute complete code to be executed as the concurrent composition
   *   of transition-wise code.
   * while, there's code to execute, keep single-stepping
  */
    
  
    //String mode=getSimulationMode();
        printCurrentExecutionInfo(event);

        String mode=setRandomSimulationMode();
        //need edit by karthika
    Set<Transition> enabledTransitions = this.getEnabledTransitions(event);
    Set<State> newConfiguration = new HashSet<>();
    Code code = null;
    
    System.out.print("Enabled Transitions :");
    if(enabledTransitions.size() > 1) {
      Set<Code> codes = new HashSet<>();
      
      for(Transition t: enabledTransitions) {
      	 System.out.print(t.name+",");
        codes.add(this.getCode(t));
      }
      System.out.println();
      
      this.detectNondeterminism(codes);
     // this.detectConcurrencyConflict(codes);
      //code = new ConcurrentCode(codes);
      if(this.detectNondeterminism(codes)){
        List<Transition> tlist = new ArrayList<>(enabledTransitions);
        Random r=new Random();
        code = this.getCode(tlist.get(r.nextInt(tlist.size())));

      }else{
        code = new ConcurrentCode(codes);
      }

    }
    else if(enabledTransitions.size() == 1) {
      List<Transition> tlist = new ArrayList<>(enabledTransitions);
      Transition t = tlist.get(0);
      System.out.println(t.name+",");
      code = this.getCode(t);
    }
    else {
      System.out.println("No transition enabled.");
      return this.configuration;
    }
    System.out.println(" -- Code Simulation Begins --");
    CodeSimulator codeSimulator = new CodeSimulator(code, this.valueEnvironment, mode);
    codeSimulator.simulate();
    /*System.out.println("Value environment");
    for(Declaration d : this.valueEnvironment.keySet()) {
      System.out.println(d + " : " + this.valueEnvironment.get(d));
    }*/

    for(State s : this.configuration){
		Transition t = getTransitionForState(s,enabledTransitions);
	      if(t!=null){
		Set<State> atomicStates = this.getDestinationTree(t).getLeafNodes();
	     	 newConfiguration.addAll(atomicStates);
				
		}
	      else{
		newConfiguration.add(s);
		}
	      
	    }
    

    if(newConfiguration.isEmpty() == false) {
      this.configuration = newConfiguration;
    }
    /*System.out.print("States in configuration : {");
    for(State s : this.configuration) {
      System.out.print(s.name+", ");
    }
    System.out.println("}");*/
    return newConfiguration;
  }

 private void detectConcurrencyConflict(Set<Code> codes) {
try{
      System.out.println("detectConcurrencyConflict : "+codes.size()+" : "+codes);
      TreeSet<Name> definitions = new TreeSet<>(new FirstComparator());
      for(Code code : codes) {
      TreeSet<Name> codeDefinitions = new TreeSet<>(new FirstComparator());
      codeDefinitions.addAll(this.getAllVariablesinCode(code));
      //System.out.println("codeDefinitions ::"+codeDefinitions);
      
      
      TreeSet<Name> intersect = new TreeSet<>(new FirstComparator());
      intersect.addAll(definitions);

      //System.out.println("intersect ::"+intersect);
      intersect.retainAll(codeDefinitions);
      /*for(Name def:intersect){
      	System.out.println("DEf : "+def.getClass()+"::"+(codeDefinitions.get(0)).equals(def));
      }*/
      //System.out.println("intersect after retainall::"+intersect);
      if(intersect.isEmpty()) {
        definitions.addAll(codeDefinitions);
      }
      else {
        
        throw new FuzzerSecurityIssueMedium("Simulator::Concurrency-Conflict detected.::"+intersect);

      }
      //System.out.println("definitions ::"+definitions);
    }
}catch(Exception e){
        System.out.println("Exception caught: conflict detected");
	Runtime.getRuntime().halt(1);
}
 
 }

  
  public Transition getTransitionForState(State s, Set<Transition> enabledTransitions){
		try{
			   for(Transition t : enabledTransitions) {
				if((this.getSourceTree(t)).hasNode(s))
					return t;
				}
		
		}
		catch(Exception e){
			System.out.println("Get transition for state");
		}
	return null;
	
	}

  private boolean detectNondeterminism(Set<Code> codes) {
  
try{
    Set<CFG> cfgs = new HashSet<>();
    for(Code code : codes) {
      Set<CFG> codeCFGs = this.getAllCFGsinCode(code);
      Set<CFG> intersect = new HashSet<>(cfgs);
      intersect.retainAll(codeCFGs);
      if(intersect.isEmpty()) {
        cfgs.addAll(codeCFGs);
      }
      else {
       // return true;
        throw new FuzzerSecurityIssueMedium("Simulator::detectNondeterminism : Non-determinism detected.");
      }
    }
}
catch(Exception e){
   System.out.println("Exception caught: non-determinism");
   Runtime.getRuntime().halt(1);
}
    return false;
  }
 
 /*  private void detectNondeterminism(Set<Code> codes) throws Exception {
  
    Set<CFG> cfgs = new HashSet<>();
    for(Code code : codes) {
      Set<CFG> codeCFGs = this.getAllCFGsinCode(code);
      Set<CFG> intersect = new HashSet<>(cfgs);
      intersect.retainAll(codeCFGs);
      if(intersect.isEmpty()) {
        cfgs.addAll(codeCFGs);
      }
      else {
        return true;
        throw new FuzzerSecurityIssueMedium("Simulator::detectNondeterminism : Non-determinism detected.");
      }
    }
  }*/
  
  private Set<Name> getAllVariablesinCode(Code code) throws Exception {
  //System.out.println("getAllVariablesinCode");
    Set<Name> definitions = new HashSet<>();
    if(code instanceof CFGCode) {
      CFGCode cfgCode = (CFGCode)code;
      CFGBasicBlockNode node=(CFGBasicBlockNode)cfgCode.cfg.entryNode;
     // System.out.println(":>:>"+node);
      if(node instanceof CFGAssignmentNode){
      		Name lhs=((CFGAssignmentNode)node).assignment.lhs;
      		//System.out.println("lhs :"+lhs);
      		definitions.add(lhs);
      	}
      while(node!=cfgCode.cfg.exitNode){
      	//System.out.println("::::"+node.getSuccessor());
      	node=(CFGBasicBlockNode)node.getSuccessor();
      	if(node instanceof CFGAssignmentNode){
      		Name lhs=((CFGAssignmentNode)node).assignment.lhs;
      		//System.out.println("lhs :"+lhs);
      		definitions.add(lhs);
      	}
      }
      if(node==cfgCode.cfg.exitNode){
      	//System.out.println(":x::"+node);
      	if(node instanceof CFGAssignmentNode){
      		Name lhs=((CFGAssignmentNode)node).assignment.lhs;
      		//System.out.println("lhs :"+lhs);
      		definitions.add(lhs);
      	}
      }
      //cfgs.add(cfgCode.cfg);
    }
    else if(code instanceof SequenceCode) {
      SequenceCode sequenceCode = (SequenceCode)code;
      for(Code c : sequenceCode.codes) {
        definitions.addAll(this.getAllVariablesinCode(c));
      }
    }
    else if(code instanceof ConcurrentCode) {
      ConcurrentCode concurrentCode = (ConcurrentCode)code;
      for(Code c : concurrentCode.codes) {
        definitions.addAll(this.getAllVariablesinCode(c));
      }
    }
    else {
      throw new Exception("Simulator::getAllCFGsinCode - Not implemented.");
    }
   
    return definitions;
  }
  
  private Set<CFG> getAllCFGsinCode(Code code) throws Exception {
    Set<CFG> cfgs = new HashSet<>();
    if(code instanceof CFGCode) {
      CFGCode cfgCode = (CFGCode)code;
      cfgs.add(cfgCode.cfg);
    }
    else if(code instanceof SequenceCode) {
      SequenceCode sequenceCode = (SequenceCode)code;
      for(Code c : sequenceCode.codes) {
        cfgs.addAll(this.getAllCFGsinCode(c));
      }
    }
    else if(code instanceof ConcurrentCode) {
      ConcurrentCode concurrentCode = (ConcurrentCode)code;
      for(Code c : concurrentCode.codes) {
        cfgs.addAll(this.getAllCFGsinCode(c));
      }
    }
    else {
      throw new Exception("Simulator::getAllCFGsinCode - Not implemented.");
    }
    return cfgs;
  }

  private Map<Declaration, Expression> makeValueEnvironment() {
    Map<Declaration, Expression> environment = new HashMap<>();
    Set<Declaration> declarations = this.getAllDeclarations();
    Expression defaultValue = null;
    for(Declaration declaration : declarations) {
      if(declaration.typeName.equals("int")) {
        defaultValue = new IntegerConstant(0);
      }
      else if(declaration.typeName.equals("bool")) {
        defaultValue = new BooleanConstant(true);
      }
      environment.put(declaration, defaultValue);
    }
    return environment;
  }

  private Set<Declaration> getAllDeclarations() {
    Set<Declaration> declarations = new HashSet<>();
    Queue<State> queue = new LinkedList<>();
    queue.add(this.statechart);
    while(queue.isEmpty() == false) {
      State state = queue.remove();
      declarations.addAll(state.declarations);
      queue.addAll(state.states);
    }
    return declarations;
  }

  // Gives the set of all transitions inside of state in a recursive way.
  // useful in constructing and storing the set of transitions for fast access
  // later on.
  private Set<Transition> getAllTransitions() {
    Set<Transition> transitions = new HashSet<>();
    Queue<State> queue = new LinkedList<>();
    queue.add(this.statechart);
    while(queue.isEmpty() == false) {
      State state = queue.remove();
      transitions.addAll(state.transitions);
      queue.addAll(state.states);
    }
    return transitions;
  }

  // Recursively look for a state by the given name. 
  // Return the first match. Null on no match.
  public State getSubstateByName(String name, State state) {
    List<State> substates = state.states;
    for(State s : substates) {
      if(s.name.equals(name)) {
        return s;
      }
      State ss = this.getSubstateByName(name, s);
      if(ss != null) {
        return ss;
      }
    }
    return null;
  }

  // Generates a tree of states.
  // Useful in later plucking subtrees out of the main tree and
  // creating code fragments out of it. 
  private Tree<State> getStateTree(State state) throws Exception {
    Set<Tree<State>> trees = new HashSet<>();
    for(State child : state.states) {
      trees.add(this.getStateTree(child));
    }
    Tree<State> myStateTree = new Tree<State>(state);
    for(Tree<State> subtree : trees) {
      myStateTree.addSubtree(state, subtree);
    }
    return myStateTree;
  }

  private void makeCFGs(State state) throws Exception {
    /* action name is added by Karthika */
    this.converter.actionname=state.name+"_N";
   
    
    this.CFGs.put(state.entry, this.converter.convert(state.entry));
    this.converter.actionname=state.name+"_X";
    this.CFGs.put(state.exit, this.converter.convert(state.exit));
    for(Transition t : state.transitions) {
      this.converter.actionname=t.name+"_A";
      this.CFGs.put(t.action, this.converter.convert(t.action));
    }
    for(State s : state.states) {
      this.makeCFGs(s);
    }
  }

  private Set<Transition> getEnabledTransitions(String event)
      throws Exception {
    Set<Transition> eTransitions = new HashSet<>();
    for(Transition t : this.allTransitions) {
     
      if(t.trigger.equals(event)){
          eTransitions.add(t);
      

          } 
	  
    }
    Tree<State> slicedStateTree = this.stateTree.getSlicedSubtree(
      this.stateTree.root, this.configuration);
    Set<Transition> enabledTransitions = new HashSet<>();
    Set<State> allSourceStates = slicedStateTree.getAllNodes();
    for(Transition t : eTransitions) {
      if(allSourceStates.contains(t.getSource())) {
               BooleanConstant evaluatedGuard =
        (BooleanConstant)ActionLanguageInterpreter
	  .evaluate(t.guard, this.valueEnvironment);
          if(evaluatedGuard.equals(BooleanConstant.True))
      {
         enabledTransitions.add(t);
      }

       
      }	
    }
    return enabledTransitions;
  }

  private Code getSourceCode(Tree<CFG> tree) throws Exception {
    return this.getCode(tree);
  }

  private Code getDestinationCode(Tree<CFG> tree) throws Exception {
    Code rcode = this.getCode(tree);
    return rcode.reverse();
  }

  private Code getCode(Tree<CFG> tree) throws Exception {
    return this.getCode(tree.root, tree);
  }

  private Code getCode(CFG cfg, Tree<CFG> tree) throws Exception {
    Set<CFG> childCFGs = tree.getChildren(cfg);
    List<CFG> childCFGList = new ArrayList<>(childCFGs);
    Code myCode = new CFGCode(cfg);
    if(childCFGs.size() == 0) {
      return myCode;
    }
    else if(childCFGs.size() == 1) { // sequence code
      Code childCode = this.getCode(childCFGList.get(0), tree);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
    else { // concurrent code
      Set<Code> childCodes = new HashSet<>();
      for(CFG child : childCFGs) {
	childCodes.add(this.getCode(child, tree));
      }
      Code childCode = new ConcurrentCode(childCodes);
      Code[] codes = {childCode, myCode};
      return new SequenceCode(Arrays.asList(codes));
    }
  }

  private Set<State> getActiveAtomicSubstates(State state) throws Exception {
    Set<State> atomicStates = new HashSet<>();
    for(State atomicState : this.configuration) {
      if(this.stateTree.getAllAncestors(atomicState).contains(state)) {
        atomicStates.add(atomicState);
      }
    }
    return atomicStates;
  }

  private Tree<State> getSourceTree(Transition t) throws Exception {
    State lub = this.stateTree.lub(t.getSource(), t.getDestination());
    List<State> sourceAncestors = this.stateTree.getAllAncestorsUpto(t.getSource(), lub);
    Tree<State> sourceStateTree = null;
    if(sourceAncestors.size() > 1) {
      sourceAncestors.remove(sourceAncestors.size() - 1); // removing t.source.
      Shell shellAncestor = null;
      for(State ancestor : sourceAncestors) {
	if(ancestor instanceof Shell) {
          shellAncestor = (Shell)ancestor;
	  break;
	}
      }
      if(shellAncestor != null) {
        Set<State> atomicStates = this.getActiveAtomicSubstates(shellAncestor);
	Tree<State> subtree = this.stateTree.getSlicedSubtree(shellAncestor, atomicStates);
        List<State> higherAncestors = this.stateTree.getAllAncestorsUpto(shellAncestor, lub);
	if(higherAncestors.size() > 1) {
	  higherAncestors.remove(higherAncestors.size() - 1); // removing shell ancestor.
          sourceStateTree = new Tree<State>(higherAncestors.get(0));
          sourceStateTree.addPath(higherAncestors);
          State currentLeaf = higherAncestors.get(higherAncestors.size() - 1);
          sourceStateTree.addSubtree(currentLeaf, subtree);
	}
	else {
          sourceStateTree =subtree;
	}
      }
      else {
        Set<State> atomicStates = this.getActiveAtomicSubstates(t.getSource());
	Tree<State> subtree = this.stateTree.getSlicedSubtree(t.getSource(), atomicStates);
        sourceStateTree = new Tree<State>(sourceAncestors.get(0));
        sourceStateTree.addPath(sourceAncestors);
        State currentLeaf = sourceAncestors.get(sourceAncestors.size() - 1);
        sourceStateTree.addSubtree(currentLeaf, subtree);
      }
    }
    else {
      Set<State> atomicStates = this.getActiveAtomicSubstates(t.getSource());
      sourceStateTree = this.stateTree.getSlicedSubtree(t.getSource(), atomicStates);
    }
    return sourceStateTree;
  }

  private Code getSourceCode(Transition t) throws Exception {

    Tree<State> sourceStateTree = this.getSourceTree(t);
    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.exit);
        }
      },
      sourceStateTree);

    return this.getSourceCode(CFGTree);
  }

  private Tree<State> getEntrySubTree(State state) throws Exception {
    Tree<State> tree = new Tree<State>(state);
    if(state.states.isEmpty() == true) {
      return tree;
    }
    else if(state instanceof Shell) {
      for(State s : state.states) {
        tree.addSubtree(state, this.getEntrySubTree(s));
      }
      return tree;
    }
    else {
      tree.addSubtree(state, this.getEntrySubTree(state.states.get(0)));
      return tree;
    }
  }

  private Tree fdash(List<State>destAncPath , int inx , Transition t , State child) throws Exception
  {
    if(child.equals(destAncPath.get(inx+1)))
    {
      return f(destAncPath , inx+1 , t); 
    }
    return this.getEntrySubTree(child); 
  }

  private Tree f(List<State>destAncPath , int inx , Transition t) throws Exception
  {
    if(inx == destAncPath.size()-1)
    {
      Tree<State> subtree = this.getEntrySubTree(t.getDestination()); 
      return subtree; 
    }
    else if(destAncPath.get(inx) instanceof Shell)
    {
      Tree<State> destinationTree = new Tree<State>(destAncPath.get(inx)); 
      List<State> childStateList = destAncPath.get(inx).getAllSubstates(); 
      
      //System.out.println("SHELL " + destAncPath.get(inx).getFullName()); 
      for(State ch : childStateList){
        //System.out.println(ch.getFullName()); 
        // if(ch.equals(destAncPath.get(inx +1)))
        // {
        //   destinationTree.addSubtree(destAncPath.get(inx) , f(destAncPath, inx + 1, t)); 
        // }
        // else
        // {
        //   destinationTree.addSubtree(destAncPath.get(inx) , this.getEntrySubTree(ch)); 
        // }
        destinationTree.addSubtree(destAncPath.get(inx) , fdash(destAncPath, inx, t, ch));
      }

      return destinationTree; 
    }

    Tree<State> destinationTree = new Tree<State>(destAncPath.get(inx)); 
    destinationTree.addSubtree(destAncPath.get(inx) , f(destAncPath , inx+1 , t)); 
    return destinationTree; 
  }
  
  private Tree getDestinationTree(Transition t) throws Exception{
    Tree<State> destTree = null; 
    State lub = this.stateTree.lub(t.getSource() , t.getDestination());
    //System.out.println("DESTINATION : " + t.getDestination().getFullName()); 
    List<State> destAncList = this.stateTree.getAllAncestorsUpto(t.getDestination() , lub); 

    // System.out.println("DEST LIST"); 
    // for(State st : destAncList)
    // {
    //   System.out.println(st.getFullName()); 
    // }
    return this.f(destAncList , 0 , t); 
  }

  // private Tree getDestinationTree(Transition t) throws Exception{
  //   Tree<State> destinationStateTree = null;
  //   State lub = this.stateTree.lub(t.getSource(), t.getDestination());
  //   List<State> destinationAncestors = this.stateTree.getAllAncestorsUpto(t.getDestination(), lub);
  //   if(destinationAncestors.size() > 1) {
  //     destinationAncestors.remove(destinationAncestors.size() - 1); // removing t.destination.
  //     Shell shellAncestor = null;
  //     for(State ancestor : destinationAncestors) {
	// if(ancestor instanceof Shell) {
  //         shellAncestor = (Shell)ancestor;
	//   break;
	// }
  //     }
  //     if(shellAncestor != null) {
  //       Tree<State> subtree = this.getEntrySubTree(shellAncestor);
  //       List<State> higherAncestors = this.stateTree.getAllAncestorsUpto(shellAncestor, lub);
	// if(higherAncestors.size() > 1) {
	//   higherAncestors.remove(higherAncestors.size() - 1); // removing shell ancestor.
  //         destinationStateTree = new Tree<State>(higherAncestors.get(0));
  //         destinationStateTree.addPath(higherAncestors);
  //         State currentLeaf = higherAncestors.get(higherAncestors.size() - 1);
  //         destinationStateTree.addSubtree(currentLeaf, subtree);
	// }
	// else {
  //         destinationStateTree = subtree;
	// }
  //     }
  //     else {
  //       Tree<State> subtree = this.getEntrySubTree(t.getDestination());
  //       destinationStateTree = new Tree<State>(destinationAncestors.get(0));
  //       destinationStateTree.addPath(destinationAncestors);
  //       State currentLeaf = destinationAncestors.get(destinationAncestors.size() - 1);
  //       destinationStateTree.addSubtree(currentLeaf, subtree);
  //     }
  //   }
  //   else {
  //     destinationStateTree = this.getEntrySubTree(t.getDestination());
  //   }

  //   return destinationStateTree;
  // }

  private Code getDestinationCode(Transition t) throws Exception {
    Tree<State> destinationStateTree = this.getDestinationTree(t);
    //System.out.println(destinationStateTree); 

    Map<Statement, CFG> CFGs = this.CFGs;
    TreeMap<State, CFG> map = new TreeMap<>();
    Tree<CFG> CFGTree = map.map(
      new Function<State, CFG>() {
        public CFG apply(State state) {
	  return CFGs.get(state.entry);
        }
      },
      destinationStateTree);

    return this.getDestinationCode(CFGTree);
  }

  private Code getCode(Transition t) throws Exception {
    Code sourceCode = this.getSourceCode(t);
    Code actionCode = new CFGCode(this.CFGs.get(t.action));
    Code destinationCode = this.getDestinationCode(t);
    Code[] codes = {sourceCode, actionCode, destinationCode};
    SequenceCode code = new SequenceCode(Arrays.asList(codes));
    return code;
  }
}
