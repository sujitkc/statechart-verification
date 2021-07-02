%{
%}

%token          NEWLINE WS COMMA EOF LPAREN RPAREN LBRACE RBRACE SEMICOLON ASSIGN COLON
%token          IF THEN ELSE LET IN RETURN
%token          ADD SUBTRACT EQ
%token          AND OR NOT
%token <int>    INTEGER
%token <string> ID
%token <bool>   BOOLEAN
%token <Syntax.Expression.typ>    TYPE
%token <Syntax.Expression.typ>    INPUT

%type <Syntax.Program.program> prog
%type <Syntax.Expression.expr> expr

%start prog

%left ADD
%left SUBTRACT
%left AND
%left OR
%right NOT
%left RPAREN
%left LPAREN

%% /* Grammar rules and actions follow */

prog : decls stmt {
   {
      Syntax.Program.decls     = $1;
      Syntax.Program.statement = $2;
    }
  }
  ;

decls :
  LBRACE nonemptydecls RBRACE        { List.rev $2 }
  | LBRACE RBRACE                    { []          }
  ;

nonemptydecls :
    nonemptydecls decl             { $2 :: $1  }
  | decl                             { [ $1 ]    }
  ;
  
decl : TYPE ID SEMICOLON %prec RPAREN                  { ($2, $1) }
  ;

stmts :
    nonemptystmts                    { List.rev $1                  }
  |                                  { []                           }
  ;

nonemptystmts :
    nonemptystmts stmt               { $2 :: $1                     }
  | stmt                             { [$1]                         }
  ;

stmt :
    ID ASSIGN expr SEMICOLON         { Syntax.Program.Assignment($1, $3) }
  | IF expr THEN stmt ELSE stmt      { Syntax.Program.If($2, $4, $6)     }
  | LBRACE stmts RBRACE              { Syntax.Program.Block($2)          }
  ;

expr :
  | ID                               { Syntax.Expression.Id($1)             }
  | INTEGER                          { Syntax.Expression.IntConst($1)       }
  | BOOLEAN                          { Syntax.Expression.BoolConst $1       }
  | expr ADD expr                    { Syntax.Expression.Add($1, $3)        }
  | expr SUBTRACT expr               { Syntax.Expression.Sub($1, $3)        }
  | expr AND expr                    { Syntax.Expression.And($1, $3)        }
  | expr OR expr                     { Syntax.Expression.Or($1, $3)         }
  | NOT expr                         { Syntax.Expression.Not($2)            }
  | expr EQ expr                     { Syntax.Expression.Equals($1, $3)     }
  | LPAREN expr RPAREN               { $2                            }
  | INPUT                            { Syntax.Expression.Input($1)          }
;
%%
