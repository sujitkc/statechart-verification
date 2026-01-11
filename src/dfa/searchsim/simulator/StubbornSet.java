package searchsim.simulator;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayDeque;

import ast.*;
import searchsim.cfg.*;
import searchsim.code.*;
import searchsim.property.Property;

public class StubbornSet {

    private final Map<CFG, CFGCode> cfgMap;
    private final Set<CFGNode> allNodes;
    private final Map<CFGNode, Set<CFGNode>> globalDependencyMap;
    private final Map<CFGNode, Set<CFGNode>> reverseReachablesMap;
    private CFGNode lastSeed; // seed used in stubborn set (for debugging)
    private final Property property; // property for sound invariant checking
    private final Set<Declaration> propertyVariables; // variables in the property

    public StubbornSet(Map<CFG, CFGCode> cfgMap) {
        this(cfgMap, null);
    }

    public StubbornSet(Map<CFG, CFGCode> cfgMap, Property property) {
        this.cfgMap = cfgMap;
        this.property = property;
        this.allNodes = this.getAllCFGNodes();
        this.propertyVariables = extractPropertyVariables(property);
        this.globalDependencyMap = this.buildDependencyMap(this.allNodes);
        this.reverseReachablesMap = this.computeReverseReachables(this.allNodes);
    }

    public Map<CFGNode, Set<CFGNode>> getGlobalDependencyMap() {
        return this.globalDependencyMap;
    }

    public Map<CFGNode, Set<CFGNode>> getReverseReachablesMap() {
        return this.reverseReachablesMap;
    }

    public Set<CFGNode> getAllNodes() {
        return this.allNodes;
    }

    public CFGNode getLastSeed() {
        return this.lastSeed;
    }

    // extract all CFG nodes from the code structure
    private Set<CFGNode> getAllCFGNodes() {
        Set<CFGNode> nodeSet = new HashSet<>();
        
        for (CFG cfg : this.cfgMap.keySet()) {
            getAllNodesInCFG(cfg, nodeSet);
        }
        return nodeSet;
    }

    private void getAllNodesInCFG(CFG cfg, Set<CFGNode> allNodes) {
        if (cfg.entryNode == null) return;
        
        Queue<CFGNode> queue = new ArrayDeque<>();
        queue.add(cfg.entryNode);
        
        while (!queue.isEmpty()) {
            CFGNode node = queue.poll();
            
            // skip if already processed
            if (allNodes.contains(node)) continue;
            
            allNodes.add(node);
            addSuccessors(node, queue, allNodes);
        }
        
        // ensure exit node is included
        if (cfg.exitNode != null && !allNodes.contains(cfg.exitNode)) {
            allNodes.add(cfg.exitNode);
        }
    }
    
    private void addSuccessors(CFGNode node, Queue<CFGNode> queue, Set<CFGNode> visited) {
        if (node instanceof CFGAssignmentNode assignNode) {
            CFGNode successor = assignNode.getSuccessor();
            if (successor != null && !visited.contains(successor)) {
                queue.add(successor);
            }
        }
        else if (node instanceof CFGDecisionNode decisionNode) {
            if (decisionNode.thenSuccessor != null && !visited.contains(decisionNode.thenSuccessor)) {
                queue.add(decisionNode.thenSuccessor);
            }
            if (decisionNode.elseSuccessor != null && !visited.contains(decisionNode.elseSuccessor)) {
                queue.add(decisionNode.elseSuccessor);
            }
        }
        else if (node instanceof CFGSkipNode skipNode) {
            CFGNode successor = skipNode.getSuccessor();
            if (successor != null && !visited.contains(successor)) {
                queue.add(successor);
            }
        }
    }

    // Extracts all variables referenced in a property expression.
    // Returns empty set if property is null
    private Set<Declaration> extractPropertyVariables(Property property) {
        if (property == null) {
            return new HashSet<>();
        }
        
        Expression expr = property.getExpression();
        Set<Declaration> variables = ActionLanguageInterpreter.getDependentVarSet(expr);
        
        // getDependentVarSet returns null for constants, so handle that case
        return (variables != null) ? variables : new HashSet<>();
    }

