package constabl.actionprogram;
import ast.*;

public class InternalProgramPoint extends ProgramPoint{
    State state;
    public void setState(State s){
        this.state=s;
    }
    public State getState()
    {
        return this.state;
    }
    public InternalProgramPoint(String name){
        super(name);
    }
}