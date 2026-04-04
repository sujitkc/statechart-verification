package constabl;
import constabl.ast.connectors.*;
import constabl.ast.*;
import java.util.*;
import ast.*;

public class Simulator {
     
     List<CodeNode> codenodelist=new ArrayList<CodeNode>();
     List<CodeNode> execnodelist=new ArrayList<CodeNode>();
     public Simulator(){
        //System.out.println("inside default constructor");
        
    }
    public Simulator(Statechart sc){
        
    }
    
    public Simulator(List<CodeNode> codenodelist){
        //System.out.println("inside codenodelist constructor");
        this.codenodelist.addAll(codenodelist);
        
     }
     
     public void simulate(){
        // This has to toggle between statechart simulator and code simulator
        // what shd it do?!
        //This mainta

     }
     public int getJoinCount(){
        int count=0;
        for(CodeNode c: codenodelist){
            if(c instanceof constabl.ast.connectors.Join)
                count++;
        }
        return count;
     }
     public int getForkCount(){
        int count=0;
        for(CodeNode c: codenodelist){
            if(c instanceof constabl.ast.connectors.Fork)
                count++;
        }
        return count;
     }
     public int getSeqCount(){
        int count=0;
        for(CodeNode c: codenodelist){
            if(c instanceof constabl.ast.connectors.Seq)
                count++;
        }
        return count;
     }
     public int getCFACount(){
        int count=0;
        for(CodeNode c: codenodelist){
            if(c instanceof constabl.ast.CFA)
                count++;
        }
        return count;
     }
     public Fork getForkfromList(String name){
          for(int i=0;i<codenodelist.size();i++){
              if(codenodelist.get(i) instanceof constabl.ast.connectors.Fork && (codenodelist.get(i)).name.equals(name))
                  return (Fork)codenodelist.get(i);
          }
          Fork f=new Fork(name);
          CFA cfa = getCFAfromList(name+"_N");
          f.addPrev(cfa);
          codenodelist.add(f);
          return f;
      }

      public Seq getSeqfromList(String name){
          for(int i=0;i<codenodelist.size();i++){
              if(codenodelist.get(i) instanceof constabl.ast.connectors.Seq && (codenodelist.get(i)).name.equals(name))
                  return (Seq)codenodelist.get(i);
          }
          Seq s=new Seq(name);
          CFA cfa = getCFAfromList(name+"_N");
          s.addPrev(cfa);
          codenodelist.add(s);
          return s;
      }
      public Join getJoinfromList(String name){
          for(int i=0;i<codenodelist.size();i++){
              if(codenodelist.get(i) instanceof constabl.ast.connectors.Join && (codenodelist.get(i)).name.equals(name))
                  return (Join)codenodelist.get(i);
          }
          Join s=new Join(name);
          codenodelist.add(s);
          return s;
      }
      public CFA getCFAfromList(String name){
          for(int i=0;i<codenodelist.size();i++){
              if(codenodelist.get(i) instanceof constabl.ast.CFA && (codenodelist.get(i)).name.equals(name))
                  return (CFA)codenodelist.get(i);
          }
         
          return null;
      }
     
    public List<CodeNode> getNextNode(CodeNode currentNode){
        List<CodeNode> returnList=new ArrayList<CodeNode>();
       // System.out.println("current node : "+currentNode);
        for(CodeNode codenode: codenodelist){
            if(codenode.getPrev()!=null){
                if((codenode.getPrev()).contains(currentNode)){
                    returnList.add(codenode);
                }
            }
        }
        return returnList;
    }
    public State getShellAncestor(State s){
        if(s instanceof ast.Shell){
            return s;
        }
        else
            return getShellAncestor(s.getSuperstate());
    }
}
/* public CFA getNextCFAtoSeqConnector(List<CFA> cfalist, Seq sq){
        
        System.out.println("seq node : "+sq.name);
        for(CFA cfa: cfalist){
            if(cfa.getPrev()!=null){
                // System.out.println("Equality C : "+(cfa.getPrev()).equals(sq));
                if((cfa.getPrev()).equals(sq)){
                    //System.out.println("found a start cfa : "+cfa.name);
                    //System.out.println(cfa);
                    return cfa;
                }
            }
        }
        return null;
    }
    public List<CFA> getNextCFAtoForkConnector(List<CFA> cfalist, Fork sq){
        List<CFA> returnList=new ArrayList<CFA>();
        System.out.println("fork node : "+sq);
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
    }*/

// public List<CFA> getAllSuccessorCFAtoForkConnector(List<CFA> cfalist, Fork sq){
    //     List<CFA> returnList=new ArrayList<CFA>();
    //     System.out.println("seq node : "+sq);
    //     for(CFA cfa: cfalist){
    //         //if CFA has an ancestor fork - then add it
    //         //how to find the ancestor?
    //         //first find the prev connector of the CFA 
    //             //if it is start node - exclude
    //             //if it is join node - exclude (because in one transition - both fork and join is not possible)
    //             // if it is a seq node -- then navigate to check if there is an edge between fork n seq
    //             // if it is a fork node -- then navigate to check if there is an edge between fork n seq
    //             Block prev=cfa.prev;
    //             while(prev!=null){
    //                 if(prev instanceof java.util.List){
    //                     System.out.println("multiple paths found - choosing first one");
    //                     prev=((java.util.List<Block>)prev).get(0);
    //                 }
    //                 if(prev instanceof constabl.ast.connectors.Fork){
    //                     if(((constabl.ast.connectors.Fork)prev).equals(sq)){
    //                     returnList.add(cfa);
    //                     prev=((constabl.ast.connectors.Fork)prev).getPrev();
    //                     }
    //                 }
                    
    //             }
    //     }
    //     return returnList;
    // }
