package constablsim;
import java.util.*;
import ast.*;
import constablsim.ast.*;
public abstract class StatementToCFA {
    
    public static CFA convertToCFA(Statement stmtList, CFA prevCFA){
        CFA cfa;

        //initialize prevloc
        
        Node prevloc;
        if(prevCFA==null){
            prevloc=new NullNode();
            cfa=new CFA();
            cfa.addNode(prevloc);
        }
        else{
            prevloc=prevCFA.getFinalNode();
            cfa=prevCFA;
        }        
        //StmtList2CFA
        for(int i=0;i<((StatementList)stmtList).getStatements().size();i++){
            Statement stmt=((StatementList)stmtList).getStatements().get(i);
            //yet to do -- evaluate the statement and store program point
            Node nextloc=new Node();
            cfa.addNode(nextloc);
            Edge newEdge=new Edge("edge"+(i-1)+"_"+i, prevloc, nextloc, stmt);
            cfa.addEdge(newEdge);
            prevloc=nextloc;

        }
        return cfa;
    }
    
}
