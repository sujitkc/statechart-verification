(* Environment - begin *)
type ('a, 'b) env =
    EmptyEnv
  | NonEmptyEnv of ('a * 'b) * ('a, 'b) env

let emptyEnv () = EmptyEnv

let addBinding x v env =
  NonEmptyEnv((x, v), env)

let rec apply x env =
  match env with
    EmptyEnv -> raise Not_found
  | NonEmptyEnv((key, value), env') ->
    if x = key then value
    else (apply x env')


(* Environment - end *)
