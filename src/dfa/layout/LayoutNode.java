package layout;

import ast.State;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class LayoutNode {

    public State originalState;

    // Graph information
    public int inDegree = 0;
    public int outDegree = 0;

    public int externalOut = 0;
    public int externalIn = 0;

    public int mark = 0;
    public boolean removed = false;

    public List<LayoutNode> incomingNodes = new ArrayList<>();
    public List<LayoutNode> outgoingNodes = new ArrayList<>();

    // Sugiyama layer
    public int layer = -1;

    public double barycenter = 0;

    // Position
    public int x = 0;
    public int y = 0;

    // Size
    public int width = 4;
    public int height = 3;

    // Hierarchy
    public LayoutNode parent = null;
    public List<LayoutNode> children = new ArrayList<>();

    public Port northPort;
    public Port southPort;
    public Port eastPort;
    public Port westPort;

    public List<Route> outgoingRoutes = new ArrayList<>();
    public List<Route> incomingRoutes = new ArrayList<>();

    public LayoutNode(State originalState) {
        this.originalState = originalState;

        northPort = new Port(this, Side.NORTH);
        southPort = new Port(this, Side.SOUTH);
        eastPort = new Port(this, Side.EAST);
        westPort = new Port(this, Side.WEST);
    }

    public enum Side {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public static class Port {

        public LayoutNode owner;
        public Side side;

        public int offset = 0;

        public Port(LayoutNode owner, Side side) {
            this.owner = owner;
            this.side = side;
        }
    }

    public static class Route {

        public LayoutNode source;
        public LayoutNode destination;

        public Port sourcePort;
        public Port destinationPort;

        public List<Point> bendPoints = new ArrayList<>();

        public int channel = -1;

        public Route(LayoutNode source,
                     LayoutNode destination,
                     Port sourcePort,
                     Port destinationPort) {

            this.source = source;
            this.destination = destination;
            this.sourcePort = sourcePort;
            this.destinationPort = destinationPort;
        }

        public void addBend(int x, int y) {
            bendPoints.add(new Point(x, y));
        }
    }
}
