let test prog maxdepth =
  print_endline "Program:";
  print_string ("\n" ^ (Interpreter.string_of_program prog) ^ "\n");
  print_endline ("maxdepth = " ^ (string_of_int maxdepth));
  let _ = (Interpreter.typecheck_prog prog) in
  let set = Interpreter.SE.se_prog prog maxdepth in
  print_endline ("Symbolic execution tree:\n" ^ (Interpreter.SE.string_of_set set))

let read_prog fname =
  let cin = open_in fname in
  let lexbuf = Lexing.from_channel cin in
  Parser.prog Lexer.scan lexbuf

let read_and_test fname maxdepth =
  let prog = read_prog fname in
  test prog maxdepth
 
let test_all () =
  let tests = [("examples/1.txt", 5)] in
  let rec loop = function
    [] -> ()
  | t :: ts ->
      let fname, maxdepth = t in
      read_and_test fname maxdepth;
      loop ts
  in
  loop tests

let evaluate () =
  try
   if Array.length Sys.argv > 1 then
     let cin = open_in Sys.argv.(1) in
     let lexbuf = Lexing.from_channel cin in
     let prog = Parser.prog Lexer.scan lexbuf in
      test prog 7
   else
     test_all ()
  with
    End_of_file -> exit 0
  | Interpreter.TypeError(msg) -> print_string (msg ^ "\n")

let _ = evaluate ()
