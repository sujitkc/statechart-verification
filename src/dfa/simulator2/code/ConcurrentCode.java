package simulator2.code;

import java.util.Set;
import java.util.HashSet;

public class ConcurrentCode extends Code {
  public Set<Code> codes = new HashSet<>();

  public ConcurrentCode(Set<Code> codes) {
    this.codes = codes;
  }

  public Code reverse() {
    Set<Code> rcodes = new HashSet<>();
    for(Code code : codes) {
      rcodes.add(code.reverse());
    }
    return new ConcurrentCode(rcodes);
  }
}
