package constabl.automaton;
import constabl.actionprogram.*;;
public class Location {
    String name;
    ProgramPoint p;
    public Location(String name, ProgramPoint p) {
        this.name = name;
        this.p = p;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ProgramPoint getProgramPoint() {
        return p;
    }
    public void setProgramPoint(ProgramPoint p) {
        this.p = p;
    }
    public String toString(){
        String str="";
        str+=name+" : "+p;
        return str;
    }
}
