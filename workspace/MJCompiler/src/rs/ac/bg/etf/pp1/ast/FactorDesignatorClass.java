// generated with ast extension for cup
// version 0.8
// 2/1/2019 19:23:59


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignatorClass extends Factor {

    private DesignatorFactor DesignatorFactor;

    public FactorDesignatorClass (DesignatorFactor DesignatorFactor) {
        this.DesignatorFactor=DesignatorFactor;
        if(DesignatorFactor!=null) DesignatorFactor.setParent(this);
    }

    public DesignatorFactor getDesignatorFactor() {
        return DesignatorFactor;
    }

    public void setDesignatorFactor(DesignatorFactor DesignatorFactor) {
        this.DesignatorFactor=DesignatorFactor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorFactor!=null) DesignatorFactor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorFactor!=null) DesignatorFactor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorFactor!=null) DesignatorFactor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignatorClass(\n");

        if(DesignatorFactor!=null)
            buffer.append(DesignatorFactor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignatorClass]");
        return buffer.toString();
    }
}
