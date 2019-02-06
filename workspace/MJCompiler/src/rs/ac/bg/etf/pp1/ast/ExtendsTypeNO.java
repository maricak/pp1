// generated with ast extension for cup
// version 0.8
// 6/1/2019 19:19:39


package rs.ac.bg.etf.pp1.ast;

public class ExtendsTypeNO extends Extends {

    public ExtendsTypeNO () {
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
        buffer.append("ExtendsTypeNO(\n");

        buffer.append(tab);
        buffer.append(") [ExtendsTypeNO]");
        return buffer.toString();
    }
}
