JFLEXPATH=/home/redhood/Desktop/Work/StatechartSimulator
MYCLASSPATH=/home/redhood/Desktop/Work/StatechartSimulator/junit-4.8.1.jar:./:./classes/:${JFLEXPATH}/cup-11a.jar
if [ $# = 1 ]; then
  echo 1
  testcase=$1
fi
if [ $# = 0 ]; then
  echo 2
  testcase=TestAll
fi

java -cp ${MYCLASSPATH} org.junit.runner.JUnitCore testcases.${testcase}
