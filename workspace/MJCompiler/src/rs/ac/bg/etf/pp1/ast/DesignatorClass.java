// generated with ast extension for cup
// version 0.8
// 2/1/2019 19:23:59


package rs.ac.bg.etf.pp1.ast;

public class DesignatorClass extends Designator {

    private String I1;
    private DesignatorSelectorList DesignatorSelectorList;

    public DesignatorClass (String I1, DesignatorSelectorList DesignatorSelectorList) {
        this.I1=I1;
        this.DesignatorSelectorList=DesignatorSelectorList;
        if(DesignatorSelectorList!=null) DesignatorSelectorList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public DesignatorSelectorList getDesignatorSelectorList() {
        return DesignatorSelectorList;
    }

    public void setDesignatorSelectorList(DesignatorSelectorList DesignatorSelectorList) {
        this.DesignatorSelectorList=DesignatorSelectorList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorSelectorList!=null) DesignatorSelectorList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorSelectorList!=null) DesignatorSelectorList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorSelectorList!=null) DesignatorSelectorList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorClass(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(DesignatorSelectorList!=null)
            buffer.append(DesignatorSelectorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorClass]");
        return buffer.toString();
    }
}
