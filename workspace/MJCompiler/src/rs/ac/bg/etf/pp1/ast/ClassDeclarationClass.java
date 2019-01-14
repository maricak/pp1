// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclarationClass extends ClassDecl {

    private OptionalExtendsType OptionalExtendsType;
    private OptionalImplementsTypeList OptionalImplementsTypeList;
    private VarDeclList VarDeclList;
    private OptionalMethodDeclList OptionalMethodDeclList;

    public ClassDeclarationClass (OptionalExtendsType OptionalExtendsType, OptionalImplementsTypeList OptionalImplementsTypeList, VarDeclList VarDeclList, OptionalMethodDeclList OptionalMethodDeclList) {
        this.OptionalExtendsType=OptionalExtendsType;
        if(OptionalExtendsType!=null) OptionalExtendsType.setParent(this);
        this.OptionalImplementsTypeList=OptionalImplementsTypeList;
        if(OptionalImplementsTypeList!=null) OptionalImplementsTypeList.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.OptionalMethodDeclList=OptionalMethodDeclList;
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.setParent(this);
    }

    public OptionalExtendsType getOptionalExtendsType() {
        return OptionalExtendsType;
    }

    public void setOptionalExtendsType(OptionalExtendsType OptionalExtendsType) {
        this.OptionalExtendsType=OptionalExtendsType;
    }

    public OptionalImplementsTypeList getOptionalImplementsTypeList() {
        return OptionalImplementsTypeList;
    }

    public void setOptionalImplementsTypeList(OptionalImplementsTypeList OptionalImplementsTypeList) {
        this.OptionalImplementsTypeList=OptionalImplementsTypeList;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public OptionalMethodDeclList getOptionalMethodDeclList() {
        return OptionalMethodDeclList;
    }

    public void setOptionalMethodDeclList(OptionalMethodDeclList OptionalMethodDeclList) {
        this.OptionalMethodDeclList=OptionalMethodDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalExtendsType!=null) OptionalExtendsType.accept(visitor);
        if(OptionalImplementsTypeList!=null) OptionalImplementsTypeList.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalExtendsType!=null) OptionalExtendsType.traverseTopDown(visitor);
        if(OptionalImplementsTypeList!=null) OptionalImplementsTypeList.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalExtendsType!=null) OptionalExtendsType.traverseBottomUp(visitor);
        if(OptionalImplementsTypeList!=null) OptionalImplementsTypeList.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclarationClass(\n");

        if(OptionalExtendsType!=null)
            buffer.append(OptionalExtendsType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalImplementsTypeList!=null)
            buffer.append(OptionalImplementsTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalMethodDeclList!=null)
            buffer.append(OptionalMethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclarationClass]");
        return buffer.toString();
    }
}
