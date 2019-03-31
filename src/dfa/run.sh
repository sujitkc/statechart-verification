JFLEXPATH=D:/karthika/tools/Statechart/lib
MYCLASSPATH=~${JFLEXPATH}/junit-4.8.1.jar;./;./classes/;${JFLEXPATH}/java-cup-11a.jar
MYLIBPATH=D:/karthika/tools/Statechart/lib
if [ $# = 1 ]; then
  echo 1
  testcase=$1
fi
if [ $# = 2 ]; then
  echo 1
  echo $2
  testcase=$1
fi
if [ $# = 0 ]; then
  echo 2
  testcase=TestAll
fi

java -Dfilename=$2 -cp "${MYLIBPATH}/java-cup-11a.jar;${MYLIBPATH}/jflex-1.6.0.jar;${MYLIBPATH}/junit-4.8.1.jar;ast;classes;./;" org.junit.runner.JUnitCore testcases.${testcase} 