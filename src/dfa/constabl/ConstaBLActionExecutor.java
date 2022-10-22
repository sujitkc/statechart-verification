package constabl;

import ast.*;
import constabl.consimulator.*;
import constabl.actionprogram.*;
import java.util.*;

public class ConstaBLActionExecutor {
    public Environment env;
    public static List<ProgramPoint> readyProgramPoints= new ArrayList<ProgramPoint>();

    
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public Environment getEnvironment() {
        return this.env;
    }

    public static void executeAction(ExecutionBlock eblock, ProgramPoint point) {
        System.out.println("Executing the point : " + point);
        
        if(readyProgramPoints!=null && readyProgramPoints.size()==0){
            //stop execution
            System.out.println("If");
        }
        else{
            System.out.println("Else: " );
            if(eblock instanceof SequentialExecutionBlock){
                readyProgramPoints.remove(point);
                point=chooseRandom();
                readyProgramPoints.add(((SequentialExecutionBlock)eblock).getNextProgramPoint(point));
                
                executeAction(eblock, point);
            }else if( eblock instanceof ConcurrentExecutionBlock){
                System.out.println("hello");
            }
        }
        
    }
    public static ProgramPoint chooseRandom(){
        if(readyProgramPoints.size()>0){
            Random rand = new Random();
            int i=rand.nextInt(readyProgramPoints.size());
            System.out.println(i);
            ProgramPoint randomElement = readyProgramPoints.get(i);
            readyProgramPoints.remove(randomElement);
            return randomElement;
        }
        else{
            return null;
        }
        
    }
    public static void getReadySet(ExecutionBlock eBlock, List<ProgramPoint> ppoints){
        
    }

}
