package constabl.automaton;
import ast.*;
import java.util.*;
public interface Graph {
    public List<Statement> getReadyStatement(List<Statement> current);
}
