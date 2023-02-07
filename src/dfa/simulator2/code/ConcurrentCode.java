package simulator2.code;

import java.util.Set;
import java.util.HashSet;

public class ConcurrentCode extends Code {
  public Set<Code> codes = new HashSet<>();

  public ConcurrentCode(Set<Code> codes) {
    this.codes = codes;
  }
}
