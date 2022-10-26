package constabl.automaton;
import java.util.*;
import ast.*;
public abstract class StatementToCFA {
    
    public static CFA convertToCFA(StatementList stmtList){
        //initialize
        CFA cfa=new CFA();
        List<Edge> edges=new ArrayList<Edge>();
        List<Location> locations=new ArrayList<Location>();
        Location prevloc;
        Location nextloc;
        int i=1;

        //yet to do -- evaluate the statement and store program point
        prevloc=new Location("l"+i, null);
        locations.add(prevloc);
        //StmtList2CFA
        for(Statement stmt: stmtList.getStatements()){
            
            //yet to do -- evaluate the statement and store program point
            i++;
            nextloc=new Location("l"+i, null);
            locations.add(nextloc);
            Edge newEdge=new Edge("edge"+(i-1)+"_"+i, prevloc, nextloc, stmt);
            edges.add(newEdge);
            prevloc=nextloc;

        }

        // add all the edges and locations and return
        cfa.setEdges(edges);
        cfa.setLocations(locations);
        return cfa;
    }
}
