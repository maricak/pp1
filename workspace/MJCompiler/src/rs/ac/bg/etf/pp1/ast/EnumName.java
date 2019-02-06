// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:16


package rs.ac.bg.etf.pp1.ast;

public class EnumName extends EnumAssign {

    private String enumName;

    public EnumName (String enumName) {
        this.enumName=enumName;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName=enumName;
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
        buffer.append("EnumName(\n");

        buffer.append(" "+tab+enumName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumName]");
        return buffer.toString();
    }
}
