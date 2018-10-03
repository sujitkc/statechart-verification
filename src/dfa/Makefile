MYLIBPATH=/home/sujit/My-Downloads/source/jflex-1.6.0/lib

Analyser : Analyser.java Lexer.java sym.java Parser.java ast/*.java
	if [ ! -d classes ]; then \
		mkdir classes; \
	fi;
	javac -Xlint:-unchecked -d classes *.java ast/*.java flatten/Flattener.java metric/*.java -cp ${MYLIBPATH}/java-cup-11a.jar:${MYLIBPATH}/jflex-1.6.0.jar:ast:./:classes;

sym.java : parser.cup
	java -jar ${MYLIBPATH}/java-cup-11a.jar -parser Parser -dump < parser.cup

Lexer.java : lexer.flex
	java -jar ${MYLIBPATH}/jflex-1.6.0.jar lexer.flex

clean:
	rm Parser.java sym.java Lexer.java
	rm -rf classes/*
