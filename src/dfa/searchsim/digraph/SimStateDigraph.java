package searchsim.digraph; 

import searchsim.simulator.*;
import java.util.Map; 
import java.util.HashMap; 
import java.util.Set;
import java.util.HashSet; 

public class SimStateDigraph extends Digraph<SimState>{
    private String color = "grey"; 

    public SimStateDigraph(SimState root){
        super(root); 
    }

    public void setColor(String col){
        this.color = col; 
    }

    @Override 
    public void toDotScript()
    {
        this.indexNodes();
        System.out.println("digraph {"); 
        System.out.println("node [shape=record fontname=Arial];");
        
        for(int i = 1; i<=this.getNumNodes(); i++)
        {
            System.out.println(i + " " + "[label=\"" + this.indexToNode.get(i).toString() + "\"]\n"); 
            if(this.indexToNode.get(i) instanceof ExternalState)
                System.out.println("[style=filled, fillcolor=" + this.color + "]"); 
        }
        
        for(Map.Entry<SimState , Set<SimState>>entry : this.getAdjList().entrySet())
        {
            for(SimState node : entry.getValue())
            {
                System.out.println(this.nodeToIndex.get(entry.getKey()) + "->" + this.nodeToIndex.get(node)); 
            }
        }
        System.out.println("}"); 
    }

}