package frontend;

import java_cup.runtime.Symbol;
import java.io.FileInputStream;
import java.io.InputStream;

%%
%class Lexer

%line
%column

%cup
%{

%}

%state STRLITERAL

LineTerminator = [\r|\n|\r\n]
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
intconst = [+\-]?0|[1-9][0-9]*
   
identifier = [A-Za-z]([_]?[A-Za-z0-9])*
Comment = {TraditionalComment} | {EndOfLineComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?

%%
<YYINITIAL>{intconst}       {
                    /* System.out.println("INTCONST");     */ 

                    Integer n = Integer.parseInt(yytext());
                    return new Symbol(sym.INTCONST, yyline, yycolumn, n);
                 }
<YYINITIAL>"entry"          { /* System.out.println("entry");        */ return new Symbol(sym.ENTRY, yyline, yycolumn, null);      }
<YYINITIAL>"exit"           { /* System.out.println("exit");         */ return new Symbol(sym.EXIT, yyline, yycolumn, null);       }
//"String"         { /* System.out.println("String");       */ return new Symbol(sym.STRING, yyline, yycolumn, null);     }
<YYINITIAL>"while"          { /* System.out.println("while");        */ return new Symbol(sym.WHILE, yyline, yycolumn, null);      }
<YYINITIAL>"if"             { /* System.out.println("if");           */ return new Symbol(sym.IF, yyline, yycolumn, null);         }
<YYINITIAL>"else"           { /* System.out.println("else");         */ return new Symbol(sym.ELSE, yyline, yycolumn, null);       }
<YYINITIAL>"statechart"     { /* System.out.println("statechart");   */ return new Symbol(sym.STATECHART, yyline, yycolumn, null); }
<YYINITIAL>"state"          { /* System.out.println("state");        */ return new Symbol(sym.STATE, yyline, yycolumn, null);      }
<YYINITIAL>"shell"          { /* System.out.println("shell state");        */ return new Symbol(sym.SHELL, yyline, yycolumn, null);      }
<YYINITIAL>"transition"     { /* System.out.println("transition");   */ return new Symbol(sym.TRANSITION, yyline, yycolumn, null); }
<YYINITIAL>"source"         { /* System.out.println("source");       */ return new Symbol(sym.SRC, yyline, yycolumn, null);        }
<YYINITIAL>"destination"    { /* System.out.println("destination");  */ return new Symbol(sym.DEST, yyline, yycolumn, null);       }
<YYINITIAL>"trigger"        { /* System.out.println("trigger");      */ return new Symbol(sym.TRIGGER, yyline, yycolumn, null);    }
<YYINITIAL>"guard"          { /* System.out.println("guard");        */ return new Symbol(sym.GUARD, yyline, yycolumn, null);      }
<YYINITIAL>"action"         { /* System.out.println("action");       */ return new Symbol(sym.ACTION, yyline, yycolumn, null);     }
<YYINITIAL>"true"           { /* System.out.println("true");         */ return new Symbol(sym.TRUE, yyline, yycolumn, null);       }
<YYINITIAL>"false"          { /* System.out.println("false");        */ return new Symbol(sym.FALSE, yyline, yycolumn, null);      }
<YYINITIAL>"struct"         { /* System.out.println("struct");       */ return new Symbol(sym.STRUCT, yyline, yycolumn, null);     }
<YYINITIAL>"types"          { /* System.out.println("types"); */        return new Symbol(sym.TYPES, yyline, yycolumn, null);      }
<YYINITIAL>"type"           { /* System.out.println("type"); */         return new Symbol(sym.TYPE, yyline, yycolumn, null);       }
<YYINITIAL>"events"         { /* System.out.println("events");       */ return new Symbol(sym.EVENTS, yyline, yycolumn, null);     }
<YYINITIAL>"functions"      { /* System.out.println("functions");    */ return new Symbol(sym.FUNCTIONS, yyline, yycolumn, null);  }
<YYINITIAL>"HISTORY"        {                                           return new Symbol(sym.HISTORY, yyline, yycolumn, null);    }
//<YYINITIAL>"SHELL"          {                                           return new Symbol(sym.SHELL, yyline, yycolumn, null);      }
//<YYINITIAL>"REGION"         {                                           return new Symbol(sym.REGION, yyline, yycolumn, null);     }
/*<YYINITIAL>"FIN"            {                                           return new Symbol(sym.FIN, yyline, yycolumn, null);        }*/

<YYINITIAL>"."              { /* System.out.println("DOT");          */ return new Symbol(sym.DOT, yyline, yycolumn, null);        }
<YYINITIAL>"#"              { /* System.out.println("HASH");         */ return new Symbol(sym.HASH, yyline, yycolumn, null);       }
<YYINITIAL>":="             { /* System.out.println("assign");       */ return new Symbol(sym.ASSIGN, yyline, yycolumn, null);     }
//"["              { /* System.out.println("LPAR_SQ");      */ return new Symbol(sym.LPAR_SQ, yyline, yycolumn, null);    }
//"]"              { /* System.out.println("RPAR_SQ");      */ return new Symbol(sym.RPAR_SQ, yyline, yycolumn, null);    }
<YYINITIAL>"{"              { /* System.out.println("LBRACE");       */ return new Symbol(sym.LBRACE, yyline, yycolumn, null);     }
<YYINITIAL>"}"              { /* System.out.println("RBRACE");       */ return new Symbol(sym.RBRACE, yyline, yycolumn, null);     }
<YYINITIAL>"("              { /* System.out.println("LPAREN");       */ return new Symbol(sym.LPAREN, yyline, yycolumn, null);     }
<YYINITIAL>")"              { /* System.out.println("RPAREN");       */ return new Symbol(sym.RPAREN, yyline, yycolumn, null);     }
<YYINITIAL>";"              { /* System.out.println("SEMICOLON");    */ return new Symbol(sym.SEMICOLON, yyline, yycolumn, null);  }
<YYINITIAL>","              { /* System.out.println("COMMA");        */ return new Symbol(sym.COMMA, yyline, yycolumn, null);      }
<YYINITIAL>":"              { /* System.out.println("COLON");        */ return new Symbol(sym.COLON, yyline, yycolumn, null);      }
<YYINITIAL>"&"              { /* System.out.println("AND");          */ return new Symbol(sym.AND, yyline, yycolumn, null);        }
<YYINITIAL>"!="             { /* System.out.println("NE");           */ return new Symbol(sym.NE, yyline, yycolumn, null);         }
<YYINITIAL>"+"              { /* System.out.println("ADD");          */ return new Symbol(sym.ADD, yyline, yycolumn, null);        }
<YYINITIAL>"-"              { /* System.out.println("SUB");          */ return new Symbol(sym.SUB, yyline, yycolumn, null);        }
<YYINITIAL>"*"              { /* System.out.println("MUL");          */ return new Symbol(sym.MUL, yyline, yycolumn, null);        }
<YYINITIAL>"/"              { /* System.out.println("DIV");          */ return new Symbol(sym.DIV, yyline, yycolumn, null);        }
<YYINITIAL>"<|"             { /* System.out.println("LTRI");         */ return new Symbol(sym.LTRI, yyline, yycolumn, null);       }
<YYINITIAL>"|>"             { /* System.out.println("RTRI");         */ return new Symbol(sym.RTRI, yyline, yycolumn, null);       }
<YYINITIAL>">"              { /* System.out.println("GT");           */ return new Symbol(sym.GT, yyline, yycolumn, null);         }
<YYINITIAL>">="             { /* System.out.println("GE");           */ return new Symbol(sym.GE, yyline, yycolumn, null);         }
<YYINITIAL>"<"              { /* System.out.println("LT");           */ return new Symbol(sym.LT, yyline, yycolumn, null);         }
<YYINITIAL>"<="             { /* System.out.println("LE");           */ return new Symbol(sym.LE, yyline, yycolumn, null);         }
<YYINITIAL>"="              { /* System.out.println("EQ");           */ return new Symbol(sym.EQ, yyline, yycolumn, null);         }
<YYINITIAL>"&&"             { /* System.out.println("AND");          */ return new Symbol(sym.AND, yyline, yycolumn, null);        }
<YYINITIAL>"||"             { /* System.out.println("OR");           */ return new Symbol(sym.OR, yyline, yycolumn, null);         }
// "~"              { /* System.out.println("UMINUS");       */ return new Symbol(sym.UMINUS);     }

<YYINITIAL>{identifier}     { /* System.out.println("identifier = " + yytext()); */ 
                                                 return new Symbol(sym.IDENTIFIER, yyline, yycolumn, yytext());    }

<YYINITIAL>{WhiteSpace}     { /* do nothing */}
/* comments */
<YYINITIAL>{Comment}                      { /* ignore */ }
<YYINITIAL>"\""             {
                              // System.out.println("String literal - begin.");
                              Parser.strliteral = "";
                              yybegin(STRLITERAL);
                            }
<STRLITERAL>"\""            {
                              /*
                              System.out.println("String literal - end. Saw '"
                                + Parser.strliteral + "'");
                              */
                              yybegin(YYINITIAL);
                              return new Symbol(sym.STRLITERAL, yyline, yycolumn, Parser.strliteral);
                            }
<STRLITERAL>[^\n]           {  Parser.strliteral += yytext(); }
<STRLITERAL>[\n]            { throw new Error("Unterminated string literal"); }

[^]              { throw new Error("Illegal character <"+yytext()+">"); }
