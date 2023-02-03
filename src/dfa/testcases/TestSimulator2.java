package testcases;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;

import simulator2.*;
import simulator2.cfg.*;
import ast.*;

public class TestSimulator2 {

  @Test
  public void testSimulator1() {
    System.out.println("Hello Simulator2.");
  }

  @Test
  public void testSimulator2() {
    System.out.println("Hello again Simulator2.");
		List<String> strings = new ArrayList<>();
		strings.add("x");
    Name name = new Name(strings);
		IntegerConstant i = new IntegerConstant(10);
    AssignmentStatement s = new AssignmentStatement(name, i);
		ASTToCFG converter = new ASTToCFG(s);
		try {
		  CFG cfg = converter.convert();
			System.out.println(cfg);
		}
		catch(Exception e) {}
  }
}
