// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class DesignatorFactorClass extends DesignatorFactor {

    private Designator Designator;
    private OptionalDesignatorCallParams OptionalDesignatorCallParams;

    public DesignatorFactorClass (Designator Designator, OptionalDesignatorCallParams OptionalDesignatorCallParams) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.OptionalDesignatorCallParams=OptionalDesignatorCallParams;
        if(OptionalDesignatorCallParams!=null) OptionalDesignatorCallParams.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public OptionalDesignatorCallParams getOptionalDesignatorCallParams() {
        return OptionalDesignatorCallParams;
    }

    public void setOptionalDesignatorCallParams(OptionalDesignatorCallParams OptionalDesignatorCallParams) {
        this.OptionalDesignatorCallParams=OptionalDesignatorCallParams;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(OptionalDesignatorCallParams!=null) OptionalDesignatorCallParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(OptionalDesignatorCallParams!=null) OptionalDesignatorCallParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(OptionalDesignatorCallParams!=null) OptionalDesignatorCallParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorFactorClass(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalDesignatorCallParams!=null)
            buffer.append(OptionalDesignatorCallParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFactorClass]");
        return buffer.toString();
    }
}
