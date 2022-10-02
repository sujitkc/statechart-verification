package constabl.actionprogram;
import ast.*;

public abstract class EntryProgramPoint extends ProgramPoint{
    State state;
    public void setState(State s){
        this.state=s;
    }
    public State getState()
    {
        return this.state;
    }
     public EntryProgramPoint(String name){
        super(name);
    }
}