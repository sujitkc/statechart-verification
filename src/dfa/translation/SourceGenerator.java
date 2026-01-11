package translation;

import java.io.PrintWriter;


public class SourceGenerator {
    private PrintWriter _writer;
    public SourceGenerator if_statement(String condition) {
        _writer.print ("if (");
        _writer.print(condition);
        _writer.print (") {\n");
        return this;
    }
}

abstract class Node {
    abstract public String toString();
}

class ConditionNode extends Node {
    String condition;
    ArrayList<String> body;
}
