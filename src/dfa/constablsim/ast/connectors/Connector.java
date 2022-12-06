package constablsim.ast.connectors;

public class Connector{
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
