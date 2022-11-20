package constablsim;
import java.util.*;
import ast.*;
import constablsim.ast.*;
public class CFA {
    Set<Edge> edges=new HashSet<Edge>();
    Set<Node> nodes=new HashSet<Node>();
    Node start;
    Node end;
    public void addSuccessor(Node a, Node b, Statement stmt){
        Edge e=new Edge(a.name+"-"+b.name, a, b, stmt);
        edges.add(e);
    }
    public Set<Edge> getSuccessors(Node a){
        Set<Edge> returnSet=new HashSet<Edge>();
        for(Edge e:edges){
            if(e.getSource()==a)
                returnSet.add(e);
        }
        return returnSet;
    }
    public Set<Edge> getEdges() {
        return edges;
    }
    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }
    public void addEdge(Edge edge){
        this.edges.add(edge);
    }
    public Set<Node> getNodes() {
        return nodes;
    }
    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
    public void addNode(Node node){
        this.nodes.add(node);
    }
    public Node getStart() {
        return start;
    }
    public void setStart(Node start) {
        this.start = start;
    }
    public Node getEnd() {
        return end;
    }
    public void setEnd(Node end) {
        this.end = end;
    }
    public Node getFinalNode(){
        Node finalnode=new Node();
        for(Edge e:edges){
            
        }
        return finalnode;
    }

}
