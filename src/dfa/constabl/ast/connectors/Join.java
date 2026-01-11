package constabl.ast.connectors;
import java.util.*;
import constabl.ast.*;
public class Join extends Connector{
    
   // List<CFA> prev=new ArrayList<CFA>();
    public String name;
    public Join(String name){
        super(name);
        this.name=name;
    }
    // public List<CFA> getPrev(){
    //     return this.prev;
    // }
    // public void addPrev(CFA cfa){
        
    //     this.prev.add(cfa);
    // }

    
    public String toString(){
        String str;
        str="Join : "+this.name +" : Prev : { ";
        for(CodeNode cfa:this.prev)
            str+=cfa+"; ";
        str+=" }";
        return str;
       
    }
}
