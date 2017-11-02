MYLIBPATH=/home/harika/Desktop/Github/statechart-verification/source/jflex-1.6.1/lib

Analyser : Analyser.java Lexer.java sym.java Parser.java ast/*.java
	if [ ! -d classes ]; then \
		mkdir classes; \
	fi;
	javac -d classes *.java ast/*.java -cp ${MYLIBPATH}/java-cup-11a.jar:${MYLIBPATH}/jflex-1.6.1.jar:ast:./;

sym.java : parser.cup
	java -jar ${MYLIBPATH}/java-cup-11a.jar -parser Parser < parser.cup

Lexer.java : lexer.flex
	java -jar ${MYLIBPATH}/jflex-1.6.1.jar lexer.flex

clean:
	rm Parser.java sym.java Lexer.java
	rm -rf classes/*



