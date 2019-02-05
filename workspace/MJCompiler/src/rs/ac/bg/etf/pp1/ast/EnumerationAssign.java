// generated with ast extension for cup
// version 0.8
// 5/1/2019 14:8:29


package rs.ac.bg.etf.pp1.ast;

public class EnumerationAssign extends EnumAssignList {

    private EnumAssignList EnumAssignList;
    private EnumAssign EnumAssign;

    public EnumerationAssign (EnumAssignList EnumAssignList, EnumAssign EnumAssign) {
        this.EnumAssignList=EnumAssignList;
        if(EnumAssignList!=null) EnumAssignList.setParent(this);
        this.EnumAssign=EnumAssign;
        if(EnumAssign!=null) EnumAssign.setParent(this);
    }

    public EnumAssignList getEnumAssignList() {
        return EnumAssignList;
    }

    public void setEnumAssignList(EnumAssignList EnumAssignList) {
        this.EnumAssignList=EnumAssignList;
    }

    public EnumAssign getEnumAssign() {
        return EnumAssign;
    }

    public void setEnumAssign(EnumAssign EnumAssign) {
        this.EnumAssign=EnumAssign;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumAssignList!=null) EnumAssignList.accept(visitor);
        if(EnumAssign!=null) EnumAssign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumAssignList!=null) EnumAssignList.traverseTopDown(visitor);
        if(EnumAssign!=null) EnumAssign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumAssignList!=null) EnumAssignList.traverseBottomUp(visitor);
        if(EnumAssign!=null) EnumAssign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumerationAssign(\n");

        if(EnumAssignList!=null)
            buffer.append(EnumAssignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumAssign!=null)
            buffer.append(EnumAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumerationAssign]");
        return buffer.toString();
    }
}
