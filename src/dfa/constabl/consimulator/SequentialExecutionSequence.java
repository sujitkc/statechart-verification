package constabl.consimulator;

import ast.*;
import java.util.*;
import constabl.actionprogram.*;

public class SequentialExecutionSequence extends ExecutionSequence{
    public ArrayList<State> stateList=new ArrayList<State>();
    public List<ProgramPoint> points=new ArrayList<ProgramPoint>();
    public ConcurrentExecutionSequence next=null;
    public String toString(){
        String s=" ";
        for(ProgramPoint p: points)
            s+=p+" -> ";
        if(next!=null)
            s+=next;
        return s+" ";
    }
    public void addState(State s){
        this.stateList.add(s);
 
    }
    public void setNextCES(ConcurrentExecutionSequence ces){
        this.next=ces;
    }
    public ConcurrentExecutionSequence getNextCES(){
        return this.next;
    }
    public void setProgramPointSequence(List<ProgramPoint> p){
        this.points=p;
    }
    public List<ProgramPoint> getProgramPointSequence(){
        return this.points;
    }
    public void addProgramPoint(ProgramPoint p){
        this.points.add(p);
    }
    public boolean hasNext(){
        if(next==null)
            return false;
        else
            return true;
    }
}