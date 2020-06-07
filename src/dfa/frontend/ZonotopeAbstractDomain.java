package frontend;

import ast.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ZonotopeAbstractDomain 
{
    // takes an atomic state as an input, reads all the variables and the actions defined in it
    // execution takes place as follow: entry action --> exit actions : and the interval values are given out at the time of exit
    
    // all the instructions to be executed are stored here
    private List<Statement> listOfInstructions = new ArrayList<Statement>();
    
    // all the required variables are initialized here
    private Map<Declaration, Expression> initVariables = new HashMap<Declaration, Expression>();

    // maps each variable to its abstract value
    private Map<Declaration, List<Float>> abstractDomain = new HashMap<Declaration, List<Float>>();
    private int n = 1; // number of noise terms + 1

    ZonotopeAbstractDomain(State s, Map<Declaration, Expression> map)
    {
        for(Declaration d : s.getEnvironment().getAllDeclarations())
        {
            if(map.get(d) != null)
            {
                // as right now the only numerical type is int
                if(("int").equals(d.typeName.toString()))
                {
                    ArrayList<Float> centralValue = new ArrayList<Float>();
                    initVariables.put(d, map.get(d));
                    if(map.get(d) instanceof IntegerConstant)
                    {
                        centralValue.add(((Integer)(((IntegerConstant)(map.get(d))).value)).floatValue());
                        abstractDomain.put(d, centralValue);
                    }
                }
            }     
        }
        // there can be assignments, conditionals, loops and expressions
        listOfInstructions.add(s.entry);
        listOfInstructions.add(s.exit);
        simulate();
    }

    private void simulate()
    {
        for(Statement s : listOfInstructions)
        {
            executeInstruction(s);
        }
        for(Declaration decl : abstractDomain.keySet())
        {
            System.out.println("Variabel Name: " + decl.getFullVName());
            System.out.println("Variable Interval: [" + get_interval(abstractDomain.get(decl)).get(0) + " , " + get_interval(abstractDomain.get(decl)).get(1) + "]");
        }   
    }

    // takes the zonotope domain value and returns the interval
    private List<Float> get_interval(List<Float> l) 
    {
        float centralValue = l.get(0);
        float lhs = centralValue;
        float rhs = centralValue;
        int tag = 0;
        for(Float f : l)
        {
            if(tag == 0)
            {
                tag = 1;
                continue;
            }
            lhs = lhs - Math.abs(f);
            rhs = rhs + Math.abs(f);
        }
        List<Float> output = new ArrayList<Float>();
        output.add(lhs);
        output.add(rhs);
        return output;
    }

    // executing the instructions
    private void executeInstruction(Statement s)
    {
        if(s instanceof StatementList)
        {
            List<Statement> st_list = ((StatementList)s).getStatements();
            for(Statement st : st_list)
            {
                executeInstruction(st);
            }
        }
        else // here we have an atomic statement
        {
            if(s instanceof AssignmentStatement)
                this.executeAssignmentInstruction((AssignmentStatement)s);
            else if(s instanceof IfStatement)
                this.executeConditionalInstruction((IfStatement)s);
        }
    }

    private void executeConditionalInstruction(IfStatement s)
    {
        Map<Declaration, List<Float>> abstractDomainCopy = new HashMap<>(abstractDomain);
        executeInstruction(s.then_body);
        Map<Declaration, List<Float>> mapIfBlock = new HashMap<>(abstractDomain);
        abstractDomain = new HashMap<>(abstractDomainCopy);
        executeInstruction(s.else_body);
        Map<Declaration, List<Float>> mapElseBlock = new HashMap<>(abstractDomain);
        abstractDomain = new HashMap<>(abstractDomainCopy);
        for(Declaration decl : mapIfBlock.keySet())
        {
            if(mapIfBlock.get(decl).equals(mapElseBlock.get(decl)))
                continue;
            else
            {
                float rhs = Float.max(get_interval(mapIfBlock.get(decl)).get(1), get_interval(mapElseBlock.get(decl)).get(1));
                float lhs = Float.min(get_interval(mapIfBlock.get(decl)).get(0), get_interval(mapElseBlock.get(decl)).get(0));
                n = n + 1;
                float centralValue = (rhs + lhs)/2;
                float noise        = rhs - centralValue;
                List<Float> out = new ArrayList<Float>();
                for(int i = 0; i < n; i++)
                    out.add(((Integer)0).floatValue());
                out.set(0, centralValue);
                out.set(out.size()-1, noise);
                abstractDomain.put(decl, out);
                
            }
        }
    }

    private void executeAssignmentInstruction(AssignmentStatement s)
    {
        abstractDomain.put((s.lhs).getDeclaration(), evaluateExpression(s.rhs));
    }

    private List<Float> getAbstractValue(Expression e)
    {
        List<Float> val = new ArrayList<Float>();
        if(e instanceof IntegerConstant)
        {
            val.add((((Integer)((IntegerConstant)e).value).floatValue()));
            for(int i = 1; i < n; i++)
                val.add((((Integer)(0)).floatValue()));
        }
        else if(e instanceof Name)
        {
            val = abstractDomain.get(((Name)e).getDeclaration());
        }
        return val;
    }

    private List<Float> evaluateBinaryExpression(BinaryExpression e)
    {
        List<Float> lhs = null;
        List<Float> rhs = null;
        List<Float> val = new ArrayList<Float>();
        if(e.left instanceof IntegerConstant || e.left instanceof Name)
          lhs = getAbstractValue(e.left);
        else
          lhs = evaluateBinaryExpression((BinaryExpression)e.left);
        if(e.right instanceof IntegerConstant || e.right instanceof Name)
          rhs = getAbstractValue(e.right);
        else
          rhs = evaluateBinaryExpression((BinaryExpression)e.right);

        // operations
        if(e.operator.equals("+"))
        {
            int i = 0;
            while(i < lhs.size() && i < rhs.size())
            {
                val.add(lhs.get(i) + rhs.get(i));
                i++;
            }
        }
        else if(e.operator.equals("-"))
        {
            int i = 0;
            while(i < lhs.size() && i < rhs.size())
            {
                val.add(lhs.get(i) - rhs.get(i));
                i ++;
            }
        }
        else if(e.operator.equals("*"))
        {
            val.add(lhs.get(0) + rhs.get(0));
            float aux = 0;
            for(int i = 1; i < Integer.min(lhs.size(), rhs.size()); i++)
            {
                aux = aux + Math.abs(lhs.get(i) * rhs.get(i));
            }
            val.set(0, val.get(0) + aux/2);
            
            int i = 1;
            while(i < lhs.size() && i < rhs.size())
            {
                val.set(i, lhs.get(0)*rhs.get(i) + rhs.get(0)*lhs.get(i));
                i++;
            }
            float aux2 = 0;
            for(i = 1; i < Integer.max(lhs.size(), rhs.size()); i++)
            {
                for(int j = i + 1; j < Integer.max(lhs.size(), rhs.size()); j++)
                {
                    aux2 = aux2 + Math.abs(lhs.get(i)* rhs.get(j) + lhs.get(j)*rhs.get(i));
                }
            }
            val.add(aux + aux2);     
        }
        // ignoring division for now
        return val;
    }

    private List<Float> evaluateExpression(Expression e)
    {
        List<Float> val = new ArrayList<Float>();
        if(e instanceof IntegerConstant)
        {   
            val.add(((Integer)((IntegerConstant)e).value).floatValue());
        }
        else if(e instanceof Name)
        {
            val = (ArrayList<Float>)abstractDomain.get(((Name)e).getDeclaration());
        }
        else // assuming the third type has to be a binary expression
        {
            val = evaluateBinaryExpression((BinaryExpression) e);
        }
        return val;
    }


    
}