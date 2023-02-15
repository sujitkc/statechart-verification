package simulator2.code;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SequenceCode extends Code {
  public List<Code> codes;

  public SequenceCode(List<Code> codes) {
    this.codes = codes;
  }

  public Code reverse() {
    List<Code> rcodes = new ArrayList<>();
    rcodes.addAll(this.codes);
    Collections.reverse(rcodes);
    return new SequenceCode(rcodes);
  }
}
