package transpiler;

import java.util.Map;

import ast.*;
import transpiler.TikzVisitor;
import layout.LayoutEngine;
import layout.LayoutNode;

public class TikzGenerator {

    private final Statechart statechart;
    private final StringBuilder tikzCode;
    private final Map<State, LayoutNode> nodeCoordinates;

    public TikzGenerator(Statechart statechart) {

        this.statechart = statechart;
        this.tikzCode = new StringBuilder();

        LayoutEngine engine = new LayoutEngine();
        this.nodeCoordinates = engine.calculateCoordinates(this.statechart);
    }

    public String generate() {

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
                            + "\\usetikzlibrary{automata, positioning}\n"
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

        this.tikzCode.append("\t\t\\node[draw, rectangle, rounded corners, minimum width=" + boxWidth + "cm, minimum height=" + boxHeight + "cm, anchor=north west, label={[anchor=north west] north west:" + current.name + "}] (" + nodeID + ") at (" + xCoordinate + ", " + yCoordinate + ") {};\n");

        if (current.states != null) {
            for (State substate : current.states) {
                generateNodes(substate);
            }
        }
    }


    private void generateEdges(State current) {

        if (current.transitions != null) {

            try {

                for (Transition transition : current.transitions) {

                    String startID = transition.getSource().getFullName().replace(".", "_");
                    String endID = transition.getDestination().getFullName().replace(".", "_");

                    String guardStr = "";
                    if (transition.guard != null) {

                        TikzVisitor visitor = new TikzVisitor();
                        visitor.dispatchExpression(transition.guard);
                        guardStr = "[" + visitor.getOutput() + "]";
                    }

                    String actionStr = "";
                    if (transition.action != null && !(transition.action instanceof SkipStatement)) {

                        TikzVisitor visitor = new TikzVisitor();
                        visitor.dispatchStatement(transition.action);
                        String rawAction = visitor.getOutput().trim();
                        if (!rawAction.isEmpty()) {
                            actionStr = " / " + rawAction;
                        }
                    }

                    String label = transition.trigger;
                    if (!guardStr.isEmpty() && !actionStr.isEmpty())
                        label += " {$" + guardStr + " " + actionStr + "$}";

                    tikzCode.append("\t\t\\draw[->] (" + startID + ") edge node  {" + label + "} (" + endID + ");\n");
                }

            } catch (Exception e) {

                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        if (current.states != null) {

            for (State substate : current.states)
                generateEdges(substate);
        }
    }
}