package constabl.actionprogram;
import ast.*;

public class EntryBeginProgramPoint extends EntryProgramPoint{
    public EntryBeginProgramPoint(String name){
        super(name);
    }
    public String toString(){
        String str="";
        str+=name+" ";
        return str;
    }
}