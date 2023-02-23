package simulator2.code;

import java.util.Set;
import java.util.HashSet;

public abstract class Code {
  protected Code parentCode;
  public abstract Code reverse();
  public abstract Set<CFGCode> getFirstCFGCodeSet();

  public void setParentCode(Code code) {
    this.parentCode = code;
  }

  public Code getParentCode() {
    return this.parentCode;
  }

  public Set<CFGCode> getNextCFGCodeSet() {
    if(this.parentCode instanceof ConcurrentCode) {
      return this.parentCode.getNextCFGCodeSet();
    }
    else if(this.parentCode instanceof SequenceCode) {
      SequenceCode seqParent = (SequenceCode)this.parentCode;
      if(seqParent.getLastCode().equals(this)) {
        return this.parentCode.getNextCFGCodeSet();
      }
      else {
        Code nextSibling = seqParent.getNextSibling(this);
        return nextSibling.getFirstCFGCodeSet();
      }
    }
    else { // this is top level code, i.e. no parent.
      return new HashSet<CFGCode>();
    }
  }

}
