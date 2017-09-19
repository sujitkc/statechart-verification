import java_cup.runtime.Symbol;
import java.io.FileInputStream;
import java.io.InputStream;



%%
%class Lexer

%line
%column

%cup
%{
/*
	public static void main(String args[]) throws Exception {
		InputStream is = new FileInputStream(args[0]);
		Yylex lexer = new Yylex(is);

		Symbol token = null;
		do {
			System.out.println("attempt to read next token...");
			token = lexer.next_token();
			if(token == null) token=null;
			else System.out.println(token+" "+token.toString());
			System.out.println("hello");
		} while (token != null);
	}
	
	private int countLines(String str){
		int count = 0;
		for(int i = 0; i < str.length(); ++i){
			if(str.charAt(i) == '\n'){
				++count;
			}
		}
		return count;
	}

*/
%}

LineTerminator = [\r|\n|\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
number = 0|[1-9][0-9]*
   
identifier = [A-Za-z][A-Za-z0-9]*


%%

"List"           { return new Symbol(sym.LIST); }
"init"           { return new Symbol(sym.INIT); }
"empty"          { return new Symbol(sym.EMPTY); }
"String"         { return new Symbol(sym.STRING); }
"Number"         { return new Symbol(sym.NUMBER); }
"type"           { return new Symbol(sym.TYPE); }
"click"          { return new Symbol(sym.CLICK); }
"goto"           { return new Symbol(sym.GOTO); }
"defaultState"   { return new Symbol(sym.DEFAULT); }
"belongs_to"     { return new Symbol(sym.BELONGSTO); }
"statechart"     { return new Symbol(sym.STATECHART); }
"state"          { return new Symbol(sym.STATE); }
"source"         { return new Symbol(sym.SRC); }
"destination"    { return new Symbol(sym.DEST); }
"done"           { return new Symbol(sym.DONE); }



":="             { return new Symbol(sym.ASSIGN); }
"["              { return new Symbol(sym.LPAR_SQ); }
"]"              { return new Symbol(sym.RPAR_SQ); }
"."              { return new Symbol(sym.DOT); }
"{"              { return new Symbol(sym.LBRACE); }
"}"              { return new Symbol(sym.RBRACE); }
";"              { return new Symbol(sym.SEMICOLON); }
","              { return new Symbol(sym.COMMA); }
":"              { return new Symbol(sym.COLON); }
"&"              { return new Symbol(sym.AND); }
"!="             { return new Symbol(sym.NEQ); }
{identifier}     { System.out.println("identifier = " + yytext()); return new Symbol(sym.IDENTIFIER,yytext()); 	}
{number}         { return new Symbol(sym.CONSTANT); }
{WhiteSpace}     { System.out.println("WS");/* do nothing */}

[^]              { throw new Error("Illegal character <"+yytext()+">"); }
