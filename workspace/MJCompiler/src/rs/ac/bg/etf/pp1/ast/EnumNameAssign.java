// generated with ast extension for cup
// version 0.8
// 4/1/2019 15:41:58


package rs.ac.bg.etf.pp1.ast;

public class EnumNameAssign extends EnumAssign {

    private String enumName;
    private Integer value;

    public EnumNameAssign (String enumName, Integer value) {
        this.enumName=enumName;
        this.value=value;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName=enumName;
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
        buffer.append("EnumNameAssign(\n");

        buffer.append(" "+tab+enumName);
        buffer.append("\n");

        buffer.append(" "+tab+value);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumNameAssign]");
        return buffer.toString();
    }
}
