package constablsim.ast;
import ast.*;
import java.util.*;
public class Node {
    public String name;
    public Statement stmt;
    public Set<Node> next=null;
    public Node(){}
    public Node(String name, Statement stmt){
        this.name=name;
        this.stmt=stmt;
    }
    public void addNext(Node node){
        if(this.next==null)
            next=new HashSet<Node>();
        this.next.add(node);
    }
    public Statement getStmt() {
        return stmt;
    }
    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toString(){
           return "( "+stmt+" ) -> "+ this.name;
    }
}
