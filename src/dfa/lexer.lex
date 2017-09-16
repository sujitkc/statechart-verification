import java_cup.runtime.Symbol;
import java.io.FileInputStream;
import java.io.InputStream;



%%
%cup
%line

%{

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
%}
LineTerminator = \r|\n|\r\n
WhiteSpace     = [ \t\f]

number = 0|[1-9][0-9]*
   
identifier = [A-Za-z][A-Za-z0-9]*


%%

"List"					{ return new Symbol(sym.LIST_KEYWORD); }
"init"					{ return new Symbol(sym.init_KEYWORD); }
"empty"					{ return new Symbol(sym.empty_KEYWORD); }
"String"				{ return new Symbol(sym.STRING_KEYWORD); }
"Number"				{ return new Symbol(sym.NUMBER_KEYWORD); }
"type"					{ return new Symbol(sym.TYPE_KEYWORD); }
"click"					{ return new Symbol(sym.CLICK_KEYWORD); }
"goto"					{ return new Symbol(sym.GOTO_KEYWORD); }
"defaultState"					{ return new Symbol(sym.default_KEYWORD); }
"belongs_to"					{ return new Symbol(sym.belongs_to_KEYWORD); }
"state"					{ return new Symbol(sym.STATE_KEYWORD); }
"src"					{ return new Symbol(sym.SRC_KEYWORD); }
"dest"					{ return new Symbol(sym.DEST_KEYWORD); }
"transitions"					{ return new Symbol(sym.TRANSITIONS_KEYWORD); }
"done"					{ return new Symbol(sym.DONE_KEYWORD); }



{identifier}			{ return new Symbol(sym.IDENTIFIER,yytext()); 	}
{number}				{ return new Symbol(sym.CONSTANT); }
":="					{ return new Symbol(sym.OP_ASSIGN); }
" "						{ /*do nothing*/}
"["						{ return new Symbol(sym.LPAR_SQ); }
"]"						{ return new Symbol(sym.RPAR_SQ); }
"."						{ return new Symbol(sym.OP_DOT); }
"{"						{ return new Symbol(sym.LBRACE); }
"}"						{ return new Symbol(sym.RBRACE); }
";"						{ return new Symbol(sym.SEMICOLON); }
","						{ return new Symbol(sym.COMMA); }
":"						{ return new Symbol(sym.COLON); }
"&"						{ return new Symbol(sym.AND); }
"!="					{ return new Symbol(sym.NEQ); }
"\n"					{/* do nothing */}
{LineTerminator}					{/* do nothing */}
{WhiteSpace}					{/* do nothing */}
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }



