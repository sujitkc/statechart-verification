package constablsim.ast;
import ast.*;
public class Edge {
    String name;
    Node source;
    Node dest;
    Statement stmt; // can be expression statement in case of edges with guards
    public Edge(String name, Node source, Node dest, Statement stmt) {
        this.name = name;
        this.source = source;
        this.dest = dest;
        this.stmt = stmt;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Node getSource() {
        return source;
    }
    public void setSource(Node source) {
        this.source = source;
    }
    public Node getDest() {
        return dest;
    }
    public void setDest(Node dest) {
        this.dest = dest;
    }
    public Statement getStmt() {
        return stmt;
    }
    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
    public String toString(){
        String str="";
        str+=name+" : "+source+"---"+stmt+"---->"+dest;
        return str;
    }
}
