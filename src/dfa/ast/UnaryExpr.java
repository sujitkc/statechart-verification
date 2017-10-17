package ast;

import java.util.*;
import java.lang.*;

public class UnaryExpr extends Expression{
	public Expression n;
	public Name n1=null;
	public String o;
	public UnaryExpr(Expression n, String o){
		this.n = n;
		this.o = o;
	}
	public UnaryExpr(String o, Expression n){
		this.n = n;
		this.o = o;
	}

	public UnaryExpr(Expression n, Name n1){
		this.n = n;
		this.n1 = n1;
	}

	public String toString() {
		if(n1!=null && o!=null){
			String s = n.toString() + o + n1.toString();
    		return s;	
		}
		else if(n1==null){
			String s = n.toString() + o;
			return s;
		}
		else if(o==null){
			String s = n.toString() + n1.toString();
    		return s;
		}
		return "";
	}
}
