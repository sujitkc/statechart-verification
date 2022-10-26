package constabl.automaton;
import ast.*;
import java.util.*;
public class AutomatonSimulator {
    List<CFA> cfalist;
    List<StatementList> blocklist;
    public AutomatonSimulator(){
        cfalist=new ArrayList<CFA>();
        blocklist=new ArrayList<StatementList>();
        for(StatementList block:blocklist){
            CFA cfa=StatementToCFA.convertToCFA(block);
            cfalist.add(cfa);
        }
    }

}
