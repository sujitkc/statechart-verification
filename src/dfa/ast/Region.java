package ast;
import java.util.*;
public abstract class Region {
public List<Object> readVariables; //variables read in entry/exit of state or transition action
public List<Object> writeVariables; //variables write in entry/exit of state or transition action
public void setReadVariable(Object v){
  
	if(v instanceof java.util.ArrayList){ // v is ArrayList of Name or Declaration
			for(Object vobj:(ArrayList)v){
				String varname=getDeclarationForObject(vobj).getFullVName();
				if(!this.readVariables.contains(varname)) 
					this.readVariables.add(varname);
				}
		}
	else if(v!=null){  //v is Name or Declaration
				String varname=getDeclarationForObject(v).getFullVName();
				if(!this.readVariables.contains(varname)) 
					this.readVariables.add(varname);
	}
	
  }
 
public void setWriteVariable(Object v){
   
	if(v instanceof java.util.ArrayList){   // v is ArrayList of Name or Declaration
			for(Object vobj:(ArrayList)v){
				String varname=getDeclarationForObject(vobj).getFullVName();
				if(!this.writeVariables.contains(varname)) 
					this.writeVariables.add(varname);
				}
		}
	else if(v!=null){  //v is Name or Declaration
				String varname=getDeclarationForObject(v).getFullVName();
				if(!this.writeVariables.contains(varname)) 
					this.writeVariables.add(varname);
	}
	
  }
  
public Declaration getDeclarationForObject(Object v){
	Declaration d;
	if(v instanceof Name) d=((Name)v).getDeclaration();
	else d=(Declaration)v;
	return d;
  }
  
	
}
