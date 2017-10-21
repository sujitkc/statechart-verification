MYLIBPATH=/home/sujit/My-Downloads/source/jflex-1.6.0/lib
MYCLASSPATH="./classes/:"${MYLIBPATH}"/java-cup-11a.jar"
testcases=(
  s2
  s3 # assignment type mismatch
  s4 # misplaced transition
  s5 # unknown state
  s6 # non boolean guard
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
