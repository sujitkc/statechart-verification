package constabl.consimulator;
import constabl.actionprogram.*;
import ast.*;
import java.util.*;

public class Configuration{
    public ArrayList<State> activestates;
    public ArrayList<ProgramPoint> programpoints;
    //Each active state will have exactly one program point.
    public Configuration(ArrayList<State> activestates){
        this.activestates=activestates;
    }
    public Configuration(List<ProgramPoint> activeProgramPoint){
        this.programpoints=new ArrayList<ProgramPoint>();
        this.programpoints.addAll(activeProgramPoint);
    }
    /*public ArrayList<State> completeConfiguration(){

    }*/
    public ArrayList<ProgramPoint> getProgramPoints(){
        return this.programpoints;
    }
    public String toString(){
        String s="";
        for (State st:this.activestates){
            s+=st+"\n";
        }
        return s;
    }

}