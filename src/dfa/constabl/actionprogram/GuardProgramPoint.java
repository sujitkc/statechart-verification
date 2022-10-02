package constabl.actionprogram;
import ast.*;

public class GuardProgramPoint extends ProgramPoint{
    Transition transition;
    public void setTransition(Transition s){
        this.transition=s;
    }
    public Transition getTransition()
    {
        return this.transition;
    }
    public GuardProgramPoint(String name){
        super(name);
    }
}