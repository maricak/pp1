// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:40:58


package rs.ac.bg.etf.pp1.ast;

public class OptionalArraySelectorClass extends OptionalArraySelector {

    private ArraySelector ArraySelector;

    public OptionalArraySelectorClass (ArraySelector ArraySelector) {
        this.ArraySelector=ArraySelector;
        if(ArraySelector!=null) ArraySelector.setParent(this);
    }

    public ArraySelector getArraySelector() {
        return ArraySelector;
    }

    public void setArraySelector(ArraySelector ArraySelector) {
        this.ArraySelector=ArraySelector;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArraySelector!=null) ArraySelector.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArraySelector!=null) ArraySelector.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArraySelector!=null) ArraySelector.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptionalArraySelectorClass(\n");

        if(ArraySelector!=null)
            buffer.append(ArraySelector.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptionalArraySelectorClass]");
        return buffer.toString();
    }
}
