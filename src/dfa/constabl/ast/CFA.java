package constabl.ast;
import java.util.*;
import ast.*;
import constabl.ast.connectors.*;
public class CFA extends CodeNode{
    //Set<Edge> edges=new HashSet<Edge>();
    public CFA(String name){
         super(name);
        this.name=name;
    }
    String name;
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    Set<CFANode> nodes=new HashSet<CFANode>();
    CFANode start;
    CFANode end;
    // Connector prev=null;
    // public void addPrev(Connector c){
    //     this.prev=c;
    // }
    // public Connector getPrev(){
    //     return this.prev;
    // }
   
    public Set<CFANode> getSuccessors(CFANode a){
        Set<CFANode> returnnodes=new HashSet<CFANode>();
        for(CFANode n:nodes){
            if(n.prev!=null && n.prev.contains(a))
                returnnodes.add(n);
        }
        return returnnodes;
    }
    public Set<CFANode> getNodes() {
        return nodes;
    }
    public void setNodes(Set<CFANode> nodes) {
        this.nodes = nodes;
    }
    public void addNode(CFANode node){
        this.nodes.add(node);
    }
    public CFANode getStart() {
        return start;
    }
    public void setStart(CFANode start) {
        this.start = start;
    }
    public CFANode getEnd() {
        return end;
    }
    public void setEnd(CFANode end) {
        this.end = end;
    }
    public CFANode getFinalNode(){
        
        return this.end;
    }
    public String toString(){
        String str=" << CFA-"+this.name;
        // str+=" with Nodes : [";
        // for(CFANode n:nodes){
        //     str+=n+", ";
        // }
        // str+=" ]";
        str+=" with Prev : "+this.getPrevNodeNames()+">> ";
        //    n=n.next
        return str.replace("\n", "");
        
    }

}
