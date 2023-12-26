package searchsim.simulator; 

public abstract class SimState{
    protected SimState parent; 
    public SimState(){

    }

    public void setParent(SimState parent){
        this.parent = parent;
    }
}