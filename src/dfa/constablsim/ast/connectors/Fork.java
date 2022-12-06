package constablsim.ast.connectors;
import java.util.*;
import constablsim.*;

public class Fork extends Connector{
    CFA prev=null;
    public String name;
    public Fork(String name){
        this.name=name;
    }
    public CFA getPrev(){
        return this.prev;
    }
    public void setPrev(CFA cfa){
        this.prev=cfa;
    }
    
    public Fork(){

    }
    public String toString(){
        String str;
        str="Fork : "+this.name +" : Prev : { "+this.prev+" }";
        return str;
    }
}
