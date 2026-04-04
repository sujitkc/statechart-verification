package ast;

import java.util.List;
import java.util.ArrayList;

public class InputFunctionDeclaration extends FunctionDeclaration {

 public InputFunctionDeclaration(List<String> typeParameterNames, TypeName typename) {
  
    super("input", typeParameterNames, typename, new DeclarationList());
    
  }
}
