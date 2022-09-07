/* The following code was generated by JFlex 1.4.1 on 7/9/22 9:13 PM */

package frontend;

import java_cup.runtime.Symbol;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1
 * on 7/9/22 9:13 PM from the specification file
 * <tt>frontend/lexer.flex</tt>
 */
class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int STRLITERAL = 1;
  public static final int YYINITIAL = 0;

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = {
     0,  0,  0,  0,  0,  0,  0,  0,  0,  3, 58,  0,  3,  2,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     3, 53, 57, 43,  0,  0, 52,  0, 48, 49, 10, 54, 51,  4, 42,  9, 
     5,  6,  6,  6,  6,  6,  6,  6,  6,  6, 44, 50, 55, 45, 56,  0, 
     0,  7,  7,  7,  7, 38,  7, 40, 31, 32,  7,  7, 39,  7, 41, 35, 
     7,  7, 36, 33, 34,  7,  7,  7,  7, 37,  7,  0,  0,  0,  0,  8, 
     0, 23,  7, 24, 27, 11, 21, 28, 19, 17,  7,  7, 20,  7, 12, 25, 
    29,  7, 14, 22, 13, 26, 30, 18, 16, 15,  7, 46,  1, 47,  0,  0
  };

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\2\2\1\3\2\4\1\5\1\6\1\7"+
    "\14\5\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\1\1\1\23\1\24\1\25"+
    "\1\26\1\27\1\30\1\31\1\32\1\33\1\0\1\34"+
    "\1\0\6\5\1\35\13\5\1\36\1\22\1\37\1\40"+
    "\1\41\1\42\1\34\2\0\24\5\1\0\1\5\1\43"+
    "\1\44\3\5\1\45\1\46\14\5\1\47\3\5\1\50"+
    "\1\51\1\52\2\5\1\53\3\5\1\54\1\5\1\55"+
    "\1\5\1\56\3\5\1\57\1\5\1\60\1\61\2\5"+
    "\1\62\1\63\4\5\1\64\5\5\1\65\2\5\1\66"+
    "\1\67\1\5\1\70";

  private static int [] zzUnpackAction() {
    int [] result = new int[161];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\73\0\166\0\261\0\166\0\354\0\166\0\u0127"+
    "\0\u0162\0\u019d\0\166\0\u01d8\0\u0213\0\u024e\0\u0289\0\u02c4"+
    "\0\u02ff\0\u033a\0\u0375\0\u03b0\0\u03eb\0\u0426\0\u0461\0\166"+
    "\0\166\0\u049c\0\166\0\166\0\166\0\166\0\166\0\166"+
    "\0\166\0\u04d7\0\u0512\0\354\0\u054d\0\u0588\0\166\0\166"+
    "\0\166\0\166\0\166\0\166\0\u05c3\0\u05fe\0\u0639\0\u0674"+
    "\0\u06af\0\u06ea\0\u0725\0\u0760\0\u079b\0\u0162\0\u07d6\0\u0811"+
    "\0\u084c\0\u0887\0\u08c2\0\u08fd\0\u0938\0\u0973\0\u09ae\0\u09e9"+
    "\0\u0a24\0\166\0\166\0\166\0\166\0\166\0\166\0\166"+
    "\0\u0a5f\0\u0a9a\0\u0ad5\0\u0b10\0\u0b4b\0\u0b86\0\u0bc1\0\u0bfc"+
    "\0\u0c37\0\u0c72\0\u0cad\0\u0ce8\0\u0d23\0\u0d5e\0\u0d99\0\u0dd4"+
    "\0\u0e0f\0\u0e4a\0\u0e85\0\u0ec0\0\u0efb\0\u0f36\0\u0f71\0\u0fac"+
    "\0\u0162\0\u0162\0\u0fe7\0\u1022\0\u105d\0\u0162\0\u1098\0\u10d3"+
    "\0\u110e\0\u1149\0\u1184\0\u11bf\0\u11fa\0\u1235\0\u1270\0\u12ab"+
    "\0\u12e6\0\u1321\0\u135c\0\u0162\0\u1397\0\u13d2\0\u140d\0\u0162"+
    "\0\u0162\0\u0162\0\u1448\0\u1483\0\u14be\0\u14f9\0\u1534\0\u156f"+
    "\0\u0162\0\u15aa\0\u0162\0\u15e5\0\u0162\0\u1620\0\u165b\0\u1696"+
    "\0\u0162\0\u16d1\0\u0162\0\u0162\0\u170c\0\u1747\0\u0162\0\u0162"+
    "\0\u1782\0\u17bd\0\u17f8\0\u1833\0\u0162\0\u186e\0\u18a9\0\u18e4"+
    "\0\u191f\0\u195a\0\u0162\0\u1995\0\u19d0\0\u0162\0\u0162\0\u1a0b"+
    "\0\u0162";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[161];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\2\5\1\6\1\7\1\10\1\11\1\3"+
    "\1\12\1\13\1\14\1\11\1\15\3\11\1\16\1\17"+
    "\2\11\1\20\1\21\1\22\3\11\1\23\1\24\2\11"+
    "\1\25\1\11\1\26\2\11\1\27\5\11\1\30\1\31"+
    "\1\32\1\33\1\34\1\35\1\36\1\37\1\40\1\41"+
    "\1\42\1\43\1\44\1\45\1\46\1\47\1\5\71\50"+
    "\1\51\1\52\74\0\1\53\66\0\1\54\7\0\1\7"+
    "\72\0\2\10\71\0\3\11\1\55\2\0\37\11\32\0"+
    "\1\56\1\57\65\0\3\11\1\55\2\0\1\11\1\60"+
    "\3\11\1\61\3\11\1\62\11\11\1\63\13\11\26\0"+
    "\3\11\1\55\2\0\3\11\1\64\1\65\32\11\26\0"+
    "\3\11\1\55\2\0\12\11\1\66\24\11\26\0\3\11"+
    "\1\55\2\0\10\11\1\67\26\11\26\0\3\11\1\55"+
    "\2\0\14\11\1\70\2\11\1\71\17\11\26\0\3\11"+
    "\1\55\2\0\2\11\1\72\13\11\1\73\20\11\26\0"+
    "\3\11\1\55\2\0\15\11\1\74\21\11\26\0\3\11"+
    "\1\55\2\0\1\75\36\11\26\0\3\11\1\55\2\0"+
    "\17\11\1\76\17\11\26\0\3\11\1\55\2\0\25\11"+
    "\1\77\11\11\26\0\3\11\1\55\2\0\24\11\1\100"+
    "\12\11\26\0\3\11\1\55\2\0\33\11\1\101\3\11"+
    "\76\0\1\102\101\0\1\103\63\0\1\104\16\0\1\105"+
    "\53\0\1\106\72\0\1\107\22\0\3\11\3\0\37\11"+
    "\21\0\2\56\1\110\67\56\1\110\12\111\1\112\60\111"+
    "\5\0\3\11\1\55\2\0\2\11\1\113\34\11\26\0"+
    "\3\11\1\55\2\0\6\11\1\114\30\11\26\0\3\11"+
    "\1\55\2\0\13\11\1\115\23\11\26\0\3\11\1\55"+
    "\2\0\1\116\36\11\26\0\3\11\1\55\2\0\6\11"+
    "\1\117\5\11\1\120\2\11\1\121\17\11\26\0\3\11"+
    "\1\55\2\0\22\11\1\122\14\11\26\0\3\11\1\55"+
    "\2\0\6\11\1\123\30\11\26\0\3\11\1\55\2\0"+
    "\11\11\1\124\25\11\26\0\3\11\1\55\2\0\1\11"+
    "\1\125\35\11\26\0\3\11\1\55\2\0\3\11\1\126"+
    "\10\11\1\127\22\11\26\0\3\11\1\55\2\0\17\11"+
    "\1\130\17\11\26\0\3\11\1\55\2\0\2\11\1\131"+
    "\34\11\26\0\3\11\1\55\2\0\13\11\1\132\23\11"+
    "\26\0\3\11\1\55\2\0\14\11\1\133\22\11\26\0"+
    "\3\11\1\55\2\0\26\11\1\134\10\11\26\0\3\11"+
    "\1\55\2\0\33\11\1\135\3\11\26\0\3\11\1\55"+
    "\2\0\35\11\1\136\1\11\21\0\12\111\1\137\60\111"+
    "\11\0\1\110\1\112\65\0\3\11\1\55\2\0\3\11"+
    "\1\140\33\11\26\0\3\11\1\55\2\0\2\11\1\141"+
    "\34\11\26\0\3\11\1\55\2\0\1\142\36\11\26\0"+
    "\3\11\1\55\2\0\1\11\1\143\35\11\26\0\3\11"+
    "\1\55\2\0\21\11\1\144\15\11\26\0\3\11\1\55"+
    "\2\0\1\11\1\145\35\11\26\0\3\11\1\55\2\0"+
    "\1\146\36\11\26\0\3\11\1\55\2\0\1\147\36\11"+
    "\26\0\3\11\1\55\2\0\11\11\1\150\25\11\26\0"+
    "\3\11\1\55\2\0\13\11\1\151\23\11\26\0\3\11"+
    "\1\55\2\0\15\11\1\152\21\11\26\0\3\11\1\55"+
    "\2\0\17\11\1\153\17\11\26\0\3\11\1\55\2\0"+
    "\2\11\1\154\34\11\26\0\3\11\1\55\2\0\3\11"+
    "\1\155\33\11\26\0\3\11\1\55\2\0\6\11\1\156"+
    "\30\11\26\0\3\11\1\55\2\0\2\11\1\157\34\11"+
    "\26\0\3\11\1\55\2\0\3\11\1\160\33\11\26\0"+
    "\3\11\1\55\2\0\27\11\1\161\7\11\26\0\3\11"+
    "\1\55\2\0\34\11\1\162\2\11\26\0\3\11\1\55"+
    "\2\0\25\11\1\163\11\11\21\0\11\111\1\110\1\137"+
    "\60\111\5\0\3\11\1\55\2\0\4\11\1\164\32\11"+
    "\26\0\3\11\1\55\2\0\2\11\1\165\34\11\26\0"+
    "\3\11\1\55\2\0\21\11\1\166\15\11\26\0\3\11"+
    "\1\55\2\0\13\11\1\167\23\11\26\0\3\11\1\55"+
    "\2\0\13\11\1\170\23\11\26\0\3\11\1\55\2\0"+
    "\1\171\36\11\26\0\3\11\1\55\2\0\1\172\36\11"+
    "\26\0\3\11\1\55\2\0\2\11\1\173\34\11\26\0"+
    "\3\11\1\55\2\0\15\11\1\174\21\11\26\0\3\11"+
    "\1\55\2\0\1\175\36\11\26\0\3\11\1\55\2\0"+
    "\15\11\1\176\21\11\26\0\3\11\1\55\2\0\16\11"+
    "\1\177\20\11\26\0\3\11\1\55\2\0\6\11\1\200"+
    "\30\11\26\0\3\11\1\55\2\0\20\11\1\201\16\11"+
    "\26\0\3\11\1\55\2\0\30\11\1\202\6\11\26\0"+
    "\3\11\1\55\2\0\34\11\1\203\2\11\26\0\3\11"+
    "\1\55\2\0\30\11\1\204\6\11\26\0\3\11\1\55"+
    "\2\0\13\11\1\205\23\11\26\0\3\11\1\55\2\0"+
    "\1\206\36\11\26\0\3\11\1\55\2\0\6\11\1\207"+
    "\30\11\26\0\3\11\1\55\2\0\6\11\1\210\30\11"+
    "\26\0\3\11\1\55\2\0\2\11\1\211\34\11\26\0"+
    "\3\11\1\55\2\0\15\11\1\212\21\11\26\0\3\11"+
    "\1\55\2\0\1\213\36\11\26\0\3\11\1\55\2\0"+
    "\1\11\1\214\35\11\26\0\3\11\1\55\2\0\1\11"+
    "\1\215\35\11\26\0\3\11\1\55\2\0\31\11\1\216"+
    "\5\11\26\0\3\11\1\55\2\0\36\11\1\217\26\0"+
    "\3\11\1\55\2\0\3\11\1\220\33\11\26\0\3\11"+
    "\1\55\2\0\2\11\1\221\34\11\26\0\3\11\1\55"+
    "\2\0\16\11\1\222\20\11\26\0\3\11\1\55\2\0"+
    "\10\11\1\223\26\11\26\0\3\11\1\55\2\0\14\11"+
    "\1\224\22\11\26\0\3\11\1\55\2\0\32\11\1\225"+
    "\4\11\26\0\3\11\1\55\2\0\6\11\1\226\30\11"+
    "\26\0\3\11\1\55\2\0\1\11\1\227\35\11\26\0"+
    "\3\11\1\55\2\0\14\11\1\230\22\11\26\0\3\11"+
    "\1\55\2\0\2\11\1\231\34\11\26\0\3\11\1\55"+
    "\2\0\16\11\1\232\20\11\26\0\3\11\1\55\2\0"+
    "\13\11\1\233\23\11\26\0\3\11\1\55\2\0\3\11"+
    "\1\234\33\11\26\0\3\11\1\55\2\0\6\11\1\235"+
    "\30\11\26\0\3\11\1\55\2\0\1\11\1\236\35\11"+
    "\26\0\3\11\1\55\2\0\2\11\1\237\34\11\26\0"+
    "\3\11\1\55\2\0\16\11\1\240\20\11\26\0\3\11"+
    "\1\55\2\0\1\11\1\241\35\11\21\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[6726];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\1\1\1\11\1\1\1\11\3\1\1\11"+
    "\14\1\2\11\1\1\7\11\5\1\6\11\1\0\1\1"+
    "\1\0\22\1\7\11\2\0\24\1\1\0\102\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[161];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Lexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  Lexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzPushbackPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead < 0) {
      return true;
    }
    else {
      zzEndRead+= numRead;
      return false;
    }
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = zzPushbackPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = zzLexicalState;


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 28: 
          { /* ignore */
          }
        case 57: break;
        case 2: 
          { /* do nothing */
          }
        case 58: break;
        case 23: 
          { Parser.strliteral += yytext();
          }
        case 59: break;
        case 45: 
          { return new Symbol(sym.SHELL, yyline, yycolumn, null);
          }
        case 60: break;
        case 4: 
          { /* System.out.println("INTCONST");     */ 

                    Integer n = Integer.parseInt(yytext());
                    return new Symbol(sym.INTCONST, yyline, yycolumn, n);
          }
        case 61: break;
        case 35: 
          { /* System.out.println("exit");         */ return new Symbol(sym.EXIT, yyline, yycolumn, null);
          }
        case 62: break;
        case 46: 
          { /* System.out.println("events");       */ return new Symbol(sym.EVENTS, yyline, yycolumn, null);
          }
        case 63: break;
        case 55: 
          { /* System.out.println("statechart");   */ return new Symbol(sym.STATECHART, yyline, yycolumn, null);
          }
        case 64: break;
        case 8: 
          { /* System.out.println("DOT");          */ return new Symbol(sym.DOT, yyline, yycolumn, null);
          }
        case 65: break;
        case 48: 
          { /* System.out.println("source");       */ return new Symbol(sym.SRC, yyline, yycolumn, null);
          }
        case 66: break;
        case 27: 
          { /* System.out.println("RTRI");         */ return new Symbol(sym.RTRI, yyline, yycolumn, null);
          }
        case 67: break;
        case 1: 
          { throw new Error("Illegal character <"+yytext()+">");
          }
        case 68: break;
        case 37: 
          { /* System.out.println("true");         */ return new Symbol(sym.TRUE, yyline, yycolumn, null);
          }
        case 69: break;
        case 10: 
          { /* System.out.println("COLON");        */ return new Symbol(sym.COLON, yyline, yycolumn, null);
          }
        case 70: break;
        case 9: 
          { /* System.out.println("HASH");         */ return new Symbol(sym.HASH, yyline, yycolumn, null);
          }
        case 71: break;
        case 5: 
          { /* System.out.println("identifier = " + yytext()); */ 
                                                 return new Symbol(sym.IDENTIFIER, yyline, yycolumn, yytext());
          }
        case 72: break;
        case 34: 
          { /* System.out.println("GE");           */ return new Symbol(sym.GE, yyline, yycolumn, null);
          }
        case 73: break;
        case 21: 
          { /* System.out.println("GT");           */ return new Symbol(sym.GT, yyline, yycolumn, null);
          }
        case 74: break;
        case 44: 
          { /* System.out.println("guard");        */ return new Symbol(sym.GUARD, yyline, yycolumn, null);
          }
        case 75: break;
        case 30: 
          { /* System.out.println("assign");       */ return new Symbol(sym.ASSIGN, yyline, yycolumn, null);
          }
        case 76: break;
        case 6: 
          { /* System.out.println("DIV");          */ return new Symbol(sym.DIV, yyline, yycolumn, null);
          }
        case 77: break;
        case 38: 
          { /* System.out.println("type"); */         return new Symbol(sym.TYPE, yyline, yycolumn, null);
          }
        case 78: break;
        case 26: 
          { /* System.out.println("OR");           */ return new Symbol(sym.OR, yyline, yycolumn, null);
          }
        case 79: break;
        case 3: 
          { /* System.out.println("SUB");          */ return new Symbol(sym.SUB, yyline, yycolumn, null);
          }
        case 80: break;
        case 29: 
          { /* System.out.println("if");           */ return new Symbol(sym.IF, yyline, yycolumn, null);
          }
        case 81: break;
        case 11: 
          { /* System.out.println("EQ");           */ return new Symbol(sym.EQ, yyline, yycolumn, null);
          }
        case 82: break;
        case 19: 
          { /* System.out.println("ADD");          */ return new Symbol(sym.ADD, yyline, yycolumn, null);
          }
        case 83: break;
        case 40: 
          { /* System.out.println("types"); */        return new Symbol(sym.TYPES, yyline, yycolumn, null);
          }
        case 84: break;
        case 53: 
          { /* System.out.println("functions");    */ return new Symbol(sym.FUNCTIONS, yyline, yycolumn, null);
          }
        case 85: break;
        case 52: 
          { return new Symbol(sym.HISTORY, yyline, yycolumn, null);
          }
        case 86: break;
        case 36: 
          { /* System.out.println("else");         */ return new Symbol(sym.ELSE, yyline, yycolumn, null);
          }
        case 87: break;
        case 56: 
          { /* System.out.println("destination");  */ return new Symbol(sym.DEST, yyline, yycolumn, null);
          }
        case 88: break;
        case 33: 
          { /* System.out.println("LE");           */ return new Symbol(sym.LE, yyline, yycolumn, null);
          }
        case 89: break;
        case 41: 
          { /* System.out.println("while");        */ return new Symbol(sym.WHILE, yyline, yycolumn, null);
          }
        case 90: break;
        case 42: 
          { /* System.out.println("false");        */ return new Symbol(sym.FALSE, yyline, yycolumn, null);
          }
        case 91: break;
        case 20: 
          { /* System.out.println("LT");           */ return new Symbol(sym.LT, yyline, yycolumn, null);
          }
        case 92: break;
        case 43: 
          { /* System.out.println("state");        */ return new Symbol(sym.STATE, yyline, yycolumn, null);
          }
        case 93: break;
        case 32: 
          { /* System.out.println("LTRI");         */ return new Symbol(sym.LTRI, yyline, yycolumn, null);
          }
        case 94: break;
        case 50: 
          { return new Symbol(sym.REGION, yyline, yycolumn, null);
          }
        case 95: break;
        case 47: 
          { /* System.out.println("struct");       */ return new Symbol(sym.STRUCT, yyline, yycolumn, null);
          }
        case 96: break;
        case 13: 
          { /* System.out.println("RBRACE");       */ return new Symbol(sym.RBRACE, yyline, yycolumn, null);
          }
        case 97: break;
        case 16: 
          { /* System.out.println("SEMICOLON");    */ return new Symbol(sym.SEMICOLON, yyline, yycolumn, null);
          }
        case 98: break;
        case 51: 
          { /* System.out.println("trigger");      */ return new Symbol(sym.TRIGGER, yyline, yycolumn, null);
          }
        case 99: break;
        case 22: 
          { // System.out.println("String literal - begin.");
                              Parser.strliteral = "";
                              yybegin(STRLITERAL);
          }
        case 100: break;
        case 12: 
          { /* System.out.println("LBRACE");       */ return new Symbol(sym.LBRACE, yyline, yycolumn, null);
          }
        case 101: break;
        case 7: 
          { /* System.out.println("MUL");          */ return new Symbol(sym.MUL, yyline, yycolumn, null);
          }
        case 102: break;
        case 31: 
          { /* System.out.println("NE");           */ return new Symbol(sym.NE, yyline, yycolumn, null);
          }
        case 103: break;
        case 49: 
          { /* System.out.println("action");       */ return new Symbol(sym.ACTION, yyline, yycolumn, null);
          }
        case 104: break;
        case 15: 
          { /* System.out.println("RPAREN");       */ return new Symbol(sym.RPAREN, yyline, yycolumn, null);
          }
        case 105: break;
        case 39: 
          { /* System.out.println("entry");        */ return new Symbol(sym.ENTRY, yyline, yycolumn, null);
          }
        case 106: break;
        case 25: 
          { throw new Error("Unterminated string literal");
          }
        case 107: break;
        case 24: 
          { /*
                              System.out.println("String literal - end. Saw '"
                                + Parser.strliteral + "'");
                              */
                              yybegin(YYINITIAL);
                              return new Symbol(sym.STRLITERAL, yyline, yycolumn, Parser.strliteral);
          }
        case 108: break;
        case 18: 
          { /* System.out.println("AND");          */ return new Symbol(sym.AND, yyline, yycolumn, null);
          }
        case 109: break;
        case 14: 
          { /* System.out.println("LPAREN");       */ return new Symbol(sym.LPAREN, yyline, yycolumn, null);
          }
        case 110: break;
        case 54: 
          { /* System.out.println("transition");   */ return new Symbol(sym.TRANSITION, yyline, yycolumn, null);
          }
        case 111: break;
        case 17: 
          { /* System.out.println("COMMA");        */ return new Symbol(sym.COMMA, yyline, yycolumn, null);
          }
        case 112: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { return new java_cup.runtime.Symbol(sym.EOF); }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
