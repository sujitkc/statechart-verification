package constabl.consimulator;

import ast.*;
import java.util.*;
import constabl.actionprogram.*;

public class SequentialExecutionBlock extends ExecutionBlock{
    public ArrayList<State> stateList=new ArrayList<State>();
    public List<ProgramPoint> points=new ArrayList<ProgramPoint>();
    public ConcurrentExecutionBlock next=null;
    public ProgramPoint getLastProgramPoint(){
        return this.points.get(this.points.size()-1);
    }
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
    public void setNextCES(ConcurrentExecutionBlock ces){
        this.next=ces;
    }
    public ConcurrentExecutionBlock getNextCES(){
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
    @Override
    public boolean hasNext(){
        System.out.println("Next : "+this.next);
        if(this.next==null)
            return false;
        else
            return true;
    }
}