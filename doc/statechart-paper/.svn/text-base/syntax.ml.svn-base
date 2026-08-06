type statechart =
  state      *    (* initial state *)
  state list *    (* other states *)
  transition list (* transitions *)

type state =
    AtomicState of
      name *  (* State name *)
    | block * (* Entry actions *)
    | block   (* Exit actions *) 
  | NonAtomicState of statechart
  | ShallowHistory
  | DeepHistory
  | Branch

type transition = Transition of
    id * (* source state name *)
    id * (* destination state name *)
    expr  * (* trigger *)
    expr  * (* guard  *)
    block   (* action *)

block = stmt list (* statements *)

stmt = Assignment of 
        expr * (* LHS *)
        expr   (* RHS *)
  | Expr of expr (* independent expressions like 
    arithmetic expressions or function calls *)
  | Return of expr (* Returned value *)
  | If of expr  * (* boolean condition *)
          block * (* then block *)
          block   (* else block *)
  | Loop of expr * (* boolean condition *)
            block  (* loop body *)

expr =
    BinaryExpr of expr   * (* LHS *)
                  binop * (* Operator *)
                  expr     (* RHS *)
  | UnaryExpr of unaryop * (* Unary operator *)
                 expr
  | FunctionCall of expr * (* Function *)
                    expr list (* actual parameters *)
  | Id of string
  | string_literal of string
  | num_const of string
  | PHI (* empty set *)
  | Set of expr list


