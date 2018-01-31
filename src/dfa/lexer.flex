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

"List"           { /* System.out.println("List");         */ return new Symbol(sym.LIST);       }
"entry"          { /* System.out.println("entry");        */ return new Symbol(sym.ENTRY);       }
"String"         { /* System.out.println("String");       */ return new Symbol(sym.STRING);     }
"int   "         { /* System.out.println("int");          */ return new Symbol(sym.INT);        }
"while"          { /* System.out.println("while");        */ return new Symbol(sym.WHILE);      }
"if"             { /* System.out.println("if");           */ return new Symbol(sym.IF);         }
"else"           { /* System.out.println("else");         */ return new Symbol(sym.ELSE);       }
"goto"           { /* System.out.println("goto");         */ return new Symbol(sym.GOTO);       }
"in"             { /* System.out.println("in");           */ return new Symbol(sym.IN);         }
"statechart"     { /* System.out.println("statechart");   */ return new Symbol(sym.STATECHART); }
"state"          { /* System.out.println("state");        */ return new Symbol(sym.STATE);      }
"transition"     { /* System.out.println("transition");   */ return new Symbol(sym.TRANSITION); }
"source"         { /* System.out.println("source");       */ return new Symbol(sym.SRC);        }
"destination"    { /* System.out.println("destination");  */ return new Symbol(sym.DEST);       }
"guard"          { /* System.out.println("guard");        */ return new Symbol(sym.GUARD);      }
"action"         { /* System.out.println("action");       */ return new Symbol(sym.ACTION);     }
"done"           { /* System.out.println("done");         */ return new Symbol(sym.DONE);       }
"true"           { /* System.out.println("true");         */ return new Symbol(sym.TRUE);       }
"false"          { /* System.out.println("false");        */ return new Symbol(sym.FALSE);      }
"struct"         { /* System.out.println("struct");       */ return new Symbol(sym.STRUCT);     }
"type"           { /* System.out.println("type");         */ return new Symbol(sym.TYPE);       }
"events"         { /* System.out.println("events");       */ return new Symbol(sym.EVENTS);     }


"."              { /* System.out.println("DOT");          */ return new Symbol(sym.DOT);        }
"#"              { /* System.out.println("HASH");         */ return new Symbol(sym.HASH);       }
":="             { /* System.out.println("assign");       */ return new Symbol(sym.ASSIGN);     }
"["              { /* System.out.println("LPAR_SQ");      */ return new Symbol(sym.LPAR_SQ);    }
"]"              { /* System.out.println("RPAR_SQ");      */ return new Symbol(sym.RPAR_SQ);    }
"{"              { /* System.out.println("LBRACE");       */ return new Symbol(sym.LBRACE);     }
"}"              { /* System.out.println("RBRACE");       */ return new Symbol(sym.RBRACE);     }
"("              { /* System.out.println("LPAREN");       */ return new Symbol(sym.LPAREN);     }
")"              { /* System.out.println("RPAREN");       */ return new Symbol(sym.RPAREN);     }
";"              { /* System.out.println("SEMICOLON");    */ return new Symbol(sym.SEMICOLON);  }
","              { /* System.out.println("COMMA");        */ return new Symbol(sym.COMMA);      }
":"              { /* System.out.println("COLON");        */ return new Symbol(sym.COLON);      }
"&"              { /* System.out.println("AND");          */ return new Symbol(sym.AND);        }
"!="             { /* System.out.println("NE");           */ return new Symbol(sym.NE);         }
"+"              { /* System.out.println("ADD");          */ return new Symbol(sym.ADD);        }
"-"              { /* System.out.println("SUB");          */ return new Symbol(sym.SUB);        }
"*"              { /* System.out.println("MUL");          */ return new Symbol(sym.MUL);        }
"/"              { /* System.out.println("DIV");          */ return new Symbol(sym.DIV);        }
">"              { /* System.out.println("GT");           */ return new Symbol(sym.GT);         }
">="             { /* System.out.println("GE");           */ return new Symbol(sym.GE);         }
"<"              { /* System.out.println("LT");           */ return new Symbol(sym.LT);         }
"<="             { /* System.out.println("LE");           */ return new Symbol(sym.LE);         }
"="              { /* System.out.println("EQ");           */ return new Symbol(sym.EQ);         }
"&&"             { /* System.out.println("AND");          */ return new Symbol(sym.AND);        }
"||"             { /* System.out.println("OR");           */ return new Symbol(sym.OR);         }
// "~"              { /* System.out.println("UMINUS");       */ return new Symbol(sym.UMINUS);     }



{identifier}     { /* System.out.println("identifier = " + yytext()); */ 
                                                 return new Symbol(sym.IDENTIFIER,yytext());    }
{intconst}       {
                    /* System.out.println("INTCONST");     */ 

                    Integer n = Integer.parseInt(yytext());
                    return new Symbol(sym.INTCONST, n);
                 }
{WhiteSpace}     { /* do nothing */}
/* comments */
{Comment}                      { /* ignore */ }

[^]              { throw new Error("Illegal character <"+yytext()+">"); }
