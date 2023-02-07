package simulator2.code;

import java.util.List;
import java.util.ArrayList;

public class SequenceCode extends Code {
  public List<Code> codes;

  public SequenceCode(List<Code> codes) {
    this.codes = codes;
  }
}
