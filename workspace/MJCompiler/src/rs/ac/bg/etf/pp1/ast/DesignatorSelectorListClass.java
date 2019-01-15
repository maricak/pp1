// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:40:58


package rs.ac.bg.etf.pp1.ast;

public class DesignatorSelectorListClass extends DesignatorSelectorList {

    private DesignatorSelectorList DesignatorSelectorList;
    private DesignatorSelector DesignatorSelector;

    public DesignatorSelectorListClass (DesignatorSelectorList DesignatorSelectorList, DesignatorSelector DesignatorSelector) {
        this.DesignatorSelectorList=DesignatorSelectorList;
        if(DesignatorSelectorList!=null) DesignatorSelectorList.setParent(this);
        this.DesignatorSelector=DesignatorSelector;
        if(DesignatorSelector!=null) DesignatorSelector.setParent(this);
    }

    public DesignatorSelectorList getDesignatorSelectorList() {
        return DesignatorSelectorList;
    }

    public void setDesignatorSelectorList(DesignatorSelectorList DesignatorSelectorList) {
        this.DesignatorSelectorList=DesignatorSelectorList;
    }

    public DesignatorSelector getDesignatorSelector() {
        return DesignatorSelector;
    }

    public void setDesignatorSelector(DesignatorSelector DesignatorSelector) {
        this.DesignatorSelector=DesignatorSelector;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorSelectorList!=null) DesignatorSelectorList.accept(visitor);
        if(DesignatorSelector!=null) DesignatorSelector.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorSelectorList!=null) DesignatorSelectorList.traverseTopDown(visitor);
        if(DesignatorSelector!=null) DesignatorSelector.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorSelectorList!=null) DesignatorSelectorList.traverseBottomUp(visitor);
        if(DesignatorSelector!=null) DesignatorSelector.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorSelectorListClass(\n");

        if(DesignatorSelectorList!=null)
            buffer.append(DesignatorSelectorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorSelector!=null)
            buffer.append(DesignatorSelector.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorSelectorListClass]");
        return buffer.toString();
    }
}
