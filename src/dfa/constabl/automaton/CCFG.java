package constabl.automaton;

import java.util.*;
import ast.*;
public class CCFG implements Graph{
    public List<CFG> getCfglist() {
        return cfglist;
    }
    public void setCfglist(List<CFG> cfglist) {
        this.cfglist = cfglist;
    }
    public CFG getNext() {
        return next;
    }
    public void setNext(CFG next) {
        this.next = next;
    }
    List<CFG> cfglist;
    CFG next;
    public List<Statement> getReadyStatement(List<Statement> current){
        List<Statement> returnList=new ArrayList<Statement>();
        for(int i=0;i<cfglist.size();i++){
            CFG cfg=cfglist.get(i);
            List<Statement> list=new ArrayList<Statement>();
            list.add(current.get(i));
            List<Statement> returnstmt=cfg.getReadyStatement(list);
            if(returnstmt!=null)
                returnList.addAll(returnstmt);
        }
        if(returnList.size()==0){
            List<Statement> returnstmt=next.getReadyStatement(null);
            if(returnstmt!=null)
                returnList.addAll(returnstmt);
        }
        return returnList;
    }
}
