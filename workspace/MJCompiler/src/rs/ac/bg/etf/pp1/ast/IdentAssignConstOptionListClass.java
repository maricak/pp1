// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class IdentAssignConstOptionListClass extends IdentifierAssignConstOptionList {

    private IdentifierAssignConstOptionList IdentifierAssignConstOptionList;
    private ConstOption ConstOption;

    public IdentAssignConstOptionListClass (IdentifierAssignConstOptionList IdentifierAssignConstOptionList, ConstOption ConstOption) {
        this.IdentifierAssignConstOptionList=IdentifierAssignConstOptionList;
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.setParent(this);
        this.ConstOption=ConstOption;
        if(ConstOption!=null) ConstOption.setParent(this);
    }

    public IdentifierAssignConstOptionList getIdentifierAssignConstOptionList() {
        return IdentifierAssignConstOptionList;
    }

    public void setIdentifierAssignConstOptionList(IdentifierAssignConstOptionList IdentifierAssignConstOptionList) {
        this.IdentifierAssignConstOptionList=IdentifierAssignConstOptionList;
    }

    public ConstOption getConstOption() {
        return ConstOption;
    }

    public void setConstOption(ConstOption ConstOption) {
        this.ConstOption=ConstOption;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.accept(visitor);
        if(ConstOption!=null) ConstOption.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.traverseTopDown(visitor);
        if(ConstOption!=null) ConstOption.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.traverseBottomUp(visitor);
        if(ConstOption!=null) ConstOption.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IdentAssignConstOptionListClass(\n");

        if(IdentifierAssignConstOptionList!=null)
            buffer.append(IdentifierAssignConstOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstOption!=null)
            buffer.append(ConstOption.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IdentAssignConstOptionListClass]");
        return buffer.toString();
    }
}
