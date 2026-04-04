package searchsim.tree;

public class Edge<T> {
    private T parent; 
    private T child; 

    public Edge(T u , T v){
        this.parent = u; 
        this.child = v; 
    }

    public T getParent()
    {
        return this.parent; 
    }

    public T getChild()
    {
        return this.child; 
    }
}
