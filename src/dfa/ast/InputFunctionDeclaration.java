package ast;

import java.util.List;
import java.util.ArrayList;

public class InputFunctionDeclaration extends FunctionDeclaration {

	public InputFunctionDeclaration(TypeName returnTypeName){
		super("input", TypeName returnTypeName, null);
	}

}
