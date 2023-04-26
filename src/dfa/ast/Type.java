package ast;

import java.util.List;
import java.util.ArrayList;

public abstract class Type {

  public final String name;
  public List<String> typeParameterNames; // used in type declaration, e.g. map<a, b>

  // used in after typechecking of variable declarations, either in the body of the
  // program or as a part of a type declaration, e.g. structures and functions.
  public List<Type> typeArguments = new ArrayList<Type>();

  public Type(String name) {
    this.name = name;
    this.typeParameterNames = new ArrayList<String>();
  }

  public Type(String name, List<String> typeParameterNames) {
    this.name = name;
    this.typeParameterNames = typeParameterNames;
  }

  /*
    This function returns an isotopic type with all occurances of type variables
    in it replaced by the corresponding type arguments.
  */
  public abstract Type substantiate(List<Type> typeArguments) throws Exception;

  public boolean equals(Object t) {
    Type othertype = (Type)t;
    if(othertype.name != this.name || othertype.typeArguments.size() != this.typeArguments.size()) {
      return false;
    }
    for(int i = 0; i < this.typeArguments.size(); i++) {
      if(this.typeArguments.get(i).equals(othertype.typeArguments.get(i)) == false) {
        return false;
      }
    }
    return true;
  }

  public abstract Type copy();
}
