// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclarationClass extends EnumDecl {

    private OptionalAssignNum OptionalAssignNum;
    private IdentOptionalAssignNumList IdentOptionalAssignNumList;

    public EnumDeclarationClass (OptionalAssignNum OptionalAssignNum, IdentOptionalAssignNumList IdentOptionalAssignNumList) {
        this.OptionalAssignNum=OptionalAssignNum;
        if(OptionalAssignNum!=null) OptionalAssignNum.setParent(this);
        this.IdentOptionalAssignNumList=IdentOptionalAssignNumList;
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.setParent(this);
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
        buffer.append("EnumDeclarationClass(\n");

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
        buffer.append(") [EnumDeclarationClass]");
        return buffer.toString();
    }
}
