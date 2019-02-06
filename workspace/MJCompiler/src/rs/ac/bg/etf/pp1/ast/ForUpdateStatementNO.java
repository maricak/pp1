// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:16


package rs.ac.bg.etf.pp1.ast;

public class ForUpdateStatementNO extends ForUpdateStmnt {

    public ForUpdateStatementNO () {
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
        buffer.append("ForUpdateStatementNO(\n");

        buffer.append(tab);
        buffer.append(") [ForUpdateStatementNO]");
        return buffer.toString();
    }
}
