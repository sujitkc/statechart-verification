package simulator2.code;

public class CodeVisitor {

  private int level = 0;
  private String indentString = "  ";
  public CodeVisitor() {
  
  }

  private String indent() {
    String s = "";
    for(int i = 0; i < this.level; i++) {
      s += this.indentString;
    }
    return s;
  }

  public void visit(Code code) {
    if(code instanceof CFGCode) {
      CFGCode cfgCode = (CFGCode)code;
      //System.out.println(this.indent() + "******************************************");
      //System.out.println(this.indent() + "CFG Code ");
      //System.out.println(cfgCode.cfg);
      //System.out.println(this.indent() + "******************************************");
      code.accept(this);
    }
    else if(code instanceof ConcurrentCode) {
      //System.out.println(this.indent() + "Concurrent Code ");
      this.level++;
      code.accept(this);
      this.level--;
    }
    else if(code instanceof SequenceCode) {
      //System.out.println(this.indent() + "Sequence Code ");
      this.level++;
      code.accept(this);
      this.level--;
    }
  }
}
