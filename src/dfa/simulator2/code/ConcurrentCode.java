package simulator2.code;

import java.util.Set;
import java.util.HashSet;

public class ConcurrentCode extends Code {
  public Set<Code> codes = new HashSet<>();

  public ConcurrentCode(Set<Code> codes) {
    this.codes = codes;
    for(Code code : this.codes) {
      code.setParentCode(this);
    }
  }

  public Code reverse() {
    Set<Code> rcodes = new HashSet<>();
    for(Code code : codes) {
      rcodes.add(code.reverse());
    }
    return new ConcurrentCode(rcodes);
  }

  public Set<CFGCode> getFirstCFGCodeSet() {
    Set<CFGCode> firstCodes = new HashSet<>();
    for(Code code : this.codes) {
      firstCodes.addAll(code.getFirstCFGCodeSet());
    }
    return firstCodes;
  }

  public Set<CFGCode> getLastCFGCodeSet() {
    Set<CFGCode> lastCodes = new HashSet<>();
    for(Code code : this.codes) {
      lastCodes.addAll(code.getLastCFGCodeSet());
    }
    return lastCodes;
  }


  public void accept(CodeVisitor visitor) {
    for(Code c : this.codes) {
      visitor.visit(c);
    }
  }
}
