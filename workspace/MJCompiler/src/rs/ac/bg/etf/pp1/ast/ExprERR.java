// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:17


package rs.ac.bg.etf.pp1.ast;

public class ExprERR extends Expr {

    public ExprERR () {
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
        buffer.append("ExprERR(\n");

        buffer.append(tab);
        buffer.append(") [ExprERR]");
        return buffer.toString();
    }
}
