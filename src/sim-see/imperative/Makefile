evaluate : env.cmx syntax.cmx parser.cmx lexer.cmx interpreter.cmx evaluate.cmx 
	ocamlopt -thread syntax.cmx parser.cmx lexer.cmx env.cmx interpreter.cmx evaluate.cmx -o evaluate

parser.ml : parser.mly
	ocamlyacc parser.mly

parser.mli : parser.mly
	ocamlyacc -v parser.mly

lexer.ml : lexer.mll parser.mli
	ocamllex lexer.mll

parser.cmx : syntax.cmi parser.cmi parser.ml
	ocamlopt -c parser.ml

parser.cmi : syntax.cmi parser.mli
	ocamlc -c parser.mli

lexer.cmx :  parser.cmi lexer.ml
	ocamlopt -c lexer.ml

syntax.cmi : syntax.mli
	ocamlc -c syntax.mli

syntax.cmx : syntax.cmi syntax.ml
	ocamlopt -c syntax.ml

interpreter.cmi : syntax.cmi interpreter.mli
	ocamlc -c interpreter.mli

interpreter.cmx : syntax.cmi env.cmi interpreter.cmi interpreter.ml
	ocamlopt -c interpreter.ml

evaluate.cmi : evaluate.mli interpreter.cmi
	ocamlopt -c evaluate.mli

evaluate.cmx :  parser.cmi lexer.cmx syntax.cmi interpreter.cmi evaluate.ml evaluate.cmi
	ocamlopt -c evaluate.ml

env.cmx : syntax.cmi env.ml env.cmi
	ocamlopt -c env.ml

env.cmi : syntax.cmi env.mli
	ocamlc -c env.mli

clean:
	rm *.cmx *.cmi *.o evaluate lexer.ml parser.ml parser.mli
