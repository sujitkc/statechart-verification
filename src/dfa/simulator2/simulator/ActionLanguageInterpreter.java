package simulator2.simulator;

import java.util.Map;

import ast.*;

public class ActionLanguageInterpreter {

  public ActionLanguageInterpreter() {

  }
/*
AssignmentStatement.java  
ExpressionStatement.java  
StatementList.java  
IfStatement.java
WhileStatement.java
HaltStatement.java        
SkipStatement.java

Name.java                  
*/
  public static void execute(Statement statement, Map<Declaration, Expression> env) throws Exception {
    System.out.println("execute called.");
    if(statement instanceof AssignmentStatement) {
      AssignmentStatement assign = (AssignmentStatement)statement;
      Name lhs = assign.lhs;
      Expression rhs = assign.rhs;
      Expression newvalue = ActionLanguageInterpreter.evaluate(rhs, env);
      Declaration d = lhs.getDeclaration();
      env.put(d, newvalue);
    }
    else if(statement instanceof IfStatement){
      System.out.println("ActionLanguageInterpreter::interpret - if statement detected");
    }
    else if(statement instanceof WhileStatement){
      System.out.println("ActionLanguageInterpreter::interpret - While statement detected");
    }
    else if(statement instanceof SkipStatement){
      System.out.println("ActionLanguageInterpreter::interpret - SkipStatement detected");
    }
    else {
      throw new Exception("ActionLanguageInterpreter::interpret - case not implemented.");
    }
  }

  public static Expression evaluate(Expression expression, Map<Declaration, Expression> env) throws Exception {
    if(
        expression instanceof IntegerConstant ||
        expression instanceof BooleanConstant ||
        expression instanceof StringLiteral
      ) {
      return expression;
    }
    else if(expression instanceof BinaryExpression) {
      BinaryExpression be = (BinaryExpression)expression;
      Expression left = ActionLanguageInterpreter.evaluate(be.left, env);
      Expression right = ActionLanguageInterpreter.evaluate(be.right, env);
      if(be.operator.equals("+")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new IntegerConstant(ileft.value + iright.value);
      }
      else if(be.operator.equals("*")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new IntegerConstant(ileft.value * iright.value);
      }
      else if(be.operator.equals("-")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new IntegerConstant(ileft.value - iright.value);
      }
      else if(be.operator.equals("/")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new IntegerConstant(ileft.value / iright.value);
      }
      else if(be.operator.equals(">=")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new BooleanConstant(ileft.value >= iright.value);
      }
      else if(be.operator.equals(">")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new BooleanConstant(ileft.value > iright.value);
      }
      else if(be.operator.equals("<=")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new BooleanConstant(ileft.value <= iright.value);
      }
      else if(be.operator.equals("<")) {
	IntegerConstant ileft  = (IntegerConstant)left;
	IntegerConstant iright = (IntegerConstant)right;
        return new BooleanConstant(ileft.value < iright.value);
      }
      else if(be.operator.equals("&&")) {
	BooleanConstant bleft  = (BooleanConstant)left;
	BooleanConstant bright = (BooleanConstant)right;
        return new BooleanConstant(bleft.value && bright.value);
      }
      else if(be.operator.equals("||")) {
	BooleanConstant bleft  = (BooleanConstant)left;
	BooleanConstant bright = (BooleanConstant)right;
        return new BooleanConstant(bleft.value || bright.value);
      }
      else if(be.operator.equals("=")) {
          Boolean answer = true;
          if(left instanceof BooleanConstant && right instanceof BooleanConstant) {
            BooleanConstant bleft  = (BooleanConstant)left;
            BooleanConstant bright = (BooleanConstant)right;
            answer = (bleft.value == bright.value);
          }
          else if(left instanceof IntegerConstant && right instanceof IntegerConstant) {
            IntegerConstant ileft  = (IntegerConstant)left;
            IntegerConstant iright = (IntegerConstant)right;
            answer = (ileft.value == iright.value);
          }
          else if(left instanceof Name && right instanceof IntegerConstant){
            IntegerConstant iright = (IntegerConstant)right;
            Declaration d=((Name)left).getDeclaration();
            Expression e = env.get(d);
            if(e instanceof IntegerConstant){
              IntegerConstant ileft = (IntegerConstant)e;
              answer = (ileft.value == iright.value);
            }
          }
        return new BooleanConstant(answer);
      }
    }
    throw new Exception("ActionLanguageInterpreter::evaluate - case not implemented." + expression +" : "+expression.getClass());
  }
}
