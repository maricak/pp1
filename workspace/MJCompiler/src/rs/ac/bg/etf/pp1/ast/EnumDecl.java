// generated with ast extension for cup
// version 0.8
// 1/1/2019 23:41:19


package rs.ac.bg.etf.pp1.ast;

public class EnumDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private String I2;
    private OptionalAssignNum OptionalAssignNum;
    private IdentOptionalAssignNumList IdentOptionalAssignNumList;

    public EnumDecl (String I1, String I2, OptionalAssignNum OptionalAssignNum, IdentOptionalAssignNumList IdentOptionalAssignNumList) {
        this.I1=I1;
        this.I2=I2;
        this.OptionalAssignNum=OptionalAssignNum;
        if(OptionalAssignNum!=null) OptionalAssignNum.setParent(this);
        this.IdentOptionalAssignNumList=IdentOptionalAssignNumList;
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public OptionalAssignNum getOptionalAssignNum() {
        return OptionalAssignNum;
    }

    public void setOptionalAssignNum(OptionalAssignNum OptionalAssignNum) {
        this.OptionalAssignNum=OptionalAssignNum;
    }

    public IdentOptionalAssignNumList getIdentOptionalAssignNumList() {
        return IdentOptionalAssignNumList;
    }

    public void setIdentOptionalAssignNumList(IdentOptionalAssignNumList IdentOptionalAssignNumList) {
        this.IdentOptionalAssignNumList=IdentOptionalAssignNumList;
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
        if(OptionalAssignNum!=null) OptionalAssignNum.accept(visitor);
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalAssignNum!=null) OptionalAssignNum.traverseTopDown(visitor);
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalAssignNum!=null) OptionalAssignNum.traverseBottomUp(visitor);
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDecl(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(OptionalAssignNum!=null)
            buffer.append(OptionalAssignNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IdentOptionalAssignNumList!=null)
            buffer.append(IdentOptionalAssignNumList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDecl]");
        return buffer.toString();
    }
}
