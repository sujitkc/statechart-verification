package constabl.automaton;
import java.util.*;
import ast.*;
public class StatementToCFA {
    
    public CFA convertToCFA(StatementList stmtList){
        //initialize
        CFA cfa=new CFA();
        List<Edge> edges=new ArrayList<Edge>();
        List<Location> locations=new ArrayList<Location>();
        Location loc1;
        Location loc2;
        int i=1;
        //StmtList2CFA
        for(Statement stmt: stmtList.getStatements()){
            
            loc1=new Location("l"+i, null);
            
        }


        // add all the edges and locations and return
        cfa.setEdges(edges);
        cfa.setLocations(locations);
        return cfa;
    }
}
