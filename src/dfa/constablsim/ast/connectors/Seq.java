package constablsim.ast.connectors;
import java.util.*;
import constablsim.*;
public class Seq extends Connector{
    
    CFA prev=null;
    public String name;
    public Seq(String name){
        super(name);
        this.name=name;
    }
    public CFA getPrev(){
        return this.prev;
    }
    public void setPrev(CFA cfa){
        this.prev=cfa;
    }
    public String toString(){
        String str;
        str="Seq : "+this.name +" : Prev : { "+this.prev+" }";
        return str;
    }
}
