package constablsim;
import constablsim.ast.connectors.*;
import constablsim.ast.*;
import java.util.*;
public class CodeSimulator extends Simulator{
    public void execute(List<CFA> cfalist, List<Fork> forklist, List<Join> joinlist, List<Seq> seqlist){
        List<CFA> newcfalist=new ArrayList<CFA>();
        newcfalist.addAll(cfalist);
        //printing current CFA list
        System.out.println("Executing code... cfa : " +cfalist.size()+ " : fork : "+forklist.size()+" : Join : "+joinlist.size()+" : Seq :"+seqlist.size());
        Seq sq=getSeqfromList("start");
        CFA startCFA=getNextCFAtoSeqConnector(cfalist,sq);
        if(startCFA!=null)
            executeCFA(startCFA);
        newcfalist.remove(startCFA);
        CFA cfa=startCFA;
        Connector con;
        while((con=getNextConnectorNode(cfa, forklist, joinlist, seqlist))!=null){
            if(con instanceof constablsim.ast.connectors.Seq){
                System.out.println("Seq found"); 
                cfa=getNextCFAtoSeqConnector(cfalist,(Seq)con);
                executeCFA(cfa);
                newcfalist.remove(cfa);
             }
             else if(con instanceof constablsim.ast.connectors.Fork){
                System.out.println("Fork found");
                execute(newcfalist, forklist, joinlist, seqlist);
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
    public List<CFA> getAllSuccessorCFAtoForkConnector(List<CFA> cfalist, Fork sq){
        List<CFA> returnList=new ArrayList<CFA>();
        System.out.println("seq node : "+sq);
        for(CFA cfa: cfalist){
            //if CFA has an ancestor fork - then add it
            //how to find the ancestor?
            //first find the prev connector of the CFA 
                //if it is start node - exclude
                //if it is join node - exclude (because in one transition - both fork and join is not possible)
                // if it is a seq node -- then navigate to check if there is an edge between fork n seq
                // if it is a fork node -- then navigate to check if there is an edge between fork n seq
                Block prev=cfa.prev;
                while(prev!=null){
                    if(prev instanceof java.util.List){
                        System.out.println("multiple paths found - choosing first one");
                        prev=((java.util.List<Block>)prev).get(0);
                    }
                    if(prev instanceof constablsim.ast.connectors.Fork){
                        if(((constablsim.ast.connectors.Fork)prev).equals(sq)){
                        returnList.add(cfa);
                        prev=((constablsim.ast.connectors.Fork)prev).getPrev();
                        }
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
