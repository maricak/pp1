// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:40:57


package rs.ac.bg.etf.pp1.ast;

public class DeclarationOptionListClass extends DeclOptionList {

    private DeclOptionList DeclOptionList;
    private DeclOption DeclOption;

    public DeclarationOptionListClass (DeclOptionList DeclOptionList, DeclOption DeclOption) {
        this.DeclOptionList=DeclOptionList;
        if(DeclOptionList!=null) DeclOptionList.setParent(this);
        this.DeclOption=DeclOption;
        if(DeclOption!=null) DeclOption.setParent(this);
    }

    public DeclOptionList getDeclOptionList() {
        return DeclOptionList;
    }

    public void setDeclOptionList(DeclOptionList DeclOptionList) {
        this.DeclOptionList=DeclOptionList;
    }

    public DeclOption getDeclOption() {
        return DeclOption;
    }

    public void setDeclOption(DeclOption DeclOption) {
        this.DeclOption=DeclOption;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclOptionList!=null) DeclOptionList.accept(visitor);
        if(DeclOption!=null) DeclOption.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclOptionList!=null) DeclOptionList.traverseTopDown(visitor);
        if(DeclOption!=null) DeclOption.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclOptionList!=null) DeclOptionList.traverseBottomUp(visitor);
        if(DeclOption!=null) DeclOption.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclarationOptionListClass(\n");

        if(DeclOptionList!=null)
            buffer.append(DeclOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DeclOption!=null)
            buffer.append(DeclOption.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclarationOptionListClass]");
        return buffer.toString();
    }
}
