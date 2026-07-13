package transpiler;

import java.util.HashMap;
import java.util.Map;

import ast.*;
import layout.LayoutEngine;
import layout.LayoutNode;

public class TikzGenerator {

    private final Statechart statechart;
    private final StringBuilder tikzCode;
    private final Map<State, LayoutNode> nodeCoordinates;
    
    private final Map<String, Integer> edgeCounts = new HashMap<>();

    public TikzGenerator(Statechart statechart) {
        this.statechart = statechart;
        this.tikzCode = new StringBuilder();

        LayoutEngine engine = new LayoutEngine();
        this.nodeCoordinates = engine.calculateCoordinates(this.statechart);
    }

    public String generate() {
        rightTrackOffset = 1.0;
        leftTrackOffset = 1.0;

        rightLaneUsage = 0;
        leftLaneUsage = 0;

        loopCounter = 0;
        edgeCounts.clear();

        this.generatePrelude();
        this.tikzCode.append("\t\\begin{tikzpicture}[node distance=3cm, auto]\n");
        this.generateNodes(this.statechart);
        this.generateEdges(this.statechart);
        this.tikzCode.append("\t\\end{tikzpicture}\n");
        this.generateEpilogue();

        return this.tikzCode.toString();
    }

    private void generatePrelude() {
        this.tikzCode.append("\\documentclass[margin=1cm]{standalone}\n"
                            + "\\usepackage{tikz}\n"
                            + "\\usetikzlibrary{automata, positioning, calc}\n" 
                            + "\\begin{document}\n");
    }

    private void generateEpilogue() {
        this.tikzCode.append("\\end{document}\n");
    }

    private void generateNodes(State current) {
        String nodeID = current.getFullName().replace(".", "_");
        LayoutNode mathNode = this.nodeCoordinates.get(current);
        
        int xCoordinate = 0;
        int yCoordinate = 0;
        int boxWidth = 4;
        int boxHeight = 3;
        
        if (mathNode != null) {
            LayoutNode curr = mathNode;
            while (curr != null && curr.originalState != null) {
                xCoordinate += curr.x;
                yCoordinate -= curr.y;
                
                if (curr.parent != null && curr.parent.originalState != null) {
                    xCoordinate += 1;
                    yCoordinate -= 1;
                }
                curr = curr.parent;
            }
            
            boxWidth = mathNode.width;
            boxHeight = mathNode.height;
        }

        nodeID = current.getFullName().replace(".", "_"); 
        String nodeLabel = current.name.replace("_", "-");

        this.tikzCode.append("\t\t\\node[draw, rectangle, rounded corners, minimum width=" + boxWidth + "cm, minimum height=" + boxHeight + "cm, anchor=north west, label={[anchor=north west] north west:" + nodeLabel + "}] (" + nodeID + ") at (" + xCoordinate + ", " + yCoordinate + ") {};\n");

        if (current.states != null) {
            for (State substate : current.states) {
                generateNodes(substate);
            }
        }
    }

    private int loopCounter = 0;

    private final String[] loopDirs = {
        "above",
        "right",
        "below",
        "left"
    };

    private double rightTrackOffset = 1.0;
    private double leftTrackOffset  = 1.0;

    private int rightLaneUsage = 0;
    private int leftLaneUsage = 0;

    private String getExitAnchor(LayoutNode src, LayoutNode dst) {

        int dx = dst.x - src.x;
        int dy = dst.y - src.y;

        if (Math.abs(dx) >= Math.abs(dy)) {
            return dx >= 0 ? "east" : "west";
        }

        return dy >= 0 ? "south" : "north";
    }

    private String getEntryAnchor(LayoutNode src, LayoutNode dst) {

        int dx = dst.x - src.x;
        int dy = dst.y - src.y;

        if (Math.abs(dx) >= Math.abs(dy)) {
            return dx >= 0 ? "west" : "east";
        }

        return dy >= 0 ? "north" : "south";
    }

    private boolean isLongTransition(LayoutNode src,
                                     LayoutNode dst) {

        return Math.abs(src.layer - dst.layer) > 1;

    }

    private double calculateShift(int count) {

        if (count == 0)
            return 0;

        int multiplier = (count + 1) / 2;

        return (count % 2 == 0)
                ? -25.0 * multiplier
                : 25.0 * multiplier;

    }

    private String buildLabel(Transition transition)
            throws Exception {

        StringBuilder label = new StringBuilder();

        if (transition.trigger != null)
            label.append(transition.trigger);

        String guard = "";

        if (transition.guard != null) {

            TikzVisitor visitor = new TikzVisitor();

            visitor.dispatchExpression(transition.guard);

            guard = visitor.getOutput();

        }

        String action = "";

        if (transition.action != null &&
            !(transition.action instanceof SkipStatement)) {

            TikzVisitor visitor = new TikzVisitor();

            visitor.dispatchStatement(transition.action);

            action = visitor.getOutput().trim();

        }

        if (!guard.isEmpty() || !action.isEmpty()) {

            label.append(" {$");

            if (!guard.isEmpty())
                label.append("[").append(guard).append("]");

            if (!action.isEmpty()) {

                if (!guard.isEmpty())
                    label.append(" ");

                label.append("/ ").append(action);

            }

            label.append("$}");

        }

        return label.toString();

    }

