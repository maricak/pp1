// generated with ast extension for cup
// version 0.8
// 3/1/2019 15:5:31


package rs.ac.bg.etf.pp1.ast;

public class ConstantDeclERR extends ConstDecl {

    public ConstantDeclERR () {
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
        buffer.append("ConstantDeclERR(\n");

        buffer.append(tab);
        buffer.append(") [ConstantDeclERR]");
        return buffer.toString();
    }
}