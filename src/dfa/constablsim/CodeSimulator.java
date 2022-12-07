package constablsim;
import constablsim.ast.connectors.*;
import constablsim.ast.*;
import java.util.*;
public class CodeSimulator extends Simulator{
    

    public CodeSimulator(List<CFA> cfalist, List<Fork> forklist, List<Join> joinlist, List<Seq> seqlist){
                super(cfalist,forklist,joinlist,seqlist);
                System.out.println("CFA list composed... cfa : " +cfalist.size()+ " : fork : "+forklist.size()+" : Join : "+joinlist.size()+" : Seq :"+seqlist.size());

    }
    public void execute(CFA cfa){
        
        //printing current CFA list
        
        if(cfa!=null)
            executeCFA(cfa);
        Connector con;
        if((con=getNextConnectorNode(cfa, forklist, joinlist, seqlist))!=null){
            if(con instanceof constablsim.ast.connectors.Seq){
                //System.out.println("Seq found"); 
                cfa=getNextCFAtoSeqConnector(cfalist,(Seq)con);
                execute(cfa);
                
             }
             else if(con instanceof constablsim.ast.connectors.Fork){
                //System.out.println("Fork found"); 
                List<CFA> cfas=getNextCFAtoForkConnector(cfalist,(Fork)con);
                for(CFA concurrentcfa:cfas)
                    execute(concurrentcfa);
            }
            else if(con instanceof constablsim.ast.connectors.Join){
                System.out.println("Join found");
            }

        }
    }

    public void executeCFA(CFA cfa){
        System.out.println("===============");
        System.out.println("Executing CFA : "+ cfa.toString().replace("\n", ""));
        Node node=cfa.getStart();
        //System.out.println("start node :"+node);
        while(cfa.getSuccessors(node)!=null&&cfa.getSuccessors(node).size()!=0){    
            //System.out.println("Printing successors : "+cfa.getSuccessors(node));                    
            node=cfa.getSuccessors(node).iterator().next();
            System.out.println("Executing  Node : {"+(node.toString()).replace("\n", "")+"}");
        }
    }

}
