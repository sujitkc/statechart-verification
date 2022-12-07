package constablsim;
import constablsim.ast.connectors.*;
import constablsim.ast.*;
import java.util.*;
import ast.*;

public class Simulator {
     List<CFA> cfalist=new ArrayList<CFA>();
     List<Fork> forklist=new ArrayList<Fork>();
     List<Join> joinlist=new ArrayList<Join>();
     List<Seq> seqlist=new ArrayList<Seq>();
     public Simulator(){
       
    }
    public Simulator(Statechart sc){
       
    }
     public Simulator(List<CFA> cfalist, List<Fork> forklist, List<Join> joinlist, List<Seq> seqlist){
        this.cfalist.addAll(cfalist);
        this.forklist.addAll(forklist);
        this.joinlist.addAll(joinlist);
        this.seqlist.addAll(seqlist);
        
     }
     public void simulate(){}
     public Fork getForkfromList(String name){
          for(int i=0;i<forklist.size();i++){
              if(forklist.get(i).name.equals(name))
                  return forklist.get(i);
          }
          Fork f=new Fork(name);
          CFA cfa = getCFAfromList(name+"_N");
          f.setPrev(cfa);
          forklist.add(f);
          return f;
      }
      public Seq getSeqfromList(String name){
          for(int i=0;i<seqlist.size();i++){
              if(seqlist.get(i).name.equals(name))
                  return seqlist.get(i);
          }
          Seq s=new Seq(name);
          CFA cfa = getCFAfromList(name+"_N");
          s.setPrev(cfa);
          seqlist.add(s);
          return s;
      }
      public Join getJoinfromList(String name){
          for(int i=0;i<joinlist.size();i++){
              if(joinlist.get(i).name.equals(name))
                  return joinlist.get(i);
          }
          Join s=new Join(name);
          joinlist.add(s);
          return s;
      }
      public CFA getCFAfromList(String name){
          for(int i=0;i<cfalist.size();i++){
              if(cfalist.get(i).name.equals(name))
                  return cfalist.get(i);
          }
         
          return null;
      }
      public CFA getNextCFAtoSeqConnector(List<CFA> cfalist, Seq sq){
        
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
    }
    public State getShellAncestor(State s){
        if(s instanceof ast.Shell){
            return s;
        }
        else
            return getShellAncestor(s.getSuperstate());
    }
}

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
    //                 if(prev instanceof constablsim.ast.connectors.Fork){
    //                     if(((constablsim.ast.connectors.Fork)prev).equals(sq)){
    //                     returnList.add(cfa);
    //                     prev=((constablsim.ast.connectors.Fork)prev).getPrev();
    //                     }
    //                 }
                    
    //             }
    //     }
    //     return returnList;
    // }
