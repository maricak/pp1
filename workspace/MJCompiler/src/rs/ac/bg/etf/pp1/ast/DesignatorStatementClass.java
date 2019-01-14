// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementClass extends DesignatorStatement {

    private Designator Designator;
    private DesignatorStatementOption DesignatorStatementOption;

    public DesignatorStatementClass (Designator Designator, DesignatorStatementOption DesignatorStatementOption) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorStatementOption=DesignatorStatementOption;
        if(DesignatorStatementOption!=null) DesignatorStatementOption.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorStatementOption getDesignatorStatementOption() {
        return DesignatorStatementOption;
    }

    public void setDesignatorStatementOption(DesignatorStatementOption DesignatorStatementOption) {
        this.DesignatorStatementOption=DesignatorStatementOption;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorStatementOption!=null) DesignatorStatementOption.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorStatementOption!=null) DesignatorStatementOption.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorStatementOption!=null) DesignatorStatementOption.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementClass(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementOption!=null)
            buffer.append(DesignatorStatementOption.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementClass]");
        return buffer.toString();
    }
}
