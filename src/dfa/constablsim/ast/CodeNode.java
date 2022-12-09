package constablsim.ast;
import java.util.*;
public class CodeNode{
    public String name;

    public List<CodeNode> prev=new ArrayList<CodeNode>();
    public CodeNode(String name){
        this.name=name;
    }
    public List<CodeNode> getPrev(){
        return this.prev;
    }
    public void addPrev(CodeNode cfa){
        
        this.prev.add(cfa);
    }
}