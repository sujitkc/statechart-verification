package constablsim;
import constablsim.ast.connectors.*;
import constablsim.ast.*;
import java.util.*;
public class CodeSimulator extends Simulator{
    public void execute(List<CFA> cfalist, List<Fork> forklist, List<Join> joinlist, List<Seq> seqlist){
        //printing current CFA list
        System.out.println("Executing code... cfa : " +cfalist.size()+ " : fork : "+forklist.size()+" : Join : "+joinlist.size()+" : Seq :"+seqlist.size());
        Seq sq=getSeqfromList("start");
        CFA startCFA=getNextCFAtoSeqConnector(cfalist,sq);
        if(startCFA!=null)
            executeCFA(startCFA);
        CFA cfa=startCFA;
        Connector con;
        while((con=getNextConnectorNode(cfa, forklist, joinlist, seqlist))!=null){
            if(con instanceof constablsim.ast.connectors.Seq){
                System.out.println("Seq found"); 
                cfa=getNextCFAtoSeqConnector(cfalist,(Seq)con);
                executeCFA(cfa);
             }
             else if(con instanceof constablsim.ast.connectors.Fork){
                System.out.println("Fork found");
            }
            else if(con instanceof constablsim.ast.connectors.Join){
                System.out.println("Join found");
            }

        }
    }
    public CFA getNextCFAtoSeqConnector(List<CFA> cfalist, Seq sq){
        
        System.out.println("seq node : "+sq);
        for(CFA cfa: cfalist){
            if(cfa.getPrev()!=null){
                // System.out.println("Equality C : "+(cfa.getPrev()).equals(sq));
                if((cfa.getPrev()).equals(sq)){
                    System.out.println("found a start cfa : "+cfa.name);
                    //System.out.println(cfa);
                    return cfa;
                }
            }
        }
        return null;
    }
    public List<CFA> getNextCFAtoForkConnector(List<CFA> cfalist, Fork sq){
        List<CFA> returnList=new ArrayList<CFA>();
        System.out.println("seq node : "+sq);
        for(CFA cfa: cfalist){
            if(cfa.getPrev()!=null){
                if((cfa.getPrev()).equals(sq)){
                    returnList.add(cfa);
                }
            }
        }
        return returnList;
    }
    public Connector getNextConnectorNode(CFA cfa, List<Fork> forklist, List<Join> joinlist, List<Seq> seqlist){
        for(Fork f: forklist){
            if(f.getPrev()!=null && f.getPrev().equals(cfa))
                return f;
        }
        for(Join j: joinlist){
            if(j.getPrev()!=null && j.getPrev().contains(cfa))
                return j;
        }
        for(Seq s: seqlist){
            if(s.getPrev()!=null && s.getPrev().equals(cfa))
                return s;
        }
        return null;
    }
    public void executeCFA(CFA cfa){
        System.out.println("execute CFA : "+cfa.name);
        Node node=cfa.getStart();
        System.out.println("start node :"+node);
        while(cfa.getSuccessors(node)!=null&&cfa.getSuccessors(node).size()!=0){    
            //System.out.println("Printing successors : "+cfa.getSuccessors(node));                    
            node=cfa.getSuccessors(node).iterator().next();
            System.out.println(node);
        }
    }

}
