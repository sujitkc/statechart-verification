MYLIBPATH=/home/harika/Desktop/Github/statechart-verification/source/jflex-1.6.1/lib
MYCLASSPATH="./classes/:"${MYLIBPATH}"/java-cup-11a.jar"


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
