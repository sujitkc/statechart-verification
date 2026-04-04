package constabl.ast.connectors;
import constabl.ast.*;
public class Connector extends CodeNode{
    public String name;
    
    public Connector(String name){
        super(name);
        this.name=name;
    }


    
    public String toString(){
        String str;
        str="Connector : "+this.name +" : Prev : { ";
        for(CodeNode cfa:this.prev)
            str+=cfa+"; ";
        str+=" }";
        return str;
       
    }
    public boolean equals(Connector c){
        return this.name.equals(c.name);
    }
}
