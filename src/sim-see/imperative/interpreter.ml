exception TypeError of string

let string_of_typ = function
  | Syntax.Expression.Boolean -> "boolean"
  | Syntax.Expression.Integer -> "int"

let rec string_of_expr = function
  | Syntax.Expression.Id(vname)          -> vname
  | Syntax.Expression.IntConst(n)        -> string_of_int n
  | Syntax.Expression.Add(e1, e2)        ->
      (string_of_expr e1) ^ " + " ^ (string_of_expr e2)
  | Syntax.Expression.Sub(e1, e2)        ->
      (string_of_expr e1) ^ " - " ^ (string_of_expr e2)
  | Syntax.Expression.BoolConst(b)       -> string_of_bool b
  | Syntax.Expression.Not(e)             ->
      "not(" ^ string_of_expr e ^ ")"
  | Syntax.Expression.Or(e1, e2)         ->
      "(" ^ (string_of_expr e1) ^ ") or (" ^ 
      (string_of_expr e2) ^ ")"
  | Syntax.Expression.And(e1, e2)        -> 
      "(" ^ (string_of_expr e1) ^ ") and (" ^
      (string_of_expr e2) ^ ")"
  | Syntax.Expression.Equals(e1, e2)     -> "(" ^
      (string_of_expr e1) ^ ") = (" ^ (string_of_expr e2) ^ ")"
  | Syntax.Expression.Input(t)           -> "input(" ^ (string_of_typ t) ^ ")"

let rec string_of_stmt = function
    | Syntax.Program.Assignment(s, e) ->
        s ^ " := " ^ (string_of_expr e) ^ ";"
    | Syntax.Program.Block(slist) -> "{\n" ^
        (List.fold_left (fun x y -> x ^
        (string_of_stmt y) ^ "\n") "" slist) ^ "}"
  | Syntax.Program.If(b, s1, s2)      ->
      "if (" ^(string_of_expr b) ^ ") then (" ^
      (string_of_stmt s1) ^ ") else (" ^
      (string_of_stmt s2) ^ ")"

let string_of_decl (vname, ty) =
  (string_of_typ ty) ^ " " ^ vname
  
let string_of_program p =
  (List.fold_left
    (fun x y -> x ^ (string_of_decl y) ^ ";\n")
     "" p.Syntax.Program.decls)
  ^
  (string_of_stmt p.Syntax.Program.statement)

(* Typecheck declarations *)
let typecheck_declarations decs =
  let rec iter decs env =
    match decs with
      [] -> env
    | (vname, typ) :: tl ->
        iter tl (Env.addBinding vname typ env)
  in
  iter decs Env.EmptyEnv
    
(* Typechecker - Syntax *)
let rec typecheck_expr e env : Syntax.Expression.typ =
  match e with
  | Syntax.Expression.Id(vname)          -> Env.apply vname env
  | Syntax.Expression.IntConst(_)        -> Syntax.Expression.Integer
  | Syntax.Expression.BoolConst(_)       -> Syntax.Expression.Boolean
  | Syntax.Expression.Add(e1, e2)
  | Syntax.Expression.Sub(e1, e2)        ->
      let t1 = typecheck_expr e1 env 
      and t2 = typecheck_expr e2 env in
      (
        match t1, t2 with
          Syntax.Expression.Integer, Syntax.Expression.Integer -> Syntax.Expression.Integer
        | _ -> raise (TypeError("Type error in " ^ (string_of_expr e))) 
      )
  | Syntax.Expression.Not(e) ->
      let etype = typecheck_expr e env in
      if etype = Syntax.Expression.Boolean then
        Syntax.Expression.Boolean
      else
        raise (TypeError("Type error in " ^ (string_of_expr e)))
  | Syntax.Expression.Or(e1, e2)
  | Syntax.Expression.And(e1, e2)
  | Syntax.Expression.Equals(e1, e2) ->
      let e1type = typecheck_expr e1 env 
      and e2type = typecheck_expr e2 env in
      if e1type = e2type then Syntax.Expression.Boolean
      else raise (TypeError("Type error in " ^ (string_of_expr e)))
  | Syntax.Expression.Input(t) -> t

