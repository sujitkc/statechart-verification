exception TypeError of string

val string_of_expr : Syntax.Expression.expr -> string
val string_of_program : Syntax.Program.program -> string
val typecheck_expr : Syntax.Expression.expr
                  -> (string, Syntax.Expression.typ) Env.env
                  -> Syntax.Expression.typ

module SE: sig
  type setnode =
    | INode of Syntax.Expression.expr * Syntax.Expression.expr * setnode * int (* (variable, value, parent, depth) *)
    | DNode of Syntax.Expression.expr * setnode * int (* (variable, value, parent, depth) *)
    | EntryNode

  val string_of_list : ('a -> string) -> 'a list -> string
  val string_of_setnode : setnode -> string
  val string_of_set : (setnode list) * (setnode list) -> string
  val depth : setnode -> int
  val get_root : setnode -> setnode
  val get_var_value : Syntax.Expression.expr -> setnode -> Syntax.Expression.expr

  val se_prog : Syntax.Program.program -> int -> (setnode list) * (setnode list)
end

val typecheck_prog : Syntax.Program.program -> unit
