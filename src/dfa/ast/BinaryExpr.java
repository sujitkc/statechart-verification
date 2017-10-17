package ast;

import java.util.*;
import java.lang.*;

public class BinaryExpr extends Expression{
	public Expression n1,n2;
	public String o;
	public BinaryExpr(Expression n1, String o,Expression n2){
		this.n1 = n1;
		this.o = o;
		this.n2 = n2;
		System.out.println("harika");
	}
	public String toString() {
		String s = n1.toString() + o + n2.toString();
    	return s;
  }

}
