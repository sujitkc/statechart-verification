package simulator;

import java.util.Scanner;

import ast.*;

/* EvaluateExpression class - Contains all the static methods required to evaluate expressions - regular or binary. */
public class EvaluateExpression{
    public static Expression evaluate(Expression e){
        try {
          if(e instanceof BinaryExpression)
          {
           return evaluateBinaryExpression((BinaryExpression)e);
          }
          else if(e instanceof Name)
            return Simulator.eState.getValue(((Name)e).getDeclaration());
          else if(e instanceof BooleanConstant || e instanceof IntegerConstant || e instanceof StringLiteral)
            return e;
          else if(e instanceof FunctionCall)
            return evaluateFunction((FunctionCall)e);
        }
        catch (Exception exc)
        {
          System.out.println("Unknown Type of Expression!\n");
          return null;
        }
        return null; // the execution should not get to here

        
    }

    // Implementation to take input as a function call
    public static Expression evaluateFunction(FunctionCall f){
        // Evaluate the input function
        if(f.getName().equals("input")){
            System.out.println("Enter the value of the variable: ");
            Scanner sc = new Scanner(System.in);
            int temp = sc.nextInt();
            return (Expression)(new IntegerConstant(temp));
        }
        return null;
    }

    /* Implementation to evaluate binary expressions */
    public static Expression evaluateBinaryExpression(BinaryExpression e){
        Expression lhs = null;
        Expression rhs = null;
        if(isConstantExpression(e.left))
          lhs = e.left;
        else if(e.left instanceof Name)
          lhs =  Simulator.eState.getValue(((Name)e.left).getDeclaration());
        else
          lhs = evaluateBinaryExpression((BinaryExpression)e.left);
        if(isConstantExpression(e.right))
          rhs = e.right;
        else if(e.right instanceof Name)
          rhs = Simulator.eState.getValue(((Name)e.right).getDeclaration());
        else
          rhs = evaluateBinaryExpression((BinaryExpression)e.right);
        
        // for boolean 
        if(e.operator.equals("&&") && lhs instanceof BooleanConstant)
          return (Expression)(new BooleanConstant(((BooleanConstant)(lhs)).value && ((BooleanConstant)(rhs)).value));
        else if(e.operator.equals("||") && lhs instanceof BooleanConstant)
          return (Expression)(new BooleanConstant(((BooleanConstant)(lhs)).value || ((BooleanConstant)(rhs)).value));
        // for integers which return integers
        else if(e.operator.equals("+") && lhs instanceof IntegerConstant)
          return (Expression)(new IntegerConstant(((IntegerConstant)(lhs)).value + ((IntegerConstant)(rhs)).value));
        else if(e.operator.equals("-") && lhs instanceof IntegerConstant)
          return (Expression)(new IntegerConstant(((IntegerConstant)(lhs)).value - ((IntegerConstant)(rhs)).value));
        // this should rarely ever occur as we need to make sure we are dealing with integers
          else if(e.operator.equals("/") && lhs instanceof IntegerConstant)
          return (Expression)(new IntegerConstant(((IntegerConstant)(lhs)).value / ((IntegerConstant)(rhs)).value));
        else if(e.operator.equals("*") && lhs instanceof IntegerConstant)
          return (Expression)(new IntegerConstant(((IntegerConstant)(lhs)).value * ((IntegerConstant)(rhs)).value));
        // for integers which return boolean
        else if(e.operator.equals(">=") && lhs instanceof IntegerConstant)
          return (Expression)(new BooleanConstant(((IntegerConstant)(lhs)).value >= ((IntegerConstant)(rhs)).value));
        else if(e.operator.equals(">") && lhs instanceof IntegerConstant)
          return (Expression)(new BooleanConstant(((IntegerConstant)(lhs)).value > ((IntegerConstant)(rhs)).value));
        else if(e.operator.equals("<=") && lhs instanceof IntegerConstant)
          return (Expression)(new BooleanConstant(((IntegerConstant)(lhs)).value <= ((IntegerConstant)(rhs)).value));
        else if(e.operator.equals("<") && lhs instanceof IntegerConstant)
          return (Expression)(new BooleanConstant(((IntegerConstant)(lhs)).value < ((IntegerConstant)(rhs)).value));
        else if(e.operator.equals("=") && lhs instanceof IntegerConstant)
          return (Expression)(new BooleanConstant(((IntegerConstant)(lhs)).value == ((IntegerConstant)(rhs)).value));
        else if(e.operator.equals("!=") && lhs instanceof IntegerConstant)
          return (Expression)(new BooleanConstant(((IntegerConstant)(lhs)).value != ((IntegerConstant)(rhs)).value));
        else  
          return null;
    }

    // takes an epression and asserts whether it is of a constant type or not
    static boolean isConstantExpression(Expression e) 
    {
      if(e instanceof BooleanConstant || e instanceof IntegerConstant || e instanceof StringLiteral)
        return true;
      return false;
    }
}
