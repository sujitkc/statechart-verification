MYLIBPATH=/home/sujit/My-Downloads/source/jflex-1.6.0/lib
MYCLASSPATH="./classes/:"${MYLIBPATH}"/java-cup-11a.jar"
testcases=(
  s2
  s3  # error - assignment type mismatch
  s4  # error - misplaced transition
  s5  # error - unknown state
  s6  # error - non boolean guard
  s7  # parenthesised expression, Binary expressions (logical, relational, arithmetic), boolean constant, integer constant, statement list
  s8  # user defined type based on earlier user defined type.
  s9  # error - type error in while condition
  s10 # error - type error in if condition
  s11 # input variables
  s12 # error - duplicate names
  s13 # function declaration
  s14 # error - duplicate names in function declaration
  s15 # error - function declaration unknown return type
  s16 # error - function declaration unknown argument type
  s17 # error - 
  s18 # error - Error - Function call: undeclared function 
  s19 # error - Error - Function call: incorrect number of arguments 
  s20 # error - Error - Function call: incorrect number of arguments 
  s21 # error - Error - Function call: argument type mismatch 
  s22 # error - Error - Function call: argument type mismatch 
);

if [ $# = 1 ]; then
  java -cp ${MYCLASSPATH} Analyser $1
fi
if [ $# = 0 ]; then
  for t in ${testcases[@]}
  do
    echo "Analysing data/${t}.txt"
    java -cp ${MYCLASSPATH} Analyser "data/${t}.txt"
  done
fi
