// generated with ast extension for cup
// version 0.8
// 1/1/2019 23:41:19


package rs.ac.bg.etf.pp1.ast;

public class NoTypeIdentBracketsListClass extends TypeIdentBracketsList {

    public NoTypeIdentBracketsListClass () {
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
        buffer.append("NoTypeIdentBracketsListClass(\n");

        buffer.append(tab);
        buffer.append(") [NoTypeIdentBracketsListClass]");
        return buffer.toString();
    }
}
