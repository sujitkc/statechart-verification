package simulator2.code;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class SequenceCode extends Code {
  public final List<Code> codes;

  public SequenceCode(List<Code> codes) {
    this.codes = codes;
    for(Code code : this.codes) {
      code.setParentCode(this);
    }
  }

  public Code reverse() {
    List<Code> rcodes = new ArrayList<>();
    for(Code code : this.codes) {
      rcodes.add(code.reverse());
    }
    Collections.reverse(rcodes);
    return new SequenceCode(rcodes);
  }

  public Code getFirstCode() {
    return this.codes.get(0);
  }

  public Code getLastCode() {
    return this.codes.get(this.codes.size() - 1);
  }

  public Code getNextSibling(Code code) {
    int i = this.codes.indexOf(code);
    if(i == -1) { // code doesn't belong this sequence
      return null;
    }
    else if(i == this.codes.size() - 1) { // last code; no next.
      return null;
    }
    else {
      return this.codes.get(i + 1);
    }
  }

  public Code getPreviousSibling(Code code) {
    int i = this.codes.indexOf(code);
    if(i == -1) { // code doesn't belong this sequence
      return null;
    }
    else if(i == 0) { // first code; no previous sibling.
      return null;
    }
    else {
      return this.codes.get(i - 1);
    }
  }

  public Set<CFGCode> getFirstCFGCodeSet() {
    return this.getFirstCode().getFirstCFGCodeSet();
  }

  public Set<CFGCode> getLastCFGCodeSet() {
    return this.getLastCode().getLastCFGCodeSet();
  }


  public void accept(CodeVisitor visitor) {
    for(Code c : this.codes) {
      visitor.visit(c);
    }
  }
}
