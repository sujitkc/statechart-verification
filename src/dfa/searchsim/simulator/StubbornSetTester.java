package searchsim.simulator;

import java.util.*;
import ast.*;
import searchsim.cfg.*;
import searchsim.code.*;


//Based on the e1 event execution from 1_1_concurrent_less.stbl:
//Event e1 triggers concurrent execution:
//Branch 1: x:=23; x:=43; x:=35;  (R1A exit -> transition action -> R1B entry)
//Branch 2: y:=31; y:=33; x:=39;  (R2A exit -> transition action -> R2B entry)
public class StubbornSetTester {
    
    public static void main(String[] args) {
        
        try {
            StubbornSetTester tester = new StubbornSetTester();
            
            Map<CFG, CFGCode> cfgMap = tester.createSplitCFGStructure();
            // Map<CFG, CFGCode> cfgMap = tester.createDummyCFGStructure();

            System.out.println("Created " + cfgMap.size() + " CFG(s)");

            StubbornSet stubbornSet = new StubbornSet(cfgMap);
            
            Set<CFGNode> allNodes = stubbornSet.getAllNodes();
            System.out.println("Found " + allNodes.size() + " CFG nodes");
            tester.printAllNodes(allNodes);
            
            Map<CFGNode, Set<CFGNode>> depMap = stubbornSet.getGlobalDependencyMap();
            tester.printDependencyMap(depMap);
            
            Map<CFGNode, Set<CFGNode>> reverseReachMap = stubbornSet.getReverseReachablesMap();
            tester.printReverseReachabilityMap(reverseReachMap);
            
            System.out.println("Stubborn Set Computation Analysis:");
            tester.testStubbornSetComputation(stubbornSet, allNodes);
            
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    
//Structure: ConcurrentCode(SequenceCode(CFG1A, CFG1B), SequenceCode(CFG2A, CFG2B))
//Branch 1: CFG1A (x:=23, x:=43) -> CFG1B (x:=35) [Sequential]
//Branch 2: CFG2A (y:=31, y:=33) -> CFG2B (x:=39) [Sequential]
    private Map<CFG, CFGCode> createSplitCFGStructure() {
        Declaration varX = createVariableDeclaration("x", "int");
        Declaration varY = createVariableDeclaration("y", "int");
        
        CFGAssignmentNode node1A1 = createAssignmentNode(varX, createIntConstant(23));
        CFGAssignmentNode node1A2 = createAssignmentNode(varX, createIntConstant(43));
        node1A1.setSuccessor(node1A2);
        CFG cfg1A = new CFG("Branch1A_split", node1A1, node1A2);
        CFGCode cfgCode1A = new CFGCode(cfg1A);
        
        CFGAssignmentNode node1B1 = createAssignmentNode(varX, createIntConstant(35)); 
        CFG cfg1B = new CFG("Branch1B_split", node1B1, node1B1);
        CFGCode cfgCode1B = new CFGCode(cfg1B);
        
        List<Code> branch1Sequence = Arrays.asList(cfgCode1A, cfgCode1B);
        SequenceCode sequenceBranch1 = new SequenceCode(branch1Sequence);
        
        CFGAssignmentNode node2A1 = createAssignmentNode(varY, createIntConstant(31));
        CFGAssignmentNode node2A2 = createAssignmentNode(varY, createIntConstant(33));
        node2A1.setSuccessor(node2A2);
        CFG cfg2A = new CFG("Branch2A_split", node2A1, node2A2);
        CFGCode cfgCode2A = new CFGCode(cfg2A);
        
        CFGAssignmentNode node2B1 = createAssignmentNode(varX, createIntConstant(39)); 
        CFG cfg2B = new CFG("Branch2B_split", node2B1, node2B1);
        CFGCode cfgCode2B = new CFGCode(cfg2B);
        
        List<Code> branch2Sequence = Arrays.asList(cfgCode2A, cfgCode2B);
        SequenceCode sequenceBranch2 = new SequenceCode(branch2Sequence);
        
        Set<Code> concurrentBranches = new HashSet<>(Arrays.asList(sequenceBranch1, sequenceBranch2));
        ConcurrentCode rootConcurrentCode = new ConcurrentCode(concurrentBranches);
        
        Map<CFG, CFGCode> cfgMap = makeCFGMap(rootConcurrentCode);
        
        return cfgMap;
    }
    
    private Map<CFG, CFGCode> makeCFGMap(Code rootCode) {
        Map<CFG, CFGCode> map = new HashMap<>();
        Queue<Code> queue = new LinkedList<>();
        queue.add(rootCode);
        
        while (!queue.isEmpty()) {
            Code code = queue.remove();
            if (code instanceof CFGCode cfgCode) {
                map.put(cfgCode.cfg, cfgCode);
            } else if (code instanceof SequenceCode sequenceCode) {
                queue.addAll(sequenceCode.codes);
            } else if (code instanceof ConcurrentCode concurrentCode) {
                queue.addAll(concurrentCode.codes);
            }
        }
        return map;
    }
    
//Structure: ConcurrentCode(CFGCode(CFG1), CFGCode(CFG2))
//Branch 1: CFG1 (x:=23, x:=43, x:=35)
//Branch 2: CFG2 (y:=31, y:=33, x:=39)
    private Map<CFG, CFGCode> createDummyCFGStructure() {
        Declaration varX = createVariableDeclaration("x", "int");
        Declaration varY = createVariableDeclaration("y", "int");
        
        CFGAssignmentNode node1 = createAssignmentNode(varX, createIntConstant(23));
        CFGAssignmentNode node2 = createAssignmentNode(varX, createIntConstant(43));
        CFGAssignmentNode node3 = createAssignmentNode(varX, createIntConstant(35));
        
        node1.setSuccessor(node2);
        node2.setSuccessor(node3);
        
        CFG cfg1 = new CFG("Branch1_e1", node1, node3);
        CFGCode cfgCode1 = new CFGCode(cfg1);
        
        CFGAssignmentNode node4 = createAssignmentNode(varY, createIntConstant(31));
        CFGAssignmentNode node5 = createAssignmentNode(varY, createIntConstant(33));
        CFGAssignmentNode node6 = createAssignmentNode(varX, createIntConstant(39));
        
        node4.setSuccessor(node5);
        node5.setSuccessor(node6);
        
        CFG cfg2 = new CFG("Branch2_e1", node4, node6);
        CFGCode cfgCode2 = new CFGCode(cfg2);
        
        Set<Code> concurrentBranches = new HashSet<>(Arrays.asList(cfgCode1, cfgCode2));
        ConcurrentCode rootConcurrentCode = new ConcurrentCode(concurrentBranches);
        
        Map<CFG, CFGCode> cfgMap = makeCFGMap(rootConcurrentCode);
        
        return cfgMap;
    }
    
    private Declaration createVariableDeclaration(String name, String type) {
        TypeName typeName = new TypeName(type);
        return new Declaration(name, typeName, false);
    }
    
    private IntegerConstant createIntConstant(int value) {
        return new IntegerConstant(value);
    }
    
    private CFGAssignmentNode createAssignmentNode(Declaration variable, Expression value) {
        Name lhs = new Name(variable.vname);
        lhs.setDeclaration(variable);
        AssignmentStatement assignment = new AssignmentStatement(lhs, value);
        return new CFGAssignmentNode(assignment);
    }
    
    private void printAllNodes(Set<CFGNode> allNodes) {
        System.out.println("Node Details:");
        int nodeIndex = 1;
        for (CFGNode node : allNodes) {
            System.out.println("Node " + nodeIndex + ": " + node.toString());
            System.out.println("Read Set: " + formatDeclarationSet(node.getReadSet()));
            System.out.println("Write Set: " + formatDeclarationSet(node.getWriteSet()));
            System.out.println();
            nodeIndex++;
        }
    }
    
    private void printDependencyMap(Map<CFGNode, Set<CFGNode>> depMap) {
        System.out.println("Dependency Analysis:");
        for (Map.Entry<CFGNode, Set<CFGNode>> entry : depMap.entrySet()) {
            CFGNode node = entry.getKey();
            Set<CFGNode> dependencies = entry.getValue();
            
            System.out.println(getNodeDescription(node) + "Depends on:");
            if (dependencies.isEmpty()) {
                System.out.println("(no dependencies)");
            } else {
                for (CFGNode dep : dependencies) {
                    System.out.print(getNodeDescription(dep));
                }
            }
            System.out.println();
        }
    }
    
    private void printReverseReachabilityMap(Map<CFGNode, Set<CFGNode>> reverseReachMap) {
        System.out.println("Reverse Reachability Analysis:");
        for (Map.Entry<CFGNode, Set<CFGNode>> entry : reverseReachMap.entrySet()) {
            CFGNode node = entry.getKey();
            Set<CFGNode> reachableFrom = entry.getValue();
            
            System.out.println(getNodeDescription(node) + "Is reachable from:");
            if (reachableFrom.isEmpty()) {
                System.out.println("(no predecessors)");
            } else {
                for (CFGNode pred : reachableFrom) {
                    System.out.print(getNodeDescription(pred));
                }
            }
            System.out.println();

        }
    }
    
    private void testStubbornSetComputation(StubbornSet stubbornSet, Set<CFGNode> allNodes) {
        
        System.out.println("Case 1: Initial state (x:=23 from Branch1, y:=31 from Branch2)");
        Set<CFGNode> initialEnabled = new HashSet<>();
        for (CFGNode node : allNodes) {
            String nodeDesc = getNodeDescription(node);
            if (nodeDesc.contains("x := 23") || nodeDesc.contains("y := 31")) {
                initialEnabled.add(node);
            }
        }
        Set<CFGNode> stubbornSet1 = stubbornSet.buildStubbornSetWithNES(initialEnabled);
        System.out.println("    Enabled: " + initialEnabled.size() + " nodes");
        System.out.println("    Seed chosen: " + getNodeDescription(stubbornSet.getLastSeed()));
        System.out.println("    Stubborn Set: " + stubbornSet1.size() + " nodes");
        printStubbornSetDetails(stubbornSet1);
        
        System.out.println("Case 2: (x:=43 from Branch1, y:=33 from Branch2)");
        Set<CFGNode> secondEnabled = new HashSet<>();
        for (CFGNode node : allNodes) {
            String nodeDesc = getNodeDescription(node);
            if (nodeDesc.contains("x := 43") || nodeDesc.contains("y := 33")) {
                secondEnabled.add(node);
            }
        }
        Set<CFGNode> stubbornSet2 = stubbornSet.buildStubbornSetWithNES(secondEnabled);
        System.out.println("    Enabled: " + secondEnabled.size() + " nodes");
        System.out.println("    Seed chosen: " + getNodeDescription(stubbornSet.getLastSeed()));
        System.out.println("    Stubborn Set: " + stubbornSet2.size() + " nodes");
        printStubbornSetDetails(stubbornSet2);
        
        System.out.println("Case 3: (x:=35 from Branch1B, x:=39 from Branch2B)");
        Set<CFGNode> finalEnabled = new HashSet<>();
        for (CFGNode node : allNodes) {
            String nodeDesc = getNodeDescription(node);
            if (nodeDesc.contains("x := 35") || nodeDesc.contains("x := 39")) {
                finalEnabled.add(node);
            }
        }
        Set<CFGNode> stubbornSet3 = stubbornSet.buildStubbornSetWithNES(finalEnabled);
        System.out.println("    Enabled: " + finalEnabled.size() + " nodes");
        System.out.println("    Seed chosen: " + getNodeDescription(stubbornSet.getLastSeed()));
        System.out.println("    Stubborn Set: " + stubbornSet3.size() + " nodes");
        printStubbornSetDetails(stubbornSet3);
    }
    
    private void printStubbornSetDetails(Set<CFGNode> stubbornSet) {
        System.out.println("    Stubborn Set Contents:");
        for (CFGNode node : stubbornSet) {
            System.out.println("      - " + getNodeDescription(node));
        }
    }

    private String getNodeDescription(CFGNode node) {
        if (node instanceof CFGAssignmentNode assignNode) {
            return node.getCFG().name + ": " + assignNode.assignment.toString();
        }
        return node.getCFG().name + ": " + node.getClass().getSimpleName();
    }
    

    private String formatDeclarationSet(Set<Declaration> declarations) {
        if (declarations.isEmpty()) {
            return "{}";
        }
        
        String result = "{";
        Declaration[] declArray = declarations.toArray(new Declaration[0]);
        
        for (int i = 0; i < declArray.length; i++) {
            result += declArray[i].vname;
            if (i < declArray.length - 1) {
                result += ", ";
            }
        }
        result += "}";
        return result;
    }
}