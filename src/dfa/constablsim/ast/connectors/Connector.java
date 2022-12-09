package constablsim.ast.connectors;
import constablsim.ast.*;
public class Connector extends CodeNode{
    public String name;
   
   
    public Connector(String name){
        this.name=name;
    }
    public String toString(){
        return "..Connector..";
    }
    public boolean equals(Connector c){
        return this.name.equals(c.name);
    }
}
