// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:16


package rs.ac.bg.etf.pp1.ast;

public class ImplementsList extends Implements {

    private InterfaceList InterfaceList;

    public ImplementsList (InterfaceList InterfaceList) {
        this.InterfaceList=InterfaceList;
        if(InterfaceList!=null) InterfaceList.setParent(this);
    }

    public InterfaceList getInterfaceList() {
        return InterfaceList;
    }

    public void setInterfaceList(InterfaceList InterfaceList) {
        this.InterfaceList=InterfaceList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceList!=null) InterfaceList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceList!=null) InterfaceList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceList!=null) InterfaceList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ImplementsList(\n");

        if(InterfaceList!=null)
            buffer.append(InterfaceList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ImplementsList]");
        return buffer.toString();
    }
}
