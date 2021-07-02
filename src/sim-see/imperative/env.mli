type ('a,'b) env =
    EmptyEnv
  | NonEmptyEnv of ('a * 'b) * ('a, 'b) env

val emptyEnv : unit -> ('a, 'b) env

val addBinding : 'a -> 'b -> ('a, 'b) env -> ('a, 'b) env

val apply : 'a -> ('a, 'b) env -> 'b
