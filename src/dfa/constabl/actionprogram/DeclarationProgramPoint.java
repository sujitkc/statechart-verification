package constabl.actionprogram;
import ast.*;

public class DeclarationProgramPoint extends ProgramPoint{
    State state;
    public void setState(State s){
        this.state=s;
    }
    public State getState()
    {
        return this.state;
    }
    public DeclarationProgramPoint(String name){
        super(name);
    }
}