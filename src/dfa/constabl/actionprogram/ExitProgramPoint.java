package constabl.actionprogram;
import ast.*;

public abstract class ExitProgramPoint extends ProgramPoint{
    State state;
    public void setState(State s){
        this.state=s;
    }
    public State getState()
    {
        return this.state;
    }
    public ExitProgramPoint(String name, State s){
        super(name);
        this.state=s;
    }
}