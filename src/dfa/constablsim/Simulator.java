package constablsim;
import constablsim.ast.connectors.*;
import java.util.*;
public class Simulator {
     List<CFA> cfalist=new ArrayList<CFA>();
     List<Fork> forklist=new ArrayList<Fork>();
     List<Join> joinlist=new ArrayList<Join>();
     List<Seq> seqlist=new ArrayList<Seq>();
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
}
