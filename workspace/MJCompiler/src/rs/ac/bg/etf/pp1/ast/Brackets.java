// generated with ast extension for cup
// version 0.8
// 4/1/2019 3:12:12


package rs.ac.bg.etf.pp1.ast;

public class Brackets extends OptionalBrackets {

    public Brackets () {
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
        buffer.append("Brackets(\n");

        buffer.append(tab);
        buffer.append(") [Brackets]");
        return buffer.toString();
    }
}
