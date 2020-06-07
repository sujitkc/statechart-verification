#JFLEXPATH=/home/redhood/Desktop/CollegeWork/Semester_6/Programming_Languages/Project/Code
#MYCLASSPATH=/home/redhood/Desktop/CollegeWork/Semester_6/Programming_Languages/Project/Code/junit-4.8.1.jar:./:./classes/:${JFLEXPATH}/cup-11a.jar
JFLEXPATH=/home/advait/Desktop/Programming-Languages/StatechartSimulator/
MYCLASSPATH=/home/advait/Desktop/Programming-Languages/StatechartSimulator/junit-4.8.1.jar:./:./classes/:${JFLEXPATH}/cup-11a.jar
if [ $# = 1 ]; then
  echo 1
  testcase=$1
fi
if [ $# = 0 ]; then
  echo 2
  testcase=TestAll
fi

java -cp ${MYCLASSPATH} org.junit.runner.JUnitCore testcases.${testcase}
