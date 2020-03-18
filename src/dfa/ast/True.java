package ast;

import program.IProgram;

public class True extends BooleanConstant{

	/*public True(IProgram program) throws Exception {
		super(program, (new BooleanConstant(true)).getType());
	}Figure out why this is needed*/
     public True(){
		super(true);
	}
	
	public String toString() {
		return "true";
	}

	
}
