package metric;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import ast.*;

public class Metric {

  public static void scope_S(State state, Map<String, Integer> scope) {
    DeclarationList declarations = state.declarations;
    int numberOfStates = Metric.getNumberOfStates(state);
    for(Declaration declaration : declarations) {
      scope.put(declaration.getFullVName(), numberOfStates);
    }
    for(State s : state.states) {
      Metric.scope_S(s, scope);
    }
  }

  public static void scope_T(State state, Map<String, Integer> scope) {
    DeclarationList declarations = state.declarations;
    int numberOfStates = Metric.getNumberOfStates(state);
    for(Declaration declaration : declarations) {
      scope.put(declaration.getFullVName(), numberOfStates);
    }
    for(State s : state.states) {
      Metric.scope_T(s, scope);
    }
  }
  
  public static void allTransitions(State state, Set<Transition> transitions) {
    for(Transition transition : state.transitions) {
      transitions.add(transition);
    } 

    for(State s : state.states) {
      Metric.allTransitions(s, transitions);
    }
  }

  public static Set<Transition> t_IN(State state, Set<Transition> allTransitions) {
    Set<Transition> transitions = new HashSet<Transition>();
    for(Transition transition : allTransitions) {
      if(transition.getDestination() == state) {
        transitions.add(transition);
      }
    }
    System.out.println("t_IN(" + state.name + "):");
    for(Transition transition : transitions) {
      System.out.print("\t" + transition.name + " ");
    }
    System.out.println("");
    return transitions;
  }

  public static Set<Transition> t_OUT(State state, Set<Transition> allTransitions) {
    Set<Transition> transitions = new HashSet<Transition>();
    for(Transition transition : allTransitions) {
      if(transition.getSource() == state) {
        transitions.add(transition);
      }
    }
    return transitions;
  }

  public static void Wscope(State state, Map<String, Integer> scope, Set<Transition> allTransitions) {
    DeclarationList declarations = state.declarations;
    int numberOfStates = Metric.getNumberOfStates(state);
    int numberOfTransitions = Metric.getNumberOfTransitions(state);
    Set<Transition> t_IN = Metric.t_IN(state, allTransitions);
    for(Declaration declaration : declarations) {
      scope.put(declaration.getFullVName(), numberOfStates + numberOfTransitions + t_IN.size());
    }
    for(State s : state.states) {
      Metric.scope_T(s, scope);
    }   
  }

  public static void Rscope(State state, Map<String, Integer> scope, Set<Transition> allTransitions) {
    DeclarationList declarations = state.declarations;
    int numberOfStates = Metric.getNumberOfStates(state);
    int numberOfTransitions = Metric.getNumberOfTransitions(state);
    Set<Transition> t_OUT = Metric.t_OUT(state, allTransitions);
    for(Declaration declaration : declarations) {
      scope.put(declaration.getFullVName(), numberOfStates + numberOfTransitions + t_OUT.size());
    }
    for(State s : state.states) {
      Metric.scope_T(s, scope);
    }   
  }

  public static int getNumberOfStates(State state) {
    int numberOfStates = 1;

    for(State s : state.states) {
      numberOfStates += Metric.getNumberOfStates(s);
    }

    return numberOfStates;
  }

  public static int getNumberOfTransitions(State state) {
/*
    int numberOfTransitions = state.transitions.size();

    for(State s : state.states) {
      numberOfTransitions += Metric.getNumberOfTransitions(s);
    }
    return numberOfTransitions;
*/
    Set<Transition> transitions = new HashSet<Transition>();
    allTransitions(state, transitions);
    return transitions.size();
  }
}
