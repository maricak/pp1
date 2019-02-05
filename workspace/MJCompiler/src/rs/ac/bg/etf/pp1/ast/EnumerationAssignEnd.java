// generated with ast extension for cup
// version 0.8
// 5/1/2019 14:8:29


package rs.ac.bg.etf.pp1.ast;

public class EnumerationAssignEnd extends EnumAssignList {

    private EnumAssign EnumAssign;

    public EnumerationAssignEnd (EnumAssign EnumAssign) {
        this.EnumAssign=EnumAssign;
        if(EnumAssign!=null) EnumAssign.setParent(this);
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
        if(EnumAssign!=null) EnumAssign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumAssign!=null) EnumAssign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumAssign!=null) EnumAssign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumerationAssignEnd(\n");

        if(EnumAssign!=null)
            buffer.append(EnumAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumerationAssignEnd]");
        return buffer.toString();
    }
}
