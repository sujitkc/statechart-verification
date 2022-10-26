package constabl.automaton;
import ast.*;
public class Edge {
    String name;
    Location source;
    Location dest;
    Statement stmt; // can be expression statement in case of edges with guards
    public Edge(String name, Location source, Location dest, Statement stmt) {
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
    public Location getSource() {
        return source;
    }
    public void setSource(Location source) {
        this.source = source;
    }
    public Location getDest() {
        return dest;
    }
    public void setDest(Location dest) {
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
