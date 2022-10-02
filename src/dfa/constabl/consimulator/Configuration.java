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
        this.activestates=new ArrayList<State>();

        this.programpoints.addAll(activeProgramPoint);
        this.activestates=this.getActiveStates();
    }
    public Configuration(){
        this.programpoints=new ArrayList<ProgramPoint>();
        this.activestates=new ArrayList<State>();
    }
    
    public ArrayList<State> getActiveStates(){
        this.activestates=new ArrayList<State>();
        for(ProgramPoint p:this.programpoints){
            if(p instanceof EntryProgramPoint){
                this.activestates.add(((EntryProgramPoint)p).getState());

            }
            if(p instanceof ExitProgramPoint){
                this.activestates.add(((ExitProgramPoint)p).getState());

            }
        }
        return this.activestates;
    }
    public ArrayList<ProgramPoint> getProgramPoints(){
        return this.programpoints;
    }
    public String toString(){
        String s="";
        /*for (State st:this.activestates){
            s+=st+"\n";
        }*/
         for(ProgramPoint p:this.programpoints){
            s+=p+", ";
         }
        return s;
    }

}