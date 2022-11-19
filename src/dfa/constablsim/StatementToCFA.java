package constablsim;
import java.util.*;
import ast.*;
import constablsim.ast.*;
public abstract class StatementToCFA {
    
    public static CFA convertToCFA(StatementList stmtList){
        //initialize
        CFA cfa=new CFA();
        Set<Edge> edges=new HashSet<Edge>();
        Set<Node> nodes=new HashSet<Node>();
        Node prevloc;
        Node nextloc;
        int i=1;

        //yet to do -- evaluate the statement and store program point
        prevloc=new Node();
        nodes.add(prevloc);
        //StmtList2CFA
        for(Statement stmt: stmtList.getStatements()){
            
            //yet to do -- evaluate the statement and store program point
            i++;
            nextloc=new Node();
            nodes.add(nextloc);
            Edge newEdge=new Edge("edge"+(i-1)+"_"+i, prevloc, nextloc, stmt);
            edges.add(newEdge);
            prevloc=nextloc;

        }

        // add all the edges and locations and return
        cfa.setEdges(edges);
        cfa.setNodes(nodes);
        return cfa;
    }
}
