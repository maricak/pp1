// generated with ast extension for cup
// version 0.8
// 1/1/2019 23:41:20


package rs.ac.bg.etf.pp1.ast;

public class RelopLtClass extends Relop {

    public RelopLtClass () {
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
        buffer.append("RelopLtClass(\n");

        buffer.append(tab);
        buffer.append(") [RelopLtClass]");
        return buffer.toString();
    }
}
