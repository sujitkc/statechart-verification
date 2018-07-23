package ast;

import java.util.List;
import java.util.ArrayList;

public class TypeName {
  public final String name;
  public final List<TypeName> typeArgumentNames;
  public Type type;

  public TypeName(String name) {
    this.name = name;
    this.typeArgumentNames = new ArrayList<TypeName>();
  }

  public TypeName(String name, List<TypeName> typeArgumentNames) {
    this.name = name;
    this.typeArgumentNames = typeArgumentNames;
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
