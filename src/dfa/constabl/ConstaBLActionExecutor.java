package constabl;
import ast.*;
import constabl.consimulator.Configuration;
import constabl.consimulator.ExecutionBlock;
public class ConstaBLActionExecutor {
    public Environment env;
    public void setEnvironment(Environment env){
        this.env=env;
    }
    public Environment getEnvironment(){
        return this.env;
    }
    public static void executeAction(Configuration c, ExecutionBlock eblock){
        System.out.println("Executing the action block : "+eblock);
        if(c==null){
            c=new Configuration();
        }
        else{

        }
    }
}
