package constabl.actionprogram;

import ast.*;

public abstract class ProgramPoint{
    
    Environment env;
    String name;
    public ProgramPoint(String name){
        this.name=name;
    }
    public void setEnvironment(Environment env){
        this.env=env;
    }
    public Environment getEnvironment(){
        return this.env;
    }
    public String toString(){
        String str="";
        str+=name+" ";
        return str;
    }
}