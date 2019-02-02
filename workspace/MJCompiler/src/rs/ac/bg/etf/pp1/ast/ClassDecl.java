// generated with ast extension for cup
// version 0.8
// 2/1/2019 22:39:37


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassStart ClassStart;
    private OptionalExtendsType OptionalExtendsType;
    private InterfaceImplList InterfaceImplList;
    private ClassVarDeclList ClassVarDeclList;
    private OptionalMethodDeclList OptionalMethodDeclList;

    public ClassDecl (ClassStart ClassStart, OptionalExtendsType OptionalExtendsType, InterfaceImplList InterfaceImplList, ClassVarDeclList ClassVarDeclList, OptionalMethodDeclList OptionalMethodDeclList) {
        this.ClassStart=ClassStart;
        if(ClassStart!=null) ClassStart.setParent(this);
        this.OptionalExtendsType=OptionalExtendsType;
        if(OptionalExtendsType!=null) OptionalExtendsType.setParent(this);
        this.InterfaceImplList=InterfaceImplList;
        if(InterfaceImplList!=null) InterfaceImplList.setParent(this);
        this.ClassVarDeclList=ClassVarDeclList;
        if(ClassVarDeclList!=null) ClassVarDeclList.setParent(this);
        this.OptionalMethodDeclList=OptionalMethodDeclList;
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.setParent(this);
    }

    public ClassStart getClassStart() {
        return ClassStart;
    }

    public void setClassStart(ClassStart ClassStart) {
        this.ClassStart=ClassStart;
    }

    public OptionalExtendsType getOptionalExtendsType() {
        return OptionalExtendsType;
    }

    public void setOptionalExtendsType(OptionalExtendsType OptionalExtendsType) {
        this.OptionalExtendsType=OptionalExtendsType;
    }

    public InterfaceImplList getInterfaceImplList() {
        return InterfaceImplList;
    }

    public void setInterfaceImplList(InterfaceImplList InterfaceImplList) {
        this.InterfaceImplList=InterfaceImplList;
    }

    public ClassVarDeclList getClassVarDeclList() {
        return ClassVarDeclList;
    }

    public void setClassVarDeclList(ClassVarDeclList ClassVarDeclList) {
        this.ClassVarDeclList=ClassVarDeclList;
    }

    public OptionalMethodDeclList getOptionalMethodDeclList() {
        return OptionalMethodDeclList;
    }

    public void setOptionalMethodDeclList(OptionalMethodDeclList OptionalMethodDeclList) {
        this.OptionalMethodDeclList=OptionalMethodDeclList;
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
        if(ClassStart!=null) ClassStart.accept(visitor);
        if(OptionalExtendsType!=null) OptionalExtendsType.accept(visitor);
        if(InterfaceImplList!=null) InterfaceImplList.accept(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.accept(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassStart!=null) ClassStart.traverseTopDown(visitor);
        if(OptionalExtendsType!=null) OptionalExtendsType.traverseTopDown(visitor);
        if(InterfaceImplList!=null) InterfaceImplList.traverseTopDown(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.traverseTopDown(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassStart!=null) ClassStart.traverseBottomUp(visitor);
        if(OptionalExtendsType!=null) OptionalExtendsType.traverseBottomUp(visitor);
        if(InterfaceImplList!=null) InterfaceImplList.traverseBottomUp(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.traverseBottomUp(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        if(ClassStart!=null)
            buffer.append(ClassStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalExtendsType!=null)
            buffer.append(OptionalExtendsType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(InterfaceImplList!=null)
            buffer.append(InterfaceImplList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassVarDeclList!=null)
            buffer.append(ClassVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalMethodDeclList!=null)
            buffer.append(OptionalMethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}
