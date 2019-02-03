// generated with ast extension for cup
// version 0.8
// 3/1/2019 15:5:32


package rs.ac.bg.etf.pp1.ast;

public class Interfaces extends InterfaceList {

    private InterfaceList InterfaceList;
    private Type Type;

    public Interfaces (InterfaceList InterfaceList, Type Type) {
        this.InterfaceList=InterfaceList;
        if(InterfaceList!=null) InterfaceList.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
    }

    public InterfaceList getInterfaceList() {
        return InterfaceList;
    }

    public void setInterfaceList(InterfaceList InterfaceList) {
        this.InterfaceList=InterfaceList;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceList!=null) InterfaceList.accept(visitor);
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceList!=null) InterfaceList.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceList!=null) InterfaceList.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Interfaces(\n");

        if(InterfaceList!=null)
            buffer.append(InterfaceList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Interfaces]");
        return buffer.toString();
    }
}
