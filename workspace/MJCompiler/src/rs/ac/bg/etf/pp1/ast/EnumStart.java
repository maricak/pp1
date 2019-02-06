// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:16


package rs.ac.bg.etf.pp1.ast;

public class EnumStart implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String enumName;

    public EnumStart (String enumName) {
        this.enumName=enumName;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName=enumName;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
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
        buffer.append("EnumStart(\n");

        buffer.append(" "+tab+enumName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumStart]");
        return buffer.toString();
    }
}
