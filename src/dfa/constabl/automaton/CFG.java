package constabl.automaton;
import ast.*;
import java.util.*;
public class CFG{
/*     public Statement stmt;
    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
    public void setNext(Graph next) {
        this.next = next;
    }
    public Statement getStmt() {
        return stmt;
    }
    public Graph getNext() {
        return next;
    }
    public Graph next=null;
    public List<Statement> getReadyStatement(List<Statement> currentlist){
        Statement current=null;
        if(currentlist.size()==1)
            current=currentlist.get(0);
        List<Statement> returnList=new ArrayList<Statement>();
        if(stmt instanceof StatementList){
            List<Statement> stmtlist=((StatementList)stmt).getStatements();
            int currentIndex=stmtlist.indexOf(current);
            int nextIndex=currentIndex+1;
            if(nextIndex<stmtlist.size()){
                if(next!=null){
                    return next.getReadyStatement(currentlist);
                }
                else
                    return null;
            }else{
                returnList.add(stmtlist.get(nextIndex));
                return returnList;
            }
        }
        else{
            return null;
        }
    } */
}