let rec typecheck_stmt stmt env =
  match stmt with
  | Syntax.Program.Assignment(vname, e) ->
      let vtype = Env.apply vname env
      and etype = typecheck_expr e env in
      if vtype = etype then
        None
      else
        raise (TypeError("Type error in statement with " ^ (string_of_expr e))) 
  | Syntax.Program.If(b, s1, s2)      ->
    (
      let btype  = typecheck_expr b env 
      and s1type = typecheck_stmt s1 env 
      and s2type = typecheck_stmt s2 env in
      match btype, s1type, s2type with
        Syntax.Expression.Boolean, None, None -> None
      | _ -> raise (TypeError("Type error in " ^ (string_of_stmt stmt)))
    ) 
  | Syntax.Program.Block(stmt_list) ->
      typecheck_stmtblock stmt_list env 

and typecheck_stmtblock block env =
  match block with
    [] -> None
  | stmt :: tl ->
    (
      let stype = typecheck_stmt stmt env in
        match stype with
          None -> typecheck_stmtblock tl env 
        | Some(_) -> stype
    )
   
(* Typechecker - Program *)
let typecheck_prog (p : Syntax.Program.program) : unit =
  let env = typecheck_declarations p.Syntax.Program.decls in
  let _ = typecheck_stmt p.Syntax.Program.statement env in
    ()

(* Symbolic Execution - Begin *)


