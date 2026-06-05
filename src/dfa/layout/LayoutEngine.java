package layout;

import ast.Statechart;
import ast.State;
import ast.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutEngine {

    public List<Transition> flippedEdges = new ArrayList<>();
    public Map<State, LayoutNode> layoutNodes = new HashMap<>();

    public Map<State, LayoutNode> calculateCoordinates(Statechart statechart) {
        layoutNodes.clear();
        
        for (State s : statechart.getAllSubstates()) {
            layoutNodes.put(s, new LayoutNode(s));
        }
        
        if (statechart.transitions != null) {
            for (Transition t : statechart.transitions) {
                LayoutNode sourceNode = layoutNodes.get(t.getSource());
                LayoutNode destNode = layoutNodes.get(t.getDestination());
                
                if (sourceNode != null && destNode != null) {
                    sourceNode.outDegree++;
                    sourceNode.outgoingNodes.add(destNode);
                    
                    destNode.inDegree++;
                    destNode.incomingNodes.add(sourceNode);
                }
            }
        }
        
        List<LayoutNode> nodeList = new ArrayList<>(layoutNodes.values());
        
        breakCycles(nodeList, statechart);
        assignLayers(nodeList);
        calculateXCoordinates(nodeList);
        return layoutNodes;
    }

    private void updateNeighbours(LayoutNode node) {
        node.removed = true;
        for (LayoutNode dest : node.outgoingNodes) {
            if (!dest.removed) dest.inDegree--;
        }
        for (LayoutNode source : node.incomingNodes) {
            if (!source.removed) source.outDegree--;
        }
    }

    // 1: Cycle Breaking
    private void breakCycles(List<LayoutNode> nodes, Statechart statechart) {
        int left = 1;
        int right = nodes.size();
        int remaining = nodes.size();

        while (remaining > 0) {
            boolean changed = true;
            while (changed) {
                changed = false;
                for (LayoutNode node : nodes) {
                    if (!node.removed) {
                        if (node.inDegree == 0) {
                            node.mark = left++;
                            updateNeighbours(node);
                            remaining--;
                            changed = true;
                        } else if (node.outDegree == 0) {
                            node.mark = right--;
                            updateNeighbours(node);
                            remaining--;
                            changed = true;
                        }
                    }
                }
            }

            if (remaining > 0) {
                LayoutNode bestNode = null;
                int maxOutflow = Integer.MIN_VALUE;
                for (LayoutNode node : nodes) {
                    if (!node.removed) {
                        int outflow = node.outDegree - node.inDegree;
                        if (outflow > maxOutflow) {
                            maxOutflow = outflow;
                            bestNode = node;
                        }
                    }
                }
                if (bestNode != null) {
                    bestNode.mark = left++;
                    updateNeighbours(bestNode);
                    remaining--;
                }
            }
        }
    }

    // 2: Layer Assignment (Y-Coordinate grouping)
    private void assignLayers(List<LayoutNode> nodes) {
        for (LayoutNode node : nodes) {
            node.layer = 0; 
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (LayoutNode source : nodes) {
                for (LayoutNode dest : source.outgoingNodes) {
                    if (source.mark < dest.mark) {
                        if (dest.layer < source.layer + 1) {
                            dest.layer = source.layer + 1;
                            changed = true;
                        }
                    }
                }
            }
        }
        System.out.println("Phase 2 Complete");
    }

    // 3 & 4: Vertex Ordering and X-Coordinate Assignment
    public void calculateXCoordinates(List<LayoutNode> nodes) {
        Map<Integer, List<LayoutNode>> rows = new HashMap<>();
        for (LayoutNode node : nodes) {
            rows.putIfAbsent(node.layer, new ArrayList<>());
            rows.get(node.layer).add(node);
        }
        
        for (Map.Entry<Integer, List<LayoutNode>> entry : rows.entrySet()) {
            List<LayoutNode> nodesInRow = entry.getValue();
            
            int currentX = 0; 
            for (LayoutNode node : nodesInRow) {
                node.x = currentX;
                currentX += 4; 
            }
        }
        
        System.out.println("Phase 3 & 4 Complete");
    }

    // testing
    public static void main(String[] args) {
        System.out.println("local testing");
        
        LayoutNode nodeA = new LayoutNode(null);
        LayoutNode nodeB = new LayoutNode(null);
        LayoutNode nodeC = new LayoutNode(null);
        
        nodeA.outDegree = 1; nodeA.inDegree = 1;
        nodeA.outgoingNodes.add(nodeB); nodeA.incomingNodes.add(nodeC);
        
        nodeB.outDegree = 1; nodeB.inDegree = 1;
        nodeB.outgoingNodes.add(nodeC); nodeB.incomingNodes.add(nodeA);
        
        nodeC.outDegree = 1; nodeC.inDegree = 1;
        nodeC.outgoingNodes.add(nodeA); nodeC.incomingNodes.add(nodeB);
        
        List<LayoutNode> fakeGraph = new ArrayList<>();
        fakeGraph.add(nodeA); fakeGraph.add(nodeB); fakeGraph.add(nodeC);
        
        LayoutEngine engine = new LayoutEngine();
        
        engine.breakCycles(fakeGraph, null);
        engine.assignLayers(fakeGraph);
        engine.calculateXCoordinates(fakeGraph);
        
        System.out.println("(X, Y):");
        System.out.println("Node A: (" + nodeA.x + ", " + -(nodeA.layer * 3) + ")"); 
        System.out.println("Node B: (" + nodeB.x + ", " + -(nodeB.layer * 3) + ")"); 
        System.out.println("Node C: (" + nodeC.x + ", " + -(nodeC.layer * 3) + ")"); 
    }
}