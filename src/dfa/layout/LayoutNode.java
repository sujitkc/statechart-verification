package layout;

import ast.State;
import java.util.ArrayList;
import java.util.List;

public class LayoutNode {
    public State originalState; 
    
    // Phase 1 Variables: Cycle Breaking
    public int inDegree = 0;
    public int outDegree = 0;
    public int mark = 0;
    public boolean removed = false;
    
    public List<LayoutNode> incomingNodes = new ArrayList<>();
    public List<LayoutNode> outgoingNodes = new ArrayList<>();
    
    // Phase 2 Variables: Layer Assignment (Y-Coordinate)
    public int layer = -1;
    
    // Future Variables: X-Coordinate
    public double barycenter = 0.0;
    public int x = 0;
    public int y = 0;
    
    public LayoutNode(State originalState) {
        this.originalState = originalState;
    }
}