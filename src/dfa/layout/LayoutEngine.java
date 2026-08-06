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

    private List<LayoutNode> getPathToRoot(LayoutNode node) { 
        List<LayoutNode> path = new ArrayList<>(); 
        LayoutNode current = node; 
        while (current != null) { 
            path.add(current); current = current.parent; 
        } 
        Collections.reverse(path); 
        return path;
    }

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
        
        List<Transition> allTransitions = new ArrayList<>();
        collectAllTransitions(statechart, allTransitions);
        
        for (Transition t : allTransitions) {
            LayoutNode sourceNode = layoutNodes.get(t.getSource());
            LayoutNode destNode = layoutNodes.get(t.getDestination());
            
            if (sourceNode != null && destNode != null) {
                sourceNode.outDegree++;
                sourceNode.outgoingNodes.add(destNode);
                
                destNode.inDegree++;
                destNode.incomingNodes.add(sourceNode);
            }
        }
        
        processHierarchy(rootNode);
        
        return layoutNodes;
    }

    private void collectAllTransitions(State currentState, List<Transition> allTransitions) {
        if (currentState.transitions != null) {
            allTransitions.addAll(currentState.transitions);
        }
        if (currentState.states != null) {
            for (State childState : currentState.states) {
                collectAllTransitions(childState, allTransitions);
            }
        }
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
            for (int i = 0; i < 4; i++) {
                reduceCrossings(parent.children);
            }
            
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
            
            int horizontalPadding = Math.max(4, parent.children.size() / 2);
            int verticalPadding = Math.max(2, parent.children.size() / 3);

            parent.width = maxWidth + horizontalPadding * 2;
            parent.height = maxHeight + verticalPadding;
        }
    }

    private void convertToAbsoluteCoordinates(LayoutNode node, int parentAbsoluteX, int parentAbsoluteY) {
        node.x += parentAbsoluteX;
        node.y += parentAbsoluteY;
        
        for (LayoutNode child : node.children) {
            convertToAbsoluteCoordinates(child, node.x, node.y);
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
            List<LayoutNode> previousLayer = layers.get(i - 1);
            if (currentLayer == null || previousLayer == null) continue;

            for (LayoutNode node : currentLayer) {
                double sum = 0;
                int count = 0;
                for (LayoutNode parentNode : node.incomingNodes) {
                    if (previousLayer.contains(parentNode)) {
                        sum += previousLayer.indexOf(parentNode); 
                        count++;
                    }
                }
                node.barycenter = (count == 0) ? 0 : (sum / count);
            }

            currentLayer.sort((a, b) -> Double.compare(a.barycenter, b.barycenter));
        }

        nodes.clear();
        for (int i = 0; i <= maxLayer; i++) {
            if (layers.containsKey(i)) {
                nodes.addAll(layers.get(i));
            }
        }
    }

    private void calculateXCoordinates(List<LayoutNode> nodes) {
        Map<Integer, List<LayoutNode>> rows = new HashMap<>();
        for (LayoutNode node : nodes) {
            rows.putIfAbsent(node.layer, new ArrayList<>());
            rows.get(node.layer).add(node);
        }
        
        for (Map.Entry<Integer, List<LayoutNode>> entry : rows.entrySet()) {
            List<LayoutNode> nodesInRow = entry.getValue();
            
            int currentX = 4; 
            for (LayoutNode node : nodesInRow) {
                node.x = currentX;
                int spacing = Math.max(2, Math.max(node.outDegree, node.inDegree));

        currentX += node.width + spacing;
            }
        }
    }

    private void calculateYCoordinates(List<LayoutNode> nodes) {
        Map<Integer, List<LayoutNode>> rows = new HashMap<>();
        int maxLayer = 0;
        for (LayoutNode node : nodes) {
            rows.putIfAbsent(node.layer, new ArrayList<>());
            rows.get(node.layer).add(node);
            if (node.layer > maxLayer) maxLayer = node.layer;
        }

        int currentY = 3; 
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
            int rowSpacing = Math.max(2, row.size() / 2);

        currentY += tallestInRow + rowSpacing;
        }
    }
}
