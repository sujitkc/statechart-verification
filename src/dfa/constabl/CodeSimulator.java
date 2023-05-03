package constabl;
import constabl.ast.connectors.*;
import constabl.ast.*;
import java.util.*;
import simulator.ExecuteStatement;
public class CodeSimulator extends Simulator{
    

   
    public CodeSimulator(List<CodeNode> codenodelist){
        super(codenodelist);
       // System.out.println("CodeNode list composed... cfa : " +getCFACount()+ " : fork : "+getForkCount()+" : Join : "+getJoinCount()+" : Seq :"+getSeqCount());

    }
    public void execute(CFA cfa){
        
        //printing current CFA list
        
        if(cfa!=null)
            executeCFA(cfa);
        List<CodeNode> nodelist=getNextNode(cfa);
        Connector con;
        
        //=getNextConnectorNode(cfa, forklist, joinlist, seqlist)
        if(nodelist!=null && nodelist.size()>0 && (con = (Connector)nodelist.get(0))!=null){
            if(con instanceof constabl.ast.connectors.Seq){
                //System.out.println("Seq found"); 
                //cfa=getNextCFAtoSeqConnector(cfalist,(Seq)con);
                nodelist=getNextNode((Seq)con);
                cfa=(CFA)nodelist.get(0);
                execute(cfa);
                
             }
             else if(con instanceof constabl.ast.connectors.Fork){
                //System.out.println("Fork found"); 
                //List<CFA> cfas=getNextCFAtoForkConnector(cfalist,(Fork)con);
                List<CodeNode> cfas=getNextNode((Fork)con);
                for(CodeNode concurrentcfa:cfas)
                    execute((CFA)concurrentcfa);
            }
            else if(con instanceof constabl.ast.connectors.Join){
                System.out.println("Join found");
            }

        }
    }

    public void executeCFA(CFA cfa){
        System.out.println("===============");
        System.out.println("Executing CFA : "+ cfa.toString().replace("\n", ""));
        CFANode node=cfa.getStart();
        //System.out.println("start node :"+node);
        while(cfa.getSuccessors(node)!=null&&cfa.getSuccessors(node).size()!=0){    
            //System.out.println("Printing successors : "+cfa.getSuccessors(node));                    
            node=cfa.getSuccessors(node).iterator().next();
            
            System.out.println("Executing  CFANode : {"+(node.toString()).replace("\n", "")+"}");

            //ExectueStatement.executeStatement(node.stmt);
        }
    }
    
}
