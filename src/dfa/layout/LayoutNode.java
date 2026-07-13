package layout;

import ast.State;
import java.util.ArrayList;
import java.util.List;

public class LayoutNode {
    public State originalState; 
    
    public int inDegree = 0;
    public int outDegree = 0;
    public int mark = 0;
    public boolean removed = false;
    
    public List<LayoutNode> incomingNodes = new ArrayList<>();
    public List<LayoutNode> outgoingNodes = new ArrayList<>();
    
    public int layer = -1;
    
    public double barycenter = 0.0;
    public int x = 0;
    public int y = 0;

    public int width = 4;  // Default width for a basic empty box
    public int height = 3; // Default height for a basic empty box
    public LayoutNode parent = null;
    public List<LayoutNode> children = new ArrayList<>();
    
    public LayoutNode(State originalState) {
        this.originalState = originalState;
    }
}