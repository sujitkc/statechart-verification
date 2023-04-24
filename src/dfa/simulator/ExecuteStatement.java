package simulator;
import java.util.List;
import ast.*;

/* ExectueStatement class - Contains all the static methods to execute different types of statements : assignment, conditional, while-loops and expressions.*/
public class ExecuteStatement 
{
    public static void executeStatement(Statement statement) throws Exception 
    {
        try 
        {
          if(statement instanceof StatementList)
            executeStatementList(statement);
          else if(statement instanceof AssignmentStatement)
            executeAssignmentStatement((AssignmentStatement)statement);
          else if(statement instanceof IfStatement)
            executeConditionalStatement((IfStatement)statement);
          else if(statement instanceof WhileStatement)
            executeWhileStatement((WhileStatement)statement);
          else if(statement instanceof ExpressionStatement){
            EvaluateExpression.evaluate(((ExpressionStatement)statement).expression);
          }
          // I still do not completely understand what the next 2 statements do, right now the behavior is defined by my rudimentary understanding
          else if(statement instanceof SkipStatement) 
            return ;
          else if(statement instanceof HaltStatement)
            System.exit(0);
          else
          {
            System.out.println("The Statement Type Could Not Be Identified!\n");
            System.out.println(statement.getClass());
            return ;
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
    }

    /* Implementation for executing assignment statement */
    private static void executeAssignmentStatement(AssignmentStatement assignment) throws Exception
    {
      try {
      Declaration variableDeclaration = assignment.lhs.getDeclaration();
      Simulator.eState.setValue(variableDeclaration, EvaluateExpression.evaluate(assignment.rhs));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    /* Implementation for executing a list of statements */
    private static void executeStatementList(Statement statement) throws Exception
    {
        if(statement instanceof StatementList)
        {
          List<Statement> st_list = ((StatementList)statement).getStatements();
          for(Statement st : st_list)
            executeStatementList(st);
        }
        else 
        {
          try 
          {
            executeStatement(statement); 
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
    }
  
  
  /* Implementation for executing conditional statements */
  private static void executeConditionalStatement(IfStatement c) throws Exception
  {
    try 
    {
    // the condition would either be a straight-forward constant (although it would not make sense to have it at all then) or a binary expression
      if(c.condition instanceof BooleanConstant)
      {
        if(((BooleanConstant)c.condition).value)
          executeStatement(c.then_body);
        else
          executeStatement(c.else_body);
      }
      else if(c.condition instanceof BinaryExpression)
      {
        Expression e = EvaluateExpression.evaluate((BinaryExpression)c.condition);
        if(((BooleanConstant)e).value)
          executeStatement(c.then_body);
        else
          executeStatement(c.else_body);
      }   
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  // evaluate expression - I am not sure on how much we need this
  private static void executeWhileStatement(WhileStatement w) throws Exception
  {
    // the condition would either be a straight-forward constant (in an infinite loop like scenarios) or a binary expression
    try
    {
      if(w.condition instanceof BooleanConstant)
      {
        if(((BooleanConstant)w.condition).value)
        {
          executeStatement(w.body);
          executeStatement(w);
        }
      }
      else if(w.condition instanceof BinaryExpression)
      {
        //Expression e = evaluateBinaryExpression((BinaryExpression)w.condition);
        Expression e = EvaluateExpression.evaluate((BinaryExpression)w.condition);
        if(((BooleanConstant)e).value)
        {
          executeStatement(w.body);
          executeStatement(w);
        }
      }
          return ;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  } 

}
