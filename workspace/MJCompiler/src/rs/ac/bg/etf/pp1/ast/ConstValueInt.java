// generated with ast extension for cup
// version 0.8
// 6/1/2019 19:19:39


package rs.ac.bg.etf.pp1.ast;

public class ConstValueInt extends ConstValue {

    private Integer value;

    public ConstValueInt (Integer value) {
        this.value=value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value=value;
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
        buffer.append("ConstValueInt(\n");

        buffer.append(" "+tab+value);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstValueInt]");
        return buffer.toString();
    }
}
