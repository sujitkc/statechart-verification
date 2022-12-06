package constablsim;
import java.util.*;
import ast.*;
import constablsim.ast.*;
public abstract class StatementToCFA {
    
    public static CFA convertToCFA(Statement stmtList, String name){
        System.out.println ("converting statement list to CFA : "+ name);
        CFA cfa=new CFA(name);
        Node prevloc=new Node("start-cfa-"+name,null);
        cfa.setStart(prevloc);
        //StmtList2CFA
        for(int i=0;i<((StatementList)stmtList).getStatements().size();i++){
            Statement stmt=((StatementList)stmtList).getStatements().get(i);
            //yet to do -- evaluate the statement and store program point
            Node nextloc=new Node(i+"",stmt);
            nextloc.addPrev(prevloc);
            cfa.addNode(nextloc);
//            Edge newEdge=new Edge("edge"+(i-1)+"_"+i, prevloc, nextloc, stmt);
            prevloc=nextloc;
        }
        cfa.setEnd(prevloc);
        return cfa;
    }
    
}
