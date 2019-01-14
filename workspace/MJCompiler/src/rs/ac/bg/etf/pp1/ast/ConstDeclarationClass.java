// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclarationClass extends ConstDecl {

    private Type Type;
    private ConstOption ConstOption;
    private IdentifierAssignConstOptionList IdentifierAssignConstOptionList;

    public ConstDeclarationClass (Type Type, ConstOption ConstOption, IdentifierAssignConstOptionList IdentifierAssignConstOptionList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstOption=ConstOption;
        if(ConstOption!=null) ConstOption.setParent(this);
        this.IdentifierAssignConstOptionList=IdentifierAssignConstOptionList;
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstOption getConstOption() {
        return ConstOption;
    }

    public void setConstOption(ConstOption ConstOption) {
        this.ConstOption=ConstOption;
    }

    public IdentifierAssignConstOptionList getIdentifierAssignConstOptionList() {
        return IdentifierAssignConstOptionList;
    }

    public void setIdentifierAssignConstOptionList(IdentifierAssignConstOptionList IdentifierAssignConstOptionList) {
        this.IdentifierAssignConstOptionList=IdentifierAssignConstOptionList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ConstOption!=null) ConstOption.accept(visitor);
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstOption!=null) ConstOption.traverseTopDown(visitor);
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstOption!=null) ConstOption.traverseBottomUp(visitor);
        if(IdentifierAssignConstOptionList!=null) IdentifierAssignConstOptionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclarationClass(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstOption!=null)
            buffer.append(ConstOption.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IdentifierAssignConstOptionList!=null)
            buffer.append(IdentifierAssignConstOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclarationClass]");
        return buffer.toString();
    }
}
