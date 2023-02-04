import java.util.Set;
import java.util.HashSet;

public class SequenceCode extends Code {
  public Set<Code> codes = new HashSet<>();

  public ConcurrentCode(Set<Code> codes) {
    this.codes = codes;
  }
}
