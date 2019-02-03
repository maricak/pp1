// generated with ast extension for cup
// version 0.8
// 3/1/2019 1:43:23


package rs.ac.bg.etf.pp1.ast;

public class InterfaceImplementsList extends InterfaceImplList {

    private InterfaceList InterfaceList;

    public InterfaceImplementsList (InterfaceList InterfaceList) {
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
        buffer.append("InterfaceImplementsList(\n");

        if(InterfaceList!=null)
            buffer.append(InterfaceList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceImplementsList]");
        return buffer.toString();
    }
}
