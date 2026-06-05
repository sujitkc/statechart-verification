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

        this.tikzCode.append("\\documentclass{article}\n"
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

        int yCoordinate;
        int xCoordinate;
        
        if (mathNode != null) {
            yCoordinate = -(mathNode.layer * 3);
            xCoordinate = mathNode.x;
        } else {
            yCoordinate = 0;
            xCoordinate = 0;
        }
        
        this.tikzCode.append("\t\t\\node[state] (" + nodeID + ") at (" + xCoordinate + ", " + yCoordinate + ") {" + current.name + "};\n");

        if (current.states != null) {

            for (State substate : current.states)
                generateNodes(substate);
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





