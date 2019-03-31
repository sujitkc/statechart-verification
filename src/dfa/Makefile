MYLIBPATH=D:/karthika/tools/Statechart/lib
MYCLASSPATH="./classes/:"${MYLIBPATH}"/java-cup-11a.jar"
FE=frontend
Analyser : Analyser.java ${FE}/Lexer.java sym.java ast/*.java
	if [ ! -d classes ]; then \
		mkdir classes; \
	fi;
	javac -Xlint:-unchecked -d classes *.java ${FE}/*.java ast/*.java translation/Flattener.java metric/*.java -cp ${MYLIBPATH}/java-cup-11a.jar

sym.java : ${FE}/parser.cup
	java -jar ${MYLIBPATH}/java-cup-11a.jar -parser Parser -dump < ${FE}/parser.cup

${FE}/Lexer.java : ${FE}/lexer.flex
	java -jar ${MYLIBPATH}/jflex-1.6.0.jar ${FE}/lexer.flex

test : ${FE}/Lexer.java sym.java ast/*.java translation/*.java
	javac -Xlint:-unchecked -d classes -cp "${MYLIBPATH}/java-cup-11a.jar;${MYLIBPATH}/jflex-1.6.0.jar;${MYLIBPATH}/junit-4.8.1.jar;ast;classes;./;" *.java ast/*.java translation/Flattener.java metric/*.java testcases/*.java 
rt :
	java -cp ${MYLIBPATH}/junit-4.8.1.jar:classes/:${MYCLASSPATH}:. org.junit.runner.JUnitCore testcases.${testcase}

clean:
	rm Parser.java sym.java ${FE}/Lexer.java
	rm -rf classes/*
