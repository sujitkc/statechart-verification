package ast;

import java.util.List;
import java.util.ArrayList;

public class Struct extends Type {
  public final DeclarationList declarations;

  public Struct(String name, DeclarationList declarations) {
    super(name);
    this.declarations = declarations;
  }

  public Struct(String name, DeclarationList declarations, List<String> typeParameterNames) {
    super(name, typeParameterNames);
    this.declarations = declarations;
  }
  
  public Type substantiate(List<Type> typeArguments) throws Exception {

    DeclarationList decs = new DeclarationList();
    for(Declaration olddecl : this.declarations) {
      String vname = olddecl.vname;
      Type oldtype = olddecl.type;
      Type newtype = null;
      if(oldtype instanceof TypeVariable) {
        TypeVariable typevar = (TypeVariable)oldtype;
        newtype = typeArguments.get(typevar.index);
      }
      else {
        newtype = oldtype.substantiate(typeArguments);
      }
      Declaration newdecl = new Declaration(vname, olddecl.typeName, olddecl.input);
      newdecl.setType(newtype);
      decs.add(newdecl);
    }
    Struct s = new Struct(this.name, decs);
    return s;
  }

  public String toString() {
    String s = "struct " + this.name + "{\n";
    s += this.declarations.toString();
    s += "}\n";
    return s;
  }

  public Type copy() {
    DeclarationList decs = new DeclarationList();

    for(Declaration dec : this.declarations) {
      Declaration d = new Declaration(dec.vname, dec.typeName, dec.input);
      d.setType(dec.getType().copy());
      decs.add(d);
    }

    return new Struct(this.name, decs, this.typeParameterNames);
  }
}
