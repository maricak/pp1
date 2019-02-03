// generated with ast extension for cup
// version 0.8
// 4/1/2019 0:28:16


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassStart ClassStart;
    private Extends Extends;
    private Implements Implements;
    private ClassVarDeclList ClassVarDeclList;
    private OptionalMethodDeclList OptionalMethodDeclList;

    public ClassDecl (ClassStart ClassStart, Extends Extends, Implements Implements, ClassVarDeclList ClassVarDeclList, OptionalMethodDeclList OptionalMethodDeclList) {
        this.ClassStart=ClassStart;
        if(ClassStart!=null) ClassStart.setParent(this);
        this.Extends=Extends;
        if(Extends!=null) Extends.setParent(this);
        this.Implements=Implements;
        if(Implements!=null) Implements.setParent(this);
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

    public Extends getExtends() {
        return Extends;
    }

    public void setExtends(Extends Extends) {
        this.Extends=Extends;
    }

    public Implements getImplements() {
        return Implements;
    }

    public void setImplements(Implements Implements) {
        this.Implements=Implements;
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
        if(Extends!=null) Extends.accept(visitor);
        if(Implements!=null) Implements.accept(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.accept(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassStart!=null) ClassStart.traverseTopDown(visitor);
        if(Extends!=null) Extends.traverseTopDown(visitor);
        if(Implements!=null) Implements.traverseTopDown(visitor);
        if(ClassVarDeclList!=null) ClassVarDeclList.traverseTopDown(visitor);
        if(OptionalMethodDeclList!=null) OptionalMethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassStart!=null) ClassStart.traverseBottomUp(visitor);
        if(Extends!=null) Extends.traverseBottomUp(visitor);
        if(Implements!=null) Implements.traverseBottomUp(visitor);
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

        if(Extends!=null)
            buffer.append(Extends.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Implements!=null)
            buffer.append(Implements.toString("  "+tab));
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