    // check if two CFG nodes are dependent (RW, WR, or WW on same variable)
    public boolean areDependent(CFGNode n1, CFGNode n2) {
        Set<Declaration> read1 = n1.getReadSet();
        Set<Declaration> write1 = n1.getWriteSet();
        Set<Declaration> read2 = n2.getReadSet();
        Set<Declaration> write2 = n2.getWriteSet();

        // check for WW:
        for (Declaration d : write1) {
            if (write2.contains(d)) {
                return true;
            }
        }

        // check for WR:
        for (Declaration d : write1) {
            if (read2.contains(d)) {
                return true;
            }
        }

        // check for RW:
        for (Declaration d : read1) {
            if (write2.contains(d)) {
                return true;
            }
        }

        // Property-aware dependency: if both nodes access ANY property variable,
        // they are dependent to ensure sound invariant checking
        if (!this.propertyVariables.isEmpty()) {
            Set<Declaration> accessed1 = new HashSet<>();
            accessed1.addAll(read1);
            accessed1.addAll(write1);
            
            Set<Declaration> accessed2 = new HashSet<>();
            accessed2.addAll(read2);
            accessed2.addAll(write2);
            
            // Check if node1 accesses any property variable
            boolean node1AccessesPropertyVar = false;
            for (Declaration propVar : this.propertyVariables) {
                if (accessed1.contains(propVar)) {
                    node1AccessesPropertyVar = true;
                    break;
                }
            }
            
            // Check if node2 accesses any property variable
            boolean node2AccessesPropertyVar = false;
            for (Declaration propVar : this.propertyVariables) {
                if (accessed2.contains(propVar)) {
                    node2AccessesPropertyVar = true;
                    break;
                }
            }
            
            if (node1AccessesPropertyVar && node2AccessesPropertyVar) {
                return true;
            }
        }

        return false;
    }

    // builds a map where key is a node, value is the set of nodes it depends on
    private Map<CFGNode, Set<CFGNode>> buildDependencyMap(Set<CFGNode> nodes) {
        Map<CFGNode, Set<CFGNode>> depMap = new HashMap<>();
        for (CFGNode n1 : nodes) {
            Set<CFGNode> deps = new HashSet<>();
            for (CFGNode n2 : nodes) {
                if (!n1.equals(n2) && areDependent(n1, n2)) {
                    deps.add(n2);
                }
            }
            depMap.put(n1, deps);
        }
        return depMap;
    }

    // precompute all nodes that can reach each node
    private Map<CFGNode, Set<CFGNode>> computeReverseReachables(Set<CFGNode> allNodes) {
        Map<CFGNode, Set<CFGNode>> reverseReachables = new HashMap<>();
        
        for (CFGNode node : allNodes) {
            Set<CFGNode> reachable = new HashSet<>();
            computeReverseReachablesHelper(node, reachable, new HashSet<>());
            reachable.remove(node); // exclude the node itself
            reverseReachables.put(node, reachable);
        }
        
        return reverseReachables;
    }
    
    private void computeReverseReachablesHelper(CFGNode node, Set<CFGNode> reachable, Set<CFGNode> visited) {
        if (visited.contains(node)) return;
        visited.add(node);
        reachable.add(node);
        
        // direct predecessors (intra-CFG reachability)
        for (CFGNode predecessor : node.getPredecessors()) {
            computeReverseReachablesHelper(predecessor, reachable, visited);
        }
        
        // if entry node, check Code hierarchy (inter-CFG reachability)
        if (node.getCFG().entryNode == node) {
            addInterCFGPredecessors(node, reachable, visited);
        }
    }
    
