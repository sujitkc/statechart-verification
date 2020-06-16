#JFLEXPATH=/home/sujit/My-Downloads/source/jflex-1.6.0/lib
JFLEXPATH=/home/advait/Desktop/StatechartSimulator/
MYCLASSPATH=~/home/advait/Desktop/StatechartSimulator/junit-4.8.1.jar:./:./classes/:${JFLEXPATH}/java-cup-11a.jar
if [ $# = 1 ]; then
  echo 1
  testcase=$1
fi
if [ $# = 0 ]; then
  echo 2
  testcase=TestAll
fi

java -cp ${MYCLASSPATH} org.junit.runner.JUnitCore testcases.${testcase}
