package searchsim.property;

import ast.*;
import java.util.Map;

// property to be checked on machine states
public class Property {
    private final String name;
    private final Expression expression;
    
    public Property(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    // evaluate the property on a given environment
    public boolean evaluate(Map<Declaration, Expression> environment) {
        try {
            Expression result = searchsim.simulator.ActionLanguageInterpreter.evaluate(
                this.expression, environment);
            
            if (result instanceof BooleanConstant) {
                return ((BooleanConstant) result).equals(BooleanConstant.True);
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
    
    @Override
    public String toString() {
        return this.name + ": " + this.expression.toString();
    }
}
