package constabl;
import java.util.*;
import ast.*;
import constabl.ast.*;
public abstract class StatementToCFA {
    
    public static CFA convertToCFA(Statement stmtList, String name){
        System.out.println ("converting statement to CFA : "+ name);
        CFA cfa=new CFA(name);
        CFANode prevloc=new CFANode("start-cfa-"+name,null);
        cfa.setStart(prevloc);
        //StmtList2CFA
        for(int i=0;i<((StatementList)stmtList).getStatements().size();i++){
            Statement stmt=((StatementList)stmtList).getStatements().get(i);
            //yet to do -- evaluate the statement and store program point
            CFANode nextloc=new CFANode(i+"",stmt);
            nextloc.addPrev(prevloc);
            cfa.addNode(nextloc);
//            Edge newEdge=new Edge("edge"+(i-1)+"_"+i, prevloc, nextloc, stmt);
            prevloc=nextloc;
        }
        cfa.setEnd(prevloc);
        return cfa;
    }
    
}
