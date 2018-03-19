package ast;

import java.util.List;
import java.util.ArrayList;

public class FunctionDeclaration {
  public final String name;
  public final List<String> typeParameterNames; // used in type declaration, e.g. foo<A, B>(a : A, b : B);
  public final TypeName returnTypeName;
  private DeclarationList parameterList;

  // used in after typechecking of function declarations.
  public List<Type> typeArguments = new ArrayList<Type>();

  private Type returnType;

  public FunctionDeclaration(
      String name,
      TypeName returnTypeName,
      DeclarationList parameterList) {

    this.name               = name;
    this.typeParameterNames = new ArrayList<String>();
    this.returnTypeName     = returnTypeName;
    this.parameterList      = parameterList;
  }

  public FunctionDeclaration(
      String name,
      List<String> typeParameterNames,
      TypeName returnTypeName,
      DeclarationList parameterList) {

    this.name               = name;
    this.typeParameterNames = typeParameterNames;
    this.returnTypeName     = returnTypeName;
    this.parameterList      = parameterList;
  }

  public Type getReturnType() {
    return this.returnType;
  }

  public DeclarationList getParameterList() {
    return this.parameterList;
  }

  public String toString() {
    String s = this.name + "(";
    for(Declaration arg : this.parameterList) {
      s += arg.vname + " : " + arg.typeName + ", ";
    }
    return s + ") : " + this.returnTypeName;
  }

  public void setReturnType(Type type) {
    this.returnType = type;
  }
   
  public FunctionDeclaration copy() {
    DeclarationList params = new DeclarationList();

    for(Declaration param : this.parameterList) {
      Declaration p = new Declaration(param.vname, param.typeName, param.input);
      p.setType(param.getType().copy());
      params.add(p);
    }

    FunctionDeclaration newfunction = new FunctionDeclaration(this.name, this.typeParameterNames, this.returnTypeName, params);
    newfunction.setReturnType(this.returnType.copy());
    return newfunction;
  }

  public FunctionDeclaration substantiate(List<Type> typeArguments) throws Exception {

    DeclarationList decs = new DeclarationList();
    for(Declaration olddecl : this.parameterList) {
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
      Declaration newdecl = new Declaration(olddecl.vname, olddecl.typeName, olddecl.input);
      newdecl.setType(newtype);
      decs.add(newdecl);
    }
    FunctionDeclaration newfunction = new FunctionDeclaration(this.name, this.typeParameterNames, this.returnTypeName, decs);
    if(this.returnType == null) {
      throw new Exception("No return type found for " + this.name);
    }
    newfunction.setReturnType(this.returnType.substantiate(typeArguments));
    return newfunction;
  }
}
