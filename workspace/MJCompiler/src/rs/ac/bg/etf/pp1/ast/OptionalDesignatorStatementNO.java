// generated with ast extension for cup
// version 0.8
// 4/1/2019 0:28:16


package rs.ac.bg.etf.pp1.ast;

public class OptionalDesignatorStatementNO extends OptionalDesignatorStmnt {

    public OptionalDesignatorStatementNO () {
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
        buffer.append("OptionalDesignatorStatementNO(\n");

        buffer.append(tab);
        buffer.append(") [OptionalDesignatorStatementNO]");
        return buffer.toString();
    }
}