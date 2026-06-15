package layout;

import ast.Statechart;
import ast.State;
import ast.Transition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutEngine {

    public Map<State, LayoutNode> layoutNodes = new HashMap<>();

    public Map<State, LayoutNode> calculateCoordinates(Statechart statechart) {
        layoutNodes.clear();
        
        LayoutNode rootNode = new LayoutNode(statechart); 
        layoutNodes.put(statechart, rootNode);
        if (statechart.states != null) {
            for (State s : statechart.states) {
                LayoutNode childNode = buildHierarchy(s, rootNode);
                rootNode.children.add(childNode);
            }
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
        
        processHierarchy(rootNode);
        return layoutNodes;
    }

    private LayoutNode buildHierarchy(State currentState, LayoutNode parentNode) {
        LayoutNode node = new LayoutNode(currentState);
        node.parent = parentNode;
        layoutNodes.put(currentState, node);

        if (currentState.states != null) {
            for (State childState : currentState.states) {
                LayoutNode childNode = buildHierarchy(childState, node);
                node.children.add(childNode);
            }
        }
        return node;
    }

    private void processHierarchy(LayoutNode parent) {
        for (LayoutNode child : parent.children) {
            if (!child.children.isEmpty()) {
                processHierarchy(child);
            }
        }

        if (!parent.children.isEmpty()) {
            
            breakCycles(parent.children);
            assignLayers(parent.children);
            reduceCrossings(parent.children);
            
            calculateYCoordinates(parent.children);
            calculateXCoordinates(parent.children);
            
            int maxWidth = 0;
            int maxHeight = 0;
            
            for (LayoutNode child : parent.children) {
                int childRightEdge = child.x + child.width;
                if (childRightEdge > maxWidth) {
                    maxWidth = childRightEdge;
                }
                
                int childBottomEdge = child.y + child.height; 
                if (childBottomEdge > maxHeight) {
                    maxHeight = childBottomEdge;
                }
            }
            
            parent.width = maxWidth + 2;
            parent.height = maxHeight + 2;
        }
    }

    private void breakCycles(List<LayoutNode> nodes) {
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

    private void updateNeighbours(LayoutNode node) {
        node.removed = true;
        for (LayoutNode dest : node.outgoingNodes) {
            if (!dest.removed) dest.inDegree--;
        }
        for (LayoutNode source : node.incomingNodes) {
            if (!source.removed) source.outDegree--;
        }
    }

    private void assignLayers(List<LayoutNode> nodes) {
        for (LayoutNode node : nodes) {
            node.layer = 0; 
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (LayoutNode source : nodes) {
                for (LayoutNode dest : source.outgoingNodes) {
                    if (nodes.contains(dest) && source.mark < dest.mark) {
                        if (dest.layer < source.layer + 1) {
                            dest.layer = source.layer + 1;
                            changed = true;
                        }
                    }
                }
            }
        }
    }

    // Phase 3 - Crossing Reduction (The Barycenter Method)
    private void reduceCrossings(List<LayoutNode> nodes) {
        Map<Integer, List<LayoutNode>> layers = new HashMap<>();
        int maxLayer = 0;
        
        for (LayoutNode node : nodes) {
            layers.putIfAbsent(node.layer, new ArrayList<>());
            layers.get(node.layer).add(node);
            if (node.layer > maxLayer) maxLayer = node.layer;
        }

        for (int i = 1; i <= maxLayer; i++) {
            List<LayoutNode> currentLayer = layers.get(i);
            if (currentLayer == null) continue;

            for (LayoutNode node : currentLayer) {
                double sum = 0;
                int count = 0;
                for (LayoutNode parentNode : node.incomingNodes) {
                    if (nodes.contains(parentNode) && parentNode.layer == i - 1) {
                        sum += parentNode.x; 
                        count++;
                    }
                }
                node.barycenter = (count == 0) ? 0 : (sum / count);
            }

            currentLayer.sort((a, b) -> Double.compare(a.barycenter, b.barycenter));
        }
    }

    // Phase 4 - Coordinate Assignment
    private void calculateXCoordinates(List<LayoutNode> nodes) {
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
            }
        }
    }

    // Phase 2.5 - Dynamic Y-Coordinate Assignment
    private void calculateYCoordinates(List<LayoutNode> nodes) {
        Map<Integer, List<LayoutNode>> rows = new HashMap<>();
        int maxLayer = 0;
        for (LayoutNode node : nodes) {
            rows.putIfAbsent(node.layer, new ArrayList<>());
            rows.get(node.layer).add(node);
            if (node.layer > maxLayer) maxLayer = node.layer;
        }

        int currentY = 0;
        for (int i = 0; i <= maxLayer; i++) {
            List<LayoutNode> row = rows.get(i);
            if (row == null) continue;

            int tallestInRow = 0;
            for (LayoutNode node : row) {
                node.y = currentY;
                if (node.height > tallestInRow) {
                    tallestInRow = node.height;
                }
            }
            currentY += tallestInRow + 2; 
        }
    }
}
