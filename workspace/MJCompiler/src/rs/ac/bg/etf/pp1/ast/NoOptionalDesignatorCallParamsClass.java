// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:38


package rs.ac.bg.etf.pp1.ast;

public class NoOptionalDesignatorCallParamsClass extends OptionalDesignatorCallParams {

    public NoOptionalDesignatorCallParamsClass () {
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
        buffer.append("NoOptionalDesignatorCallParamsClass(\n");

        buffer.append(tab);
        buffer.append(") [NoOptionalDesignatorCallParamsClass]");
        return buffer.toString();
    }
}
