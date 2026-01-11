package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class Shell extends State {

 
  public Shell(
      String                    name,
      DeclarationList           declarations,
      Statement                 entry,
      Statement                 exit,
      List<State>               states,
      BooleanConstant           history
      
      ) throws Exception {
    /*BooleanConstant           region,
      BooleanConstant           shell,
      BooleanConstant           fin
*/
  //  super(name, declarations, entry, exit, states, transitions, history, region, shell, fin);
  super(name, declarations, entry, exit, states, new ArrayList<Transition>(), history);

    
    
  }

 
  public State lub(State s1, State s2) {
    List<State> a1 = s1.getAllSuperstates();
    List<State> a2 = s2.getAllSuperstates();
    State cca = null;
    while(a1.size() != 0 && a2.size() != 0) {
      if(a1.get(0).equals(a2.get(0))) {
        cca = a1.get(0);
        a1.remove(0);
        a2.remove(0);
      }
      else {
        break;
      }
    }
    return cca;
  }

  public State nameToState(Name name) {
    if(this.name.equals(name.name.get(0))) {
      return this.nameToState(name, 0);
    }
    return null;
  }

  public String toString() {
    String s = "shell " + this.name + " {\n";

    

    if(this.declarations != null) {
      for(Declaration d : this.declarations) {
        s += d.toString() + "\n";
      }
    }
  
    s += "\nentry : " + this.entry + "\n"; 
    s += "\nexit : " + this.exit + "\n";

    for(State st : this.states) {
      s += st.toString();
    }

    for(Transition tr : this.transitions) {
      s += tr.toString();
    }
    s += "}";
    return s;
  }
}