    private void generateEdges(State current) {
        if (current.transitions != null) {
            try {

                for (Transition transition : current.transitions) {

                    State sourceState = transition.getSource();
                    State destState   = transition.getDestination();

                    String startID = sourceState.getFullName().replace(".", "_");
                    String endID   = destState.getFullName().replace(".", "_");

                    LayoutNode sourceNode = this.nodeCoordinates.get(sourceState);
                    LayoutNode destNode   = this.nodeCoordinates.get(destState);

                    String label = buildLabel(transition);

                    String edgeKey =(startID.compareTo(endID) < 0) ? startID + "-" + endID : endID + "-" + startID;

                    int count = edgeCounts.getOrDefault(edgeKey, 0);

                    edgeCounts.put(edgeKey, count + 1);

                    double shiftAmt = calculateShift(count);

                    if (sourceNode == null || destNode == null) {

                        tikzCode.append(String.format(
                            "\t\t\\draw[->, rounded corners=5pt] (%s.east) -- ++(1.5,0) |- node[pos=0.75,fill=white,inner sep=2pt] {%s} (%s.east);\n",
                            startID,
                            label,
                            endID
                        ));

                        continue;
                    }

                    int deltaX = destNode.x - sourceNode.x;
                    int deltaY = destNode.y - sourceNode.y;

                        if (startID.equals(endID)) {

                            String dir = loopDirs[loopCounter++ % loopDirs.length];

                            tikzCode.append(String.format(
                                "\t\t\\draw[->] (%s) edge[loop %s] node {%s} (%s);\n",
                                startID,
                                dir,
                                label,
                                endID
                            ));

                            continue;
                        }

                        if (isLongTransition(sourceNode, destNode)) {

                            boolean preferLeft;

                            if (deltaX < 0) {
                                preferLeft = true;
                            }
                            else if (deltaX > 0) {
                                preferLeft = false;
                            }
                            else {
                                preferLeft = leftLaneUsage <= rightLaneUsage;
                            }

                            if (preferLeft) {

                                leftLaneUsage++;

                                double lane = 4.0 + (leftLaneUsage * 0.8);

                                tikzCode.append(String.format(
                                    "\t\t\\draw[->, rounded corners=5pt] "
                                + "($(%s.west)+(0,%.1fpt)$) "
                                + "-- ++(-%.1f,0) "
                                + "|- node[pos=.75,fill=white,inner sep=2pt] {%s} "
                                + "($(%s.west)+(0,%.1fpt)$);\n",

                                    startID,
                                    shiftAmt,
                                    lane,
                                    label,
                                    endID,
                                    shiftAmt
                                ));

                            } else {

                                rightLaneUsage++;

                                double lane = 4.0 + (rightLaneUsage * 0.8);

                                tikzCode.append(String.format(
                                    "\t\t\\draw[->, rounded corners=5pt] "
                                + "($(%s.east)+(0,%.1fpt)$) "
                                + "-- ++(%.1f,0) "
                                + "|- node[pos=.75,fill=white,inner sep=2pt] {%s} "
                                + "($(%s.east)+(0,%.1fpt)$);\n",

                                    startID,
                                    shiftAmt,
                                    lane,
                                    label,
                                    endID,
                                    shiftAmt
                                ));

                            }

                            continue;
                        }
                        else if (Math.abs(deltaY) >= Math.abs(deltaX)) {
                            if (deltaY > 0) {
                                tikzCode.append(String.format(
                                    "\t\t\\draw[->, rounded corners=5pt] ($(%s.south) + (%.1fpt, 0)$) -- ++(0, -0.8) -| node[pos=0.25, fill=white, inner sep=2pt] {%s} ($(%s.north) + (%.1fpt, 0)$);\n", 
                                        startID, shiftAmt, label, endID, shiftAmt
                                ));
                            } else {
                                tikzCode.append(String.format(
                                    "\t\t\\draw[->, rounded corners=5pt] ($(%s.north) + (%.1fpt, 0)$) -- ++(0, 0.8) -| node[pos=0.25, fill=white, inner sep=2pt] {%s} ($(%s.south) + (%.1fpt, 0)$);\n", 
                                        startID, shiftAmt, label, endID, shiftAmt
                                ));
                            }
                        } else {
                            if (deltaX > 0) {
                                tikzCode.append(String.format(
                                    "\t\t\\draw[->, rounded corners=5pt] ($(%s.east) + (0, %.1fpt)$) -- ++(%.1f, 0) |- node[pos=0.75, fill=white, inner sep=2pt] {%s} ($(%s.west) + (0, %.1fpt)$);\n", 
                                        startID, shiftAmt, rightTrackOffset, label, endID, shiftAmt
                                ));
                                rightTrackOffset += 0.8; 
                            } else {
                                tikzCode.append(String.format(
                                    "\t\t\\draw[->, rounded corners=5pt] ($(%s.west) + (0, %.1fpt)$) -- ++(-%.1f, 0) |- node[pos=0.75, fill=white, inner sep=2pt] {%s} ($(%s.east) + (0, %.1fpt)$);\n", 
                                        startID, shiftAmt, leftTrackOffset, label, endID, shiftAmt
                                ));
                                leftTrackOffset += 0.8; 
                            }
                        }
                    
                }
        }
        catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        if (current.states != null) {
            for (State substate : current.states){
                generateEdges(substate);
        }
    }
    }
}