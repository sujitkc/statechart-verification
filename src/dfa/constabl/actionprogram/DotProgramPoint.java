package constabl.actionprogram;
import ast.*;

public class DotProgramPoint extends ProgramPoint{
    State state;
    public void setState(State s){
        this.state=s;
    }
    public State getState()
    {
        return this.state;
    }
    public DotProgramPoint(String name){
        super(name);
    }
}