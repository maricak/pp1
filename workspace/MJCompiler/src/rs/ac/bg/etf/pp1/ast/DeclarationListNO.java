// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:15


package rs.ac.bg.etf.pp1.ast;

public class DeclarationListNO extends DeclList {

    public DeclarationListNO () {
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
        buffer.append("DeclarationListNO(\n");

        buffer.append(tab);
        buffer.append(") [DeclarationListNO]");
        return buffer.toString();
    }
}
