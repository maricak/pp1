// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class NoOptionalAssgnNumberClass extends OptionalAssignNum {

    public NoOptionalAssgnNumberClass () {
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
        buffer.append("NoOptionalAssgnNumberClass(\n");

        buffer.append(tab);
        buffer.append(") [NoOptionalAssgnNumberClass]");
        return buffer.toString();
    }
}
