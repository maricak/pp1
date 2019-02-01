// generated with ast extension for cup
// version 0.8
// 1/1/2019 21:25:13


package rs.ac.bg.etf.pp1.ast;

public class MulopModClass extends Mulop {

    public MulopModClass () {
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
        buffer.append("MulopModClass(\n");

        buffer.append(tab);
        buffer.append(") [MulopModClass]");
        return buffer.toString();
    }
}
