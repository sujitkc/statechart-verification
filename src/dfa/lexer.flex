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

LineTerminator = [\r|\n|\r\n]
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
intconst = 0|[1-9][0-9]*
   
identifier = [A-Za-z][A-Za-z0-9]*
Comment = {TraditionalComment} | {EndOfLineComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?

%%

//"List"           { /* System.out.println("List");         */ return new Symbol(sym.LIST, yyline, yycolumn, null);       }
//"entry"          { /* System.out.println("entry");        */ return new Symbol(sym.ENTRY, yyline, yycolumn, null);       }
//"String"         { /* System.out.println("String");       */ return new Symbol(sym.STRING, yyline, yycolumn, null);     }
//"int   "         { /* System.out.println("int");          */ return new Symbol(sym.INT, yyline, yycolumn, null);        }
"while"          { /* System.out.println("while");        */ return new Symbol(sym.WHILE, yyline, yycolumn, null);      }
"if"             { /* System.out.println("if");           */ return new Symbol(sym.IF, yyline, yycolumn, null);         }
"else"           { /* System.out.println("else");         */ return new Symbol(sym.ELSE, yyline, yycolumn, null);       }
//"goto"           { /* System.out.println("goto");         */ return new Symbol(sym.GOTO, yyline, yycolumn, null);       }
//"in"             { /* System.out.println("in");           */ return new Symbol(sym.IN, yyline, yycolumn, null);         }
"statechart"     { /* System.out.println("statechart");   */ return new Symbol(sym.STATECHART, yyline, yycolumn, null); }
"state"          { /* System.out.println("state");        */ return new Symbol(sym.STATE, yyline, yycolumn, null);      }
"transition"     { /* System.out.println("transition");   */ return new Symbol(sym.TRANSITION, yyline, yycolumn, null); }
"source"         { /* System.out.println("source");       */ return new Symbol(sym.SRC, yyline, yycolumn, null);        }
"destination"    { /* System.out.println("destination");  */ return new Symbol(sym.DEST, yyline, yycolumn, null);       }
"guard"          { /* System.out.println("guard");        */ return new Symbol(sym.GUARD, yyline, yycolumn, null);      }
"action"         { /* System.out.println("action");       */ return new Symbol(sym.ACTION, yyline, yycolumn, null);     }
//"done"           { /* System.out.println("done");         */ return new Symbol(sym.DONE, yyline, yycolumn, null);       }
"true"           { /* System.out.println("true");         */ return new Symbol(sym.TRUE, yyline, yycolumn, null);       }
"false"          { /* System.out.println("false");        */ return new Symbol(sym.FALSE, yyline, yycolumn, null);      }
"struct"         { /* System.out.println("struct");       */ return new Symbol(sym.STRUCT, yyline, yycolumn, null);     }
//"type"           { /* System.out.println("type");         */ return new Symbol(sym.TYPE, yyline, yycolumn, null);       }
"events"         { /* System.out.println("events");       */ return new Symbol(sym.EVENTS, yyline, yycolumn, null);     }
"functions"      { /* System.out.println("functions");    */ return new Symbol(sym.FUNCTIONS, yyline, yycolumn, null);  }


"."              { /* System.out.println("DOT");          */ return new Symbol(sym.DOT, yyline, yycolumn, null);        }
"#"              { /* System.out.println("HASH");         */ return new Symbol(sym.HASH, yyline, yycolumn, null);       }
":="             { /* System.out.println("assign");       */ return new Symbol(sym.ASSIGN, yyline, yycolumn, null);     }
//"["              { /* System.out.println("LPAR_SQ");      */ return new Symbol(sym.LPAR_SQ, yyline, yycolumn, null);    }
//"]"              { /* System.out.println("RPAR_SQ");      */ return new Symbol(sym.RPAR_SQ, yyline, yycolumn, null);    }
"{"              { /* System.out.println("LBRACE");       */ return new Symbol(sym.LBRACE, yyline, yycolumn, null);     }
"}"              { /* System.out.println("RBRACE");       */ return new Symbol(sym.RBRACE, yyline, yycolumn, null);     }
"("              { /* System.out.println("LPAREN");       */ return new Symbol(sym.LPAREN, yyline, yycolumn, null);     }
")"              { /* System.out.println("RPAREN");       */ return new Symbol(sym.RPAREN, yyline, yycolumn, null);     }
";"              { /* System.out.println("SEMICOLON");    */ return new Symbol(sym.SEMICOLON, yyline, yycolumn, null);  }
","              { /* System.out.println("COMMA");        */ return new Symbol(sym.COMMA, yyline, yycolumn, null);      }
":"              { /* System.out.println("COLON");        */ return new Symbol(sym.COLON, yyline, yycolumn, null);      }
"&"              { /* System.out.println("AND");          */ return new Symbol(sym.AND, yyline, yycolumn, null);        }
"!="             { /* System.out.println("NE");           */ return new Symbol(sym.NE, yyline, yycolumn, null);         }
"+"              { /* System.out.println("ADD");          */ return new Symbol(sym.ADD, yyline, yycolumn, null);        }
"-"              { /* System.out.println("SUB");          */ return new Symbol(sym.SUB, yyline, yycolumn, null);        }
"*"              { /* System.out.println("MUL");          */ return new Symbol(sym.MUL, yyline, yycolumn, null);        }
"/"              { /* System.out.println("DIV");          */ return new Symbol(sym.DIV, yyline, yycolumn, null);        }
">"              { /* System.out.println("GT");           */ return new Symbol(sym.GT, yyline, yycolumn, null);         }
">="             { /* System.out.println("GE");           */ return new Symbol(sym.GE, yyline, yycolumn, null);         }
"<"              { /* System.out.println("LT");           */ return new Symbol(sym.LT, yyline, yycolumn, null);         }
"<="             { /* System.out.println("LE");           */ return new Symbol(sym.LE, yyline, yycolumn, null);         }
"="              { /* System.out.println("EQ");           */ return new Symbol(sym.EQ, yyline, yycolumn, null);         }
"&&"             { /* System.out.println("AND");          */ return new Symbol(sym.AND, yyline, yycolumn, null);        }
"||"             { /* System.out.println("OR");           */ return new Symbol(sym.OR, yyline, yycolumn, null);         }
// "~"              { /* System.out.println("UMINUS");       */ return new Symbol(sym.UMINUS);     }



{identifier}     { /* System.out.println("identifier = " + yytext()); */ 
                                                 return new Symbol(sym.IDENTIFIER, yyline, yycolumn, yytext());    }
{intconst}       {
                    /* System.out.println("INTCONST");     */ 

                    Integer n = Integer.parseInt(yytext());
                    return new Symbol(sym.INTCONST, yyline, yycolumn, n);
                 }
{WhiteSpace}     { /* do nothing */}
/* comments */
{Comment}                      { /* ignore */ }

[^]              { throw new Error("Illegal character <"+yytext()+">"); }
