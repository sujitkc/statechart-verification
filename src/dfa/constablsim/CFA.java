package constablsim;
import java.util.*;
import ast.*;
import constablsim.ast.*;
public class CFA {
    //Set<Edge> edges=new HashSet<Edge>();
    Set<Node> nodes=new HashSet<Node>();
    Node start;
    Node end;
    public void addSuccessor(Node a, Node b){
        a.addNext(b);
    }
    public Set<Node> getSuccessors(Node a){
        return a.next;
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
        String str="";
        for(Node n:nodes){
            str+=n;
        }
        
        //    n=n.next
        return str;
        
    }

}
