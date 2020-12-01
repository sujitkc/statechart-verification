module Expression = struct
type typ =
    Boolean
  | Integer

(* Expression - begin *)
type expr =
  | Id        of string
  | IntConst  of int
  | Add       of expr * expr
  | Sub       of expr * expr
  | BoolConst of bool
  | Not       of expr
  | Or        of expr * expr
  | And       of expr * expr
  | Equals    of expr * expr
  | Input     of typ

let ty_init = function
  Boolean -> BoolConst(false)
| Integer -> IntConst(0)

(* Expression - end *)
end

module Program = struct
  type stmt =
    | Assignment of string * Expression.expr
    | If         of Expression.expr * stmt * stmt
    | Block      of stmt list

  type program = {
    decls    : (string * Expression.typ) list;
    statement : stmt;
  }
end

