package simulator2.code;

import java.util.Set;
import java.util.HashSet;

import simulator2.cfg.*;

public class CFGCode extends Code {
  public final CFG cfg;

  public CFGCode(CFG cfg) {
    this.cfg = cfg;
  }

  public Code reverse() {
    return this;
  }

  public Set<CFGCode> getFirstCFGCodeSet() {
    Set<CFGCode> set = new HashSet<>();
    set.add(this);
    return set;
  }
  public Set<CFGCode> getLastCFGCodeSet() {
    return this.getFirstCFGCodeSet();
  }

  public void accept(CodeVisitor visitor) {
  }
}
