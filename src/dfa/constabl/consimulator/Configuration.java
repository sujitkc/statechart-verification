package constabl.consimulator;

import ast.*;
import java.util.ArrayList;

public class Configuration{
    public ArrayList<State> activestates;
    public Configuration(ArrayList<State> activestates){
        this.activestates=activestates;
    }
    /*public ArrayList<State> completeConfiguration(){

    }*/
    public String toString(){
        String s="";
        for (State st:this.activestates){
            s+=st+"\n";
        }
        return s;
    }

}