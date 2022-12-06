package constablsim;
import java.util.*;
import ast.*;
import constablsim.ast.*;
public abstract class StatementToCFA {
    
    public static CFA convertToCFA(Statement stmtList, String name){
        CFA cfa=new CFA(name);
        Node prevloc=null;
        //StmtList2CFA
        for(int i=0;i<((StatementList)stmtList).getStatements().size();i++){
            Statement stmt=((StatementList)stmtList).getStatements().get(i);
            //yet to do -- evaluate the statement and store program point
            Node nextloc=new Node(i+"",stmt);
            cfa.addNode(nextloc);
//            Edge newEdge=new Edge("edge"+(i-1)+"_"+i, prevloc, nextloc, stmt);
            prevloc=nextloc;
        }
        cfa.setEnd(prevloc);
        return cfa;
    }
    
}
