package constabl.actionprogram;
import ast.*;

public abstract class ActionProgramPoint extends ProgramPoint{
     Transition transition;
    public void setTransition(Transition s){
        this.transition=s;
    }
    public Transition getTransition()
    {
        return this.transition;
    }
    public ActionProgramPoint(String name){
        super(name);
    }
}