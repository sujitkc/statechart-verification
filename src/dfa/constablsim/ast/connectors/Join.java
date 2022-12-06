package constablsim.ast.connectors;
import java.util.*;
import constablsim.*;
public class Join extends Connector{
    
    List<CFA> prev=new ArrayList<CFA>();
    public String name;
    public Join(String name){
        this.name=name;
    }
    public List<CFA> getPrev(){
        return this.prev;
    }
    public void addPrev(CFA cfa){
        
        this.prev.add(cfa);
    }

    public Join(){

    }
    public String toString(){
        String str;
        str="Join : "+this.name +" : Prev : { ";
        for(CFA cfa:this.prev)
            str+=cfa+"; ";
        str+=" }";
        return str;
       
    }
}
