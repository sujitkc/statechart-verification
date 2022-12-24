package constabl.ast;
import ast.*;
import java.util.*;
public class CFANode{
    public String name;
    public Statement stmt;
    public Set<CFANode> prev=null;
    public CFANode(){}
    public CFANode(String name, Statement stmt){
        
        this.name=name;
        this.stmt=stmt;
    }
    public void addPrev(CFANode node){
        if(this.prev==null)
            prev=new HashSet<CFANode>();
        this.prev.add(node);
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
