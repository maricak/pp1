// generated with ast extension for cup
// version 0.8
// 2/1/2019 2:21:53


package rs.ac.bg.etf.pp1.ast;

public class DesignatorSelectorPointClass extends DesignatorSelector {

    private PointSelector PointSelector;

    public DesignatorSelectorPointClass (PointSelector PointSelector) {
        this.PointSelector=PointSelector;
        if(PointSelector!=null) PointSelector.setParent(this);
    }

    public PointSelector getPointSelector() {
        return PointSelector;
    }

    public void setPointSelector(PointSelector PointSelector) {
        this.PointSelector=PointSelector;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(PointSelector!=null) PointSelector.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(PointSelector!=null) PointSelector.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(PointSelector!=null) PointSelector.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorSelectorPointClass(\n");

        if(PointSelector!=null)
            buffer.append(PointSelector.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorSelectorPointClass]");
        return buffer.toString();
    }
}
