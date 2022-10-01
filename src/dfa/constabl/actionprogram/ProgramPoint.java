package constabl.actionprogram;

import ast.*;

public abstract class ProgramPoint{
    Environment env;
    public void setEnvironment(Environment env){
        this.env=env;
    }
    public Environment getEnvironment(){
        return this.env;
    }
}