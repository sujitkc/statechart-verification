package ast;

import java.util.List;
import java.util.ArrayList;

public class BasicType extends Type {

  public BasicType(String name) {
    super(name);
  }

  public BasicType(String name, List<String> typeParameterNames) {
    super(name, typeParameterNames);
  }
  
  public Type substantiate(List<Type> typeArguments) throws Exception {

    BasicType bt = new BasicType(this.name, this.typeParameterNames);
    for(Type oldtype : this.typeArguments) {
      Type newtype = null;
      if(oldtype instanceof TypeVariable) {
        TypeVariable typevar = (TypeVariable)oldtype;
        newtype = typeArguments.get(typevar.index);
      }
      else {
        newtype = oldtype.substantiate(typeArguments);
      }
      bt.typeArguments.add(newtype);
    }
    return bt;
  }

  public String toString() {
    String s = "basic type '" + this.name;
    if(this.typeParameterNames.size() != 0) {
      s += "<|";
      for(String tpar : this.typeParameterNames) {
        s += tpar + ", ";
      }
      s += "|>";
    }
    if(this.typeArguments.size() != 0) {
      s += "<|";
      for(Type targ : this.typeArguments) {
        s += targ + ", ";
      }
      s += "|>";
    }

   // s += "'\n";
    return s;
  }

  public Type copy() {
    Type type = new BasicType(this.name, this.typeParameterNames);
    List<Type> typeargs = new ArrayList<Type>();

    for(Type t : this.typeArguments) {
      typeargs.add(t.copy());
    }
    type.typeArguments = typeargs;
    return type;
  }
}
