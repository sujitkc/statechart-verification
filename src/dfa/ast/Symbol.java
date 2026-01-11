package ast;

public class Symbol {
    private Declaration declaration;
    private Integer tag;

    public Symbol(Declaration d, Integer t)
    {
        this.declaration = d;
        this.tag = t;
    }

    public Declaration getDeclaration()
    {
        return this.declaration;
    }

    public Integer getTag()
    {
        return this.tag;
    }

    public void setDeclaration(Declaration d)
    {
        this.declaration = d;
    }

    public void setTag(Integer t)
    {
        this.tag = t;
    }
}