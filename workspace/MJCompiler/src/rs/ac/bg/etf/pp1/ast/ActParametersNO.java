// generated with ast extension for cup
// version 0.8
// 3/1/2019 1:43:24


package rs.ac.bg.etf.pp1.ast;

public class ActParametersNO extends ActPars {

    public ActParametersNO () {
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
        buffer.append("ActParametersNO(\n");

        buffer.append(tab);
        buffer.append(") [ActParametersNO]");
        return buffer.toString();
    }
}