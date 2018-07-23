package cfg;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import ast.*;

public class CFGassist{
  private StatementList cfgStatements;

  public CFGassist(){
    cfgStatements=new StatementList();
  }


  public StatementList CFGAST(Statechart sc){
    Name currState=new Name("currState");
    currState.setDeclaration(new Declaration("currState", new TypeName("State"), false));
    ExpressionStatement c1=new ExpressionStatement(currState);
    cfgStatements.add(c1);

    Name inputVal=new Name("inputVal");
    inputVal.setDeclaration(new Declaration("inputVal", new TypeName("String"), true));
    ExpressionStatement c2=new ExpressionStatement(inputVal);

    StatementList ifs=new StatementList();
    ifs.add(c2);
    for(Transition t: sc.transitions){
      BinaryExpression changeCurrent=new BinaryExpression(currState, t.getSourceName(), "=");
      StatementList checkActions=new StatementList(t.action);
      checkActions.add(new ExpressionStatement(changeCurrent));

      IfStatement check3=new IfStatement(t.guard, checkActions);

      BinaryExpression sourceCurrent=new BinaryExpression(t.getSourceName(), currState, "==");
      IfStatement check2=new IfStatement(sourceCurrent, check3);

      BinaryExpression inputTrigger=new BinaryExpression(inputVal, new StringLiteral(t.trigger), "==");
      IfStatement check1=new IfStatement(inputTrigger, check2);

      ifs.add(check1);
    }
    WhileStatement beginCFG=new WhileStatement(new BooleanConstant(true), ifs);
    cfgStatements.add(beginCFG);
    return cfgStatements;
  }
}
