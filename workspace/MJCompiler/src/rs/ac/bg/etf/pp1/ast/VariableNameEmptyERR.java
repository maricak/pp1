// generated with ast extension for cup
// version 0.8
// 4/1/2019 3:12:11


package rs.ac.bg.etf.pp1.ast;

public class VariableNameEmptyERR extends VarName {

    public VariableNameEmptyERR () {
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
        buffer.append("VariableNameEmptyERR(\n");

        buffer.append(tab);
        buffer.append(") [VariableNameEmptyERR]");
        return buffer.toString();
    }
}
