// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class OptionalExtendsTypeClass extends OptionalExtendsType {

    private ExtendsType ExtendsType;

    public OptionalExtendsTypeClass (ExtendsType ExtendsType) {
        this.ExtendsType=ExtendsType;
        if(ExtendsType!=null) ExtendsType.setParent(this);
    }

    public ExtendsType getExtendsType() {
        return ExtendsType;
    }

    public void setExtendsType(ExtendsType ExtendsType) {
        this.ExtendsType=ExtendsType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExtendsType!=null) ExtendsType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExtendsType!=null) ExtendsType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExtendsType!=null) ExtendsType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptionalExtendsTypeClass(\n");

        if(ExtendsType!=null)
            buffer.append(ExtendsType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptionalExtendsTypeClass]");
        return buffer.toString();
    }
}