module SE = struct
  type setnode =
    | INode of Syntax.Expression.expr * Syntax.Expression.expr * setnode * int (* (variable, value, parent, depth) *)
    | DNode of Syntax.Expression.expr * setnode * int (* (condition, parent, depth) *)
    | EntryNode

  let string_of_setnode = function
    | INode(e1, e2, p, d) ->
        let s1 = string_of_expr e1
        and s2 = string_of_expr e2 in
        "INode(" ^ s1 ^ " : " ^ s2 ^ ")"
    | DNode(e, p, d) -> "DNode(" ^ string_of_expr e ^ ")"
    | EntryNode -> "entry"

  let string_of_list f lst =
    let lst' = List.map f lst in
    List.fold_left (fun x y -> x ^ ", " ^ y) "" lst'

  let rec path n =
    match n with
    | INode(_, _, p, _) -> n :: (path p)
    | DNode(_, p, _) ->  n :: (path p)
    | EntryNode -> [n]

  let string_of_set (d, l) =
    let dpaths = List.map path d
    and lpaths = List.map path l in
    let strlist = string_of_list string_of_setnode in
    let strdpaths = List.map strlist dpaths
    and strlpaths = List.map strlist lpaths in
    let str_strlist = string_of_list (fun s -> s ^ "\n") in
    ("done:\n" ^ (str_strlist strdpaths) ^ "leaves:\n" ^ (str_strlist strlpaths) ^ "\n")
        
  let depth = function
    | INode(e1, e2, p, d) -> d
    | DNode(e, p, d) -> d
    | EntryNode -> 0

  let get_parent = function
    | INode(_, _, p, _) -> Some(p)
    | DNode(_, p, _) -> Some(p)
    | EntryNode -> None

  let rec get_root n = 
    match n with
    | INode(_, _, p, _) -> get_root(p)
    | DNode(_, p, _) -> get_root(p)
    | EntryNode -> n

  let rec get_var_value (v : Syntax.Expression.expr) (n : setnode) : Syntax.Expression.expr =
    match n with
    | INode(v', e, p, _) -> if v = v' then e else (get_var_value v p) 
    | DNode(e, p, _) -> get_var_value v p
    | EntryNode -> raise Not_found("Name not found.")

  let new_symvar_name = 
    let var_count = ref 0 in
    fun () ->
      var_count := !var_count + 1;
      "X" ^ (string_of_int !var_count)  

  let rec se_expr e leaf : Syntax.Expression.expr =
    match e with
    | Syntax.Expression.Id(vname)          -> get_var_value e leaf
    | Syntax.Expression.IntConst(_)        -> e
    | Syntax.Expression.BoolConst(_)       -> e
    | Syntax.Expression.Add(e1, e2)        ->
        let sv1 = se_expr e1 leaf
        and sv2 = se_expr e2 leaf in
        Syntax.Expression.Add(sv1, sv2) 
    | Syntax.Expression.Sub(e1, e2)        ->
        let sv1 = se_expr e1 leaf 
        and sv2 = se_expr e2 leaf in
        Syntax.Expression.Sub(sv1, sv2) 
    | Syntax.Expression.Not(e) ->
        let sv = se_expr e leaf in Syntax.Expression.Not(sv)
    | Syntax.Expression.Or(e1, e2)         ->
        let sv1 = se_expr e1 leaf 
        and sv2 = se_expr e2 leaf in
        Syntax.Expression.Or(sv1, sv2)
    | Syntax.Expression.And(e1, e2)        ->
        let sv1 = se_expr e1 leaf 
        and sv2 = se_expr e2 leaf in
        Syntax.Expression.And(sv1, sv2)
    | Syntax.Expression.Equals(e1, e2)     ->
        let sv1 = se_expr e1 leaf 
        and sv2 = se_expr e2 leaf in
        Syntax.Expression.Equals(sv1, sv2)
    | Syntax.Expression.Input(t) -> Syntax.Expression.Id(new_symvar_name ())

  let se_decl decl leaf maxdepth =
    let (vname, ty) = decl
    and depth' = (depth leaf)  + 1in
    let id = Syntax.Expression.Id(vname)
    and init = Syntax.Expression.ty_init ty in
    let leaf' = INode(id, init, leaf, depth') in
    if depth' = maxdepth then ([leaf'], [])
    else                      ([], [leaf'])

  let se_decl_all block leaves maxdepth =
    let rec loop decl _done_ leaves leaves' maxdepth =
      match leaves with
        [] -> (_done_, leaves')
      | l :: ls' ->
          let d, l = se_decl decl l maxdepth in
            loop decl (d @ _done_) ls' (l @ leaves') maxdepth
    in
    loop block [] leaves [] maxdepth

  let se_decls decls leaf maxdepth =
    let rec loop decls _done_ leaves maxdepth =
      match decls with
        [] -> (_done_, leaves)
      | decl :: decls' ->
        let d, l = se_decl_all decl leaves maxdepth in
         loop decls' (d @ _done_) l maxdepth
    in
    loop decls [] [leaf] maxdepth

  let rec se_stmt stmt leaf maxdepth =
    match stmt with
    | Syntax.Program.Assignment(vname, e) ->
        let se = se_expr e leaf in
        let d = depth leaf + 1 in
        let leaf' = INode(Syntax.Expression.Id(vname), se, leaf, d) in
        if d >= maxdepth then ([leaf'], [])
        else ([], [leaf'])
    | Syntax.Program.If(b, s1, s2)      ->
        let sb = se_expr b leaf in
        let sb' = Syntax.Expression.Not(sb)
        and d = depth leaf + 1 in
        let tnode = DNode(sb, leaf, d)
        and fnode = DNode(sb', leaf, d) in
        let tdone, tleaves = se_stmt s1 tnode maxdepth
        and fdone, fleaves = se_stmt s2 fnode maxdepth in
        (tdone @ fdone, tleaves @ fleaves)
    | Syntax.Program.Block(stmt_list) ->
        se_stmtblock stmt_list leaf maxdepth
  
  and se_stmt_all stmt leaves maxdepth =
    let rec loop s _done_ leaves leaves' maxdepth =
      match leaves with
        [] -> (_done_, leaves')
      | l :: ls ->
          let d', l' = se_stmt s l maxdepth in
            loop s (d' @ _done_) ls (l' @ leaves') maxdepth
    in
    loop stmt [] leaves [] maxdepth

  and se_stmtblock block leaf maxdepth =
    let rec loop block _done_ leaves maxdepth =
      match block with
        [] -> (_done_, leaves)
      | stmt :: stmts ->
         let d, l = se_stmt_all stmt leaves maxdepth in
         loop stmts (d @ _done_) l maxdepth
    in
    loop block [] [leaf] maxdepth
  
  let se_prog (p : Syntax.Program.program) (maxdepth : int) =
    let entry = EntryNode in
    let (ds, ls) = se_decls p.Syntax.Program.decls entry maxdepth in
    let (ds', ls') = se_stmt_all p.Syntax.Program.statement ls maxdepth in
      (ds @ ds', ls')
  end
