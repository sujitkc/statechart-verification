package constablsim;
import java.util.*;
import ast.*;
import constablsim.ast.*;
import constablsim.ast.connectors.*;
public class CFA {
    //Set<Edge> edges=new HashSet<Edge>();
    public CFA(String name){
        this.name=name;
    }
    String name;
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    Set<Node> nodes=new HashSet<Node>();
    Node start;
    Node end;
    Connector prev=null;
    public void addPrev(Connector c){
        this.prev=c;
    }
    public Connector getPrev(){
        return this.prev;
    }
    public void addPredecessor(Node a, Node b){
        a.addPrev(b);
    }
    public Set<Node> getSuccessors(Node a){
        Set<Node> returnnodes=new HashSet<Node>();
        for(Node n:nodes){
            if(n.prev!=null && n.prev.contains(a))
                returnnodes.add(n);
        }
        return returnnodes;
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
        
        return this.end;
    }
    public String toString(){
        String str=" CFA : "+this.name+" : Prev : "+this.prev+" : Nodes : ";
        for(Node n:nodes){
            str+=n;
        }
        
        //    n=n.next
        return str;
        
    }

}
