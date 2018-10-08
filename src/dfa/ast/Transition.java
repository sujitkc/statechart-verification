package ast;

import java.util.*;

public class Transition {
  public  final String     name;
  public  final Name       sourceName;
  public  final Name       destinationName;
  public  final String     trigger;
  public  final Expression guard;
  public  final Statement  action;
  private       State      source;
  private       State      destination;

  private       State      state;
  private       Statechart statechart;

  public List<Object> readVariables;
  public List<Object> writeVariables;
 
  // Variables declared in readEnvironment aren't allowed to be used as
  // l-values, e.g. LHS of an assignment
  private Environment readOnlyEnvironment = null;

  // Variables declared in rwEnvironment can be used anywhere, both as r-values
  // as well as l-values.
  private Environment rwEnvironment = null;

  // Variables declared in writeOnlyEnvironment can be used only as l-values,
  // e.g. LHS of assignments. Their values shouldn't be used anywhere.
  private Environment writeOnlyEnvironment = null;

  private Environment readEnvironment = null;

  private Environment writeEnvironment = null;

  public Transition(String name,
      Name src,
      Name dest,
      String trigger,
      Expression guard,
      Statement action) {
    this.name            = name;
    this.sourceName      = src;
    this.destinationName = dest;
    this.trigger         = trigger;
    this.guard           = guard;
    this.action          = action;
    this.readVariables = new ArrayList<Object>();
    this.writeVariables = new ArrayList<Object>();
    
  }

  public State getSource() {
    return this.source;
  }

  public State getDestination() {
    return this.destination;
  }

  public State getState() {
    return this.state;
  }

  public Environment getReadOnlyEnvironment() throws Exception {
    if(this.readOnlyEnvironment == null) {
      this.readOnlyEnvironment = this.source.getEnvironment()
        .copyExclusive(this.state.getEnvironment());
    }
    return this.readOnlyEnvironment;
  }

  public Environment getWriteOnlyEnvironment() throws Exception {
    if(this.writeOnlyEnvironment == null) {
      this.writeOnlyEnvironment = this.destination.getEnvironment()
        .copyExclusive(this.state.getEnvironment());
    }
    return this.writeOnlyEnvironment;
  }

  public Environment getRWEnvironment() throws Exception {
    if(this.rwEnvironment == null) {
      this.rwEnvironment = this.state.getEnvironment();
    }
    return this.rwEnvironment;
  }

  public Environment getReadEnvironment() throws Exception {
    if(this.readEnvironment == null) {
      this.readEnvironment = this.source.getEnvironment();
    }
    return this.readEnvironment;
  }

  public Environment getWriteEnvironment() throws Exception {
    if(this.writeEnvironment == null) {
      this.writeEnvironment = this.getWriteOnlyEnvironment()
        .copyInclusive(this.state.getEnvironment());
    }
    return this.writeEnvironment;
  }

  public void setSourceDestinationStates() throws Exception {
    this.source = this.statechart.nameToState(this.sourceName);
    if(this.source == null) {
      throw new Exception(
        "Transition.setSourceDestinationStates : couldn't find state by name '"
        + this.sourceName + "'.");
    }
    this.destination = this.statechart.nameToState(this.destinationName);
    if(this.destination == null) {
      throw new Exception(
        "Transition.setSourceDestinationStates : couldn't find state by name '"
        + this.destinationName + "'.");
    }
  }

  public void setStatechart(Statechart sc) {
    this.statechart = sc;
  }

  public void setState(State s) {
    this.state = s;
  }

  public String toString() {
    String s = "\ntransition " + this.name + "{\n";
    s += "source : " + this.source.getFullName() + "\n";
    s += "destination : " + this.destination.getFullName() + "\n";
    s += "guard : " + this.guard + "\n";
    s += "action : " + this.action + "\n";
    s += "\n}\n";
    return s;
  }
    public void setReadVariable(Object v){
	if(v instanceof java.util.ArrayList){
			for(Object vobj:(ArrayList)v){
				Declaration d;
				if(vobj instanceof Name)
				d=((Name)vobj).getDeclaration();
				else d=(Declaration)vobj;
				if(!this.readVariables.contains(d.getFullVName())) this.readVariables.add(d.getFullVName());
				}
		}
	else if(v!=null){
				Declaration d;
				if(v instanceof Name)
				d=((Name)v).getDeclaration();
				else d=(Declaration)v;
		this.readVariables.add(d.getFullVName());
	}
    /*if(v!=null){
	if(v instanceof Expression && !readVariables.contains(v.toString()))
		this.readVariables.add(v.toString());
	else if(v instanceof String && !readVariables.contains(v))
		this.readVariables.add(v.toString());
	else this.readVariables.add(v.toString());
	}*/
  }
/*  public void setReadVariable(List<Object> vlist){
	for(Object v:vlist){
		if(v!=null) setReadVariable(v);
	}
  }*/
   public void setWriteVariable(Object v){
   if(v instanceof java.util.ArrayList){
			for(Object vobj:(ArrayList)v){
			
				Declaration d;
				if(vobj instanceof Name)
				d=((Name)vobj).getDeclaration();
				else d=(Declaration)vobj;
				if(!this.writeVariables.contains(d.getFullVName())) this.writeVariables.add(d.getFullVName());
				}
		}
	else if(v!=null){
		Declaration d;
				if(v instanceof Name)
				d=((Name)v).getDeclaration();
				else d=(Declaration)v;
		this.writeVariables.add(d.getFullVName());
	}
   /*if(v!=null){
	if(v instanceof Expression && !writeVariables.contains(v.toString()))
		this.writeVariables.add(v.toString());
	if(v instanceof String && !writeVariables.contains(v))
		this.writeVariables.add(v.toString());
	else this.writeVariables.add(v.toString());
	}*/
  }
   
 /* public void setWriteVariable(List<Object> vlist){
	for(Object v:vlist){
		if(v!=null) setWriteVariable(v);
	}
  }*/

}
