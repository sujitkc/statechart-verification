package ast;

import java.util.ArrayList;
import java.util.List;

public class DeclarationList extends ArrayList<Declaration>{

  public boolean add(Declaration dec) {
    for(Declaration d : this) {
      if(d.vname.equals(dec.vname)) {
        return false;
      }
    }
    return super.add(dec);
  }
}