    private void addInterCFGPredecessors(CFGNode entryNode, Set<CFGNode> reachable, Set<CFGNode> visited) {
        CFG currentCFG = entryNode.getCFG();
        CFGCode currentCFGCode = this.cfgMap.get(currentCFG);
        
        if (currentCFGCode != null) {
            Set<CFGCode> predecessorCFGCodes = findPredecessorCFGCodes(currentCFGCode);
            
            for (CFGCode predCFGCode : predecessorCFGCodes) {
                CFG predCFG = predCFGCode.cfg;
                // add exit nodes from predecessor CFGs
                if (predCFG.exitNode != null) {
                    computeReverseReachablesHelper(predCFG.exitNode, reachable, visited);
                }
            }
        }
    }
    
    private Set<CFGCode> findPredecessorCFGCodes(CFGCode targetCFGCode) {
        Set<CFGCode> predecessors = new HashSet<>();
        
        for (CFGCode cfgCode : this.cfgMap.values()) {
            Set<CFGCode> nextCFGCodes = cfgCode.getNextCFGCodeSet();
            if (nextCFGCodes.contains(targetCFGCode)) {
                predecessors.add(cfgCode);
            }
        }
        
        return predecessors;
    }

    // compute Necessary Enabling Set (NES) for a disabled node
    public Set<CFGNode> computeNES(CFGNode disabledNode, Set<CFGNode> enabledSet, Set<CFGNode> stubbornSet) {
        Set<CFGNode> reverseReach = this.reverseReachablesMap.getOrDefault(disabledNode, new HashSet<>());
        
        // check if any enabled node in stubborn set can reach the disabled node
        Set<CFGNode> enabledInStubborn = new HashSet<>(enabledSet);
        enabledInStubborn.retainAll(stubbornSet);
        enabledInStubborn.retainAll(reverseReach);
        
        if (!enabledInStubborn.isEmpty()) {
            // some enabled transition in stubborn set can reach this node, so NES is empty
            return new HashSet<>();
        }
        
        // find candidates: enabled nodes not in stubborn set that can reach the disabled node
        Set<CFGNode> candidates = new HashSet<>(enabledSet);
        candidates.removeAll(stubbornSet);
        candidates.retainAll(reverseReach);
        
        // return one arbitrary candidate (or empty if none)
        if (!candidates.isEmpty()) {
            CFGNode chosen = candidates.iterator().next();
            Set<CFGNode> nes = new HashSet<>();
            nes.add(chosen);
            return nes;
        }
        
        return new HashSet<>();
    }

    // stubborn set computation
    public Set<CFGNode> buildStubbornSetWithNES(Set<CFGNode> enabledSet) {
        if (enabledSet.isEmpty()) {
            return new HashSet<>();
        }

        // start with one arbitrary enabled node
        // CFGNode seed = enabledSet.iterator().next();
        CFGNode seed = enabledSet.toArray(new CFGNode[0])[new Random().nextInt(enabledSet.size())];
        this.lastSeed = seed;
        Set<CFGNode> stubbornSet = new HashSet<>();
        Set<CFGNode> worklist = new HashSet<>();
        
        stubbornSet.add(seed);
        worklist.add(seed);

        while (!worklist.isEmpty()) {
            CFGNode x = worklist.iterator().next();
            worklist.remove(x);
            
            if (enabledSet.contains(x)) {
                // x is enabled: add all dependent nodes
                Set<CFGNode> dependents = this.globalDependencyMap.getOrDefault(x, new HashSet<>());
                for (CFGNode y : dependents) {
                    if (!stubbornSet.contains(y)) {
                        stubbornSet.add(y);
                        worklist.add(y);
                    }
                }
            } else {
                // x is disabled: add NES(x)
                Set<CFGNode> nes = this.computeNES(x, enabledSet, stubbornSet);
                for (CFGNode y : nes) {
                    if (!stubbornSet.contains(y)) {
                        stubbornSet.add(y);
                        worklist.add(y);
                    }
                }
            }
        }

        // intersection of stubborn set and enabled set
        stubbornSet.retainAll(enabledSet);
        return stubbornSet;
    }

}
