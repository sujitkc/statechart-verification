package searchsim.digraph; 


import java.util.Map; 
import java.util.HashMap; 
import java.util.Set;



import java.util.HashSet; 

public class Digraph<T>{
    private T root; 
    private Map<T , Set<T>>adjList = new HashMap<T , Set<T>>(); 
    private Map<Integer , T>indexToNode = new HashMap<Integer , T>(); 
    private Map<T, Integer>nodeToIndex = new HashMap<T , Integer>();  
    
    public Digraph(T root){
        this.root = root; 
        this.adjList.put(root , new HashSet<T>()); 
    }

    public T getRoot(){
        return this.root; 
    }

    public void addNode(T u){
        if(this.adjList.containsKey(u)){
            System.out.println("Node already exists"); 
            return; 
        }
        this.adjList.put(u , new HashSet<T>()); 
    }

    public void addChild(T u , T v){
        if(!this.adjList.containsKey(v)){
            this.adjList.put(v , new HashSet<T>()); 
        }

        if(!this.adjList.containsKey(u))
        {
            this.adjList.put(u , new HashSet<T>()); 
        }
        this.adjList.get(u).add(v); 
    }

    public Set<T> getLeafNodes(){
        Set<T> leafList = new HashSet<T>(); 
        for(Map.Entry<T , Set<T>>entry : this.adjList.entrySet())
        {
            if(entry.getValue().size() == 0)
            {
                leafList.add(entry.getKey()); 
            }
        }
        return leafList; 
    }

    public int getNumNodes()
    {
        return this.adjList.size(); 
    }

    public String toString(){
        String res = ""; 
        for(Map.Entry<T , Set<T>>entry : this.adjList.entrySet())
        {
            res = res + entry.getKey() + " " + entry.getValue() + "\n"; 
        }
        return res; 
    }

    public Map<T , Set<T>> getAdjList(){
        return this.adjList; 
    }

    public Set<T> getNodes()
    {
        return this.adjList.keySet(); 
    }

    public void toDotScript()
    {
        this.indexNodes();
        System.out.println("digraph {"); 
        System.out.println("node [shape=record fontname=Arial];");
        
        for(int i = 1; i<=this.getNumNodes(); i++)
        {
            System.out.println(i + " " + "[label=\"" + this.indexToNode.get(i).toString() + "\"]\n"); 
        }
        
        for(Map.Entry<T , Set<T>>entry : this.getAdjList().entrySet())
        {
            for(T node : entry.getValue())
            {
                System.out.println(this.nodeToIndex.get(entry.getKey()) + "->" + this.nodeToIndex.get(node)); 
            }
        }
        System.out.println("}"); 
    }
    public void indexNodes()
    {
        int ctr = 1; 
        for(T node : this.adjList.keySet())
        {
            this.indexToNode.put(ctr , node); 
            this.nodeToIndex.put(node , ctr); 
            ctr++; 
        }
    }

    //root does not exist 
    public void addSubgraph(T root , Digraph<T> subDigraph){
        this.addChild(root, subDigraph.getRoot()); 

        for(Map.Entry<T , Set<T>>entry : subDigraph.getAdjList().entrySet()){
            for(T ch : entry.getValue())
            {
                this.addChild(entry.getKey(), ch);
            }
        }
    }

    public void addSubgraphRootExists(T root , Digraph<T> subDigraph){

    }


    /*
     * Need to add a path searching function eventually
     */
}
