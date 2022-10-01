package constabl.actionprogram;

import ast.*;

public abstract class ProgramPoint{
    Environment env;
    public void setEnvironment(Environment env){
        this.env=env;
    }
    public void getEnvironment(){
        return this.env;
    }
}