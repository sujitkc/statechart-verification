package simulator;

import java.util.Map;
import java.util.HashMap;

import ast.*;

public class SymbolicEngine 
{
    private Map<Declaration, Symbol> symbolMap = new HashMap<Declaration, Symbol>();

    public SymbolicEngine(Statechart st)
    {
        int i = 0;
        i = this.populate(st, i);
        
        if(!st.states.isEmpty())
        {
            for(State s : st.states)
            {
                i = this.populate(s, i);
            }
        }
    }

    private int populate(State s, int i)
    {
        for(Declaration d : s.declarations)
        {
            if(d.input)
            {
                this.symbolMap.put(d, new Symbol(d, i));
                System.out.println(d.vname + " --- Sym" + i);
                i ++;
            }
        }
        return i;
    }
    
}