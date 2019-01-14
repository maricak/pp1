// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class OptionalImplementsTypeListClass extends OptionalImplementsTypeList {

    private ImplementsTypeList ImplementsTypeList;

    public OptionalImplementsTypeListClass (ImplementsTypeList ImplementsTypeList) {
        this.ImplementsTypeList=ImplementsTypeList;
        if(ImplementsTypeList!=null) ImplementsTypeList.setParent(this);
    }

    public ImplementsTypeList getImplementsTypeList() {
        return ImplementsTypeList;
    }

    public void setImplementsTypeList(ImplementsTypeList ImplementsTypeList) {
        this.ImplementsTypeList=ImplementsTypeList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ImplementsTypeList!=null) ImplementsTypeList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ImplementsTypeList!=null) ImplementsTypeList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ImplementsTypeList!=null) ImplementsTypeList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptionalImplementsTypeListClass(\n");

        if(ImplementsTypeList!=null)
            buffer.append(ImplementsTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptionalImplementsTypeListClass]");
        return buffer.toString();
    }
}
