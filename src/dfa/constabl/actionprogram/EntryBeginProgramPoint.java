package constabl.actionprogram;
import ast.*;

public class EntryBeginProgramPoint extends EntryProgramPoint{
    public EntryBeginProgramPoint(String name, State s){
        super(name,s);
    }
    public String toString(){
        String str="";
        str+=name+" ";
        return str;
    }
}