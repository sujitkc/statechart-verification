MYLIBPATH=./lib
MYCLASSPATH="./classes/:"${MYLIBPATH}"/java-cup-11a.jar"
FE=frontend
Analyser : Analyser.java ${FE}/Lexer.java sym.java ast/*.java
	if [ ! -d classes ]; then \
		mkdir classes; \
	fi;
	javac -Xlint:-unchecked -d classes *.java ${FE}/*.java ast/*.java utilities/*.java utilities/prettyprint/*.java metric/*.java -cp ${MYLIBPATH}/java-cup-11a.jar:${MYLIBPATH}/jflex-1.4.1.jar:ast:./:classes;

sym.java : ${FE}/parser.cup
	java -jar ${MYLIBPATH}/java-cup-11a.jar -parser Parser -dump < ${FE}/parser.cup

${FE}/Lexer.java : ${FE}/lexer.flex
	java -jar ${MYLIBPATH}/jflex-1.4.1.jar ${FE}/lexer.flex

test : ${FE}/Lexer.java sym.java ast/*.java program/*.java
	javac -Xlint:-unchecked -d classes *.java ast/*.java program/*.java metric/*.java testcases/*.java -cp ${MYLIBPATH}/java-cup-11a.jar:${MYLIBPATH}/jflex-1.4.1.jar:${MYLIBPATH}/junit-4.8.1.jar:ast:classes:./;

rt :
	java -cp ~/My-Downloads/junit-4.8.1.jar:classes/:${MYCLASSPATH}:. org.junit.runner.JUnitCore testcases.${testcase}

clean:
	rm Parser.java sym.java ${FE}/Lexer.java
	rm -rf classes/*

constabl-lp:
	java -jar lib/jflex-1.4.1.jar frontend/lexer.flex;
	java -jar lib/java-cup-11a.jar -parser Parser -dump < frontend/parser.cup;

constabl-build:
	javac -Xlint:-unchecked -d classes *.java ast/*.java program/*.java metric/*.java simulator/*.java  -cp ./lib/java-cup-11a.jar:./lib/jflex-1.4.1.jar:./lib/junit-4.8.1.jar:ast:classes:simulator:./;
	javac -Xlint:-unchecked -d classes *.java frontend/*.java ast/*.java utilities/*.java utilities/prettyprint/*.java metric/*.java constabl/*.java constabl/actionprogram/*.java constabl/consimulator/*.java constabl/actionprogram/*.java constabl/automaton/*.java testcases/*.java  -cp ./lib/java-cup-11a.jar:./lib/jflex-1.4.1.jar:./lib/junit-4.8.1.jar:ast:classes:simulator:./;
	javac -Xlint:-unchecked -d classes simulator/*.java constablsim/*.java constablsim/ast/*.java constablsim/ast/connectors/*.java testcases/*.java -cp ./lib/java-cup-11a.jar:./lib/jflex-1.4.1.jar:./lib/junit-4.8.1.jar:ast:classes:simulator:./;

constabl-test:
	java -cp lib/java-cup-11a.jar:lib/jflex-1.4.1.jar:lib/junit-4.8.1.jar:ast:classes:./ org.junit.runner.JUnitCore testcases.TestConStaBLSimulator

stabl-flatten-test:
	java -cp lib/java-cup-11a.jar:lib/jflex-1.4.1.jar:lib/junit-4.8.1.jar:ast:classes:./ org.junit.runner.JUnitCore testcases.TestFlatten

run: Analyser.java
	java -cp ${MYCLASSPATH} analyse/Analyser data/t1.stbl
	clang-format-8 output.cxx -i
# javac -Xlint:-unchecked -d classes *.java ast/*.java program/*.java metric/*.java testcases/*.java -cp "lib/java-cup-11a.jar;lib/jflex-1.4.1.jar;lib/junit-4.8.1.jar;ast;classes;./"
# javac -Xlint:-unchecked -d classes *.java frontend/*.java ast/*.java utilities/*.java utilities/prettyprint/*.java metric/*.java -cp "lib/java-cup-11a.jar;lib/jflex-1.4.1.jar;lib/junit-4.8.1.jar;ast;classes;./"
# java -cp "lib/java-cup-11a.jar;lib/jflex-1.4.1.jar;lib/junit-4.8.1.jar;ast;classes;./" org.junit.runner.JUnitCore testcases.TestSimulator
