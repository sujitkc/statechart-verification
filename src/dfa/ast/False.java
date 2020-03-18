package ast;

import program.IProgram;

public class False extends BooleanConstant{

	/*public False(IProgram program) throws Exception {
		super(program,(new BooleanConstant(false)).getType());
	}Figure out why this is needed*/
    public False(){
		super(false);
	}
	
	public String toString() {
		return "false";
	}

	
	
}
