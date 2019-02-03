// generated with ast extension for cup
// version 0.8
// 3/1/2019 1:43:23


package rs.ac.bg.etf.pp1.ast;

public class IdentifierOptionalAssignNumberListClass extends IdentOptionalAssignNumList {

    private IdentOptionalAssignNumList IdentOptionalAssignNumList;
    private String I2;
    private OptionalAssignNum OptionalAssignNum;

    public IdentifierOptionalAssignNumberListClass (IdentOptionalAssignNumList IdentOptionalAssignNumList, String I2, OptionalAssignNum OptionalAssignNum) {
        this.IdentOptionalAssignNumList=IdentOptionalAssignNumList;
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.setParent(this);
        this.I2=I2;
        this.OptionalAssignNum=OptionalAssignNum;
        if(OptionalAssignNum!=null) OptionalAssignNum.setParent(this);
    }

    public IdentOptionalAssignNumList getIdentOptionalAssignNumList() {
        return IdentOptionalAssignNumList;
    }

    public void setIdentOptionalAssignNumList(IdentOptionalAssignNumList IdentOptionalAssignNumList) {
        this.IdentOptionalAssignNumList=IdentOptionalAssignNumList;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.accept(visitor);
        if(OptionalAssignNum!=null) OptionalAssignNum.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.traverseTopDown(visitor);
        if(OptionalAssignNum!=null) OptionalAssignNum.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IdentOptionalAssignNumList!=null) IdentOptionalAssignNumList.traverseBottomUp(visitor);
        if(OptionalAssignNum!=null) OptionalAssignNum.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IdentifierOptionalAssignNumberListClass(\n");

        if(IdentOptionalAssignNumList!=null)
            buffer.append(IdentOptionalAssignNumList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(OptionalAssignNum!=null)
            buffer.append(OptionalAssignNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IdentifierOptionalAssignNumberListClass]");
        return buffer.toString();
    }
}
