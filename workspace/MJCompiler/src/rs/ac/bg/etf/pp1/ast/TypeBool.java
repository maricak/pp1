// generated with ast extension for cup
// version 0.8
// 3/1/2019 15:5:32


package rs.ac.bg.etf.pp1.ast;

public class TypeBool extends Type {

    public TypeBool () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TypeBool(\n");

        buffer.append(tab);
        buffer.append(") [TypeBool]");
        return buffer.toString();
    }
}
