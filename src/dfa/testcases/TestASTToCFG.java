package testcases;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;

import ast.*;
import simulator2.ASTToCFG;
import simulator2.cfg.*;

public class TestASTToCFG {

  @Test
  public void testAssignment_1() {
    System.out.println("Running testAssignment_1 ...");
    List<String> strings = new ArrayList<>();
    strings.add("x");
    Name name = new Name(strings);
    IntegerConstant i = new IntegerConstant(10);
    AssignmentStatement s = new AssignmentStatement(name, i);
    ASTToCFG converter = new ASTToCFG();
    try {
      CFG cfg = converter.convert(s);
      System.out.println(cfg);
    }
    catch(Exception e) {}
  }
 
  @Test
  public void testIf_1() {
    System.out.println("Running testIf_1 ...");
    List<String> strings = new ArrayList<>();
    strings.add("x");
    Name name = new Name(strings);
    IntegerConstant i0 = new IntegerConstant(0);
    IntegerConstant i1 = new IntegerConstant(10);
    IntegerConstant i2 = new IntegerConstant(20);
    Expression c = new BooleanConstant(true);
    AssignmentStatement s1 = new AssignmentStatement(name, i1);
    AssignmentStatement s2 = new AssignmentStatement(name, i2);
    Statement s = new IfStatement(c, s1, s2);
    ASTToCFG converter = new ASTToCFG();
    try {
      CFG cfg = converter.convert(s);
      System.out.println(cfg);
    }
    catch(Exception e) {}
  }
  
  @Test
  public void testWhile_1() {
    System.out.println("Running testWhile_1 ...");
    List<String> strings = new ArrayList<>();
    strings.add("x");
    Name name = new Name(strings);
    IntegerConstant i0 = new IntegerConstant(0);
    IntegerConstant i1 = new IntegerConstant(10);
    IntegerConstant i2 = new IntegerConstant(20);
    Expression c = new BooleanConstant(true);
    AssignmentStatement s1 = new AssignmentStatement(name, i1);
    AssignmentStatement s2 = new AssignmentStatement(name, i2);
    Statement s = new WhileStatement(c, s1);
    ASTToCFG converter = new ASTToCFG();
    try {
      CFG cfg = converter.convert(s);
      System.out.println(cfg);
    }
    catch(Exception e) {}
  }

}
