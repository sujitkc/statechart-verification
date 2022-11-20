package constablsim.ast;
import ast.*;
import java.util.*;
public class Configuration {
    List<State> currentStates;
    List<Environment> currentEnvironments;
    public Configuration(){
        currentStates=new ArrayList<State>();
        currentEnvironments=new ArrayList<Environment>();
    }
    public List<State> getCurrentStates() {
        return currentStates;
    }
    public void setCurrentStates(List<State> currentStates) {
        this.currentStates = currentStates;
    }
    public void addState(State s) {
        this.currentStates.add(s);
    }
    public List<Environment> getCurrentEnvironments() {
        return currentEnvironments;
    }
    public void setCurrentEnvironments(List<Environment> currentEnvironments) {
        this.currentEnvironments = currentEnvironments;
    }
}
