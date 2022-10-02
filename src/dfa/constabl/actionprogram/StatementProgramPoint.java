package constabl.actionprogram;
import ast.*;

public class StatementProgramPoint extends ProgramPoint{
   State state;
    public void setState(State s){
        this.state=s;
    }
    public State getState()
    {
        return this.state;
    }
    public StatementProgramPoint(String name){
        super(name);
    }
}