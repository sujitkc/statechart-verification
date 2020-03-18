JFLEXPATH=E:/documents/iiit/lib
MYCLASSPATH=${JFLEXPATH}/junit-4.13.jar;${JFLEXPATH}/hamcrest-all-1.3.jar;./;./classes/;${JFLEXPATH}/java-cup-11a.jar
MYLIBPATH=E:/documents/iiit/lib
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

java -cp "E:/documents/iiit/lib/junit-4.13.jar;E:/documents/iiit/lib/hamcrest-all-1.3.jar;./;./classes/;E:/documents/iiit/lib/java-cup-11a.jar" org.junit.runner.JUnitCore testcases.${testcase}
