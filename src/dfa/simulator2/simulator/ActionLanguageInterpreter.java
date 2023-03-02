package simulator2.simulator;

import java.util.Map;

import ast.*;

public class ActionLanguageInterpreter {

  public ActionLanguageInterpreter() {

  }

  public void execute(Statement statement, Map<Declaration, Expression> env) {
    System.out.println("execute called.");
  }

  public Expression evaluate(Expression expression, Map<Declaration, Expression> env) {
    return null;
  }
}
