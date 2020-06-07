#MYLIBPATH=/home/redhood/Desktop/CollegeWork/Semester_6/Programming_Languages/Project/Code
MYLIBPATH=/home/advait/Desktop/Programming-Languages/StatechartSimulator/
MYCLASSPATH="./classes/:"${MYLIBPATH}"/cup-11a.jar"
FE=frontend
Analyser : Analyser.java ${FE}/Lexer.java sym.java ast/*.java
	if [ ! -d classes ]; then \
		mkdir classes; \
	fi;
	javac -Xlint:-unchecked -d classes *.java ${FE}/*.java ast/*.java utilities/*.java utilities/prettyprint/*.java translation/Flattener.java metric/*.java -cp ${MYLIBPATH}/cup-11a.jar:${MYLIBPATH}/jflex-1.6.0.jar:ast:./:classes;

sym.java : ${FE}/parser.cup
	java -jar ${MYLIBPATH}/cup-11a.jar -parser Parser -dump < ${FE}/parser.cup

${FE}/Lexer.java : ${FE}/lexer.flex
	java -jar ${MYLIBPATH}/jflex-1.6.0.jar ${FE}/lexer.flex

test : ${FE}/Lexer.java sym.java ast/*.java program/*.java translation/*.java
	javac -Xlint:-unchecked -d classes *.java ast/*.java translation/*.java program/*.java metric/*.java testcases/*.java -cp ${MYLIBPATH}/cup-11a.jar:${MYLIBPATH}/jflex-1.6.0.jar:/home/advait/Desktop/Programming-Languages/StatechartSimulator/junit-4.8.1.jar:ast:classes:./;

rt :
	java -cp /home/redhood/Desktop/CollegeWork/Semester_6/Programming_Languages/Project/Code/junit-4.8.1.jar:classes/:${MYCLASSPATH}:. org.junit.runner.JUnitCore testcases.${testcase}

clean:
	rm Parser.java sym.java ${FE}/Lexer.java
	rm -rf classes/*
