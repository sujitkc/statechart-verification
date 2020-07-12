package ast;

import java.util.List;
import java.util.ArrayList;

public class FunctionName {
  public final String name;
  public final List<TypeName> typeArgumentNames;
  public FunctionDeclaration type;

  public FunctionName(String name) {
    this.name = name;
    this.typeArgumentNames = new ArrayList<TypeName>();
  }

  public FunctionName(String name, List<TypeName> typeArgumentNames) {
    this.name = name;
    this.typeArgumentNames = typeArgumentNames;
  }

  public String getName(){
    return this.name;
  }
  public String toString() {
    String s = this.name;
    if(this.typeArgumentNames.size() != 0) {
      s += "<|";
      for(TypeName tp : this.typeArgumentNames) {
        s += tp + ", ";
      }
      s += "|>";
    }
    return s;
  }
}
