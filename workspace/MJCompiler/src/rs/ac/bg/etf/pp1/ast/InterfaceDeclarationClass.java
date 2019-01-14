// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDeclarationClass extends InterfaceDecl {

    private InterfaceMethodDeclList InterfaceMethodDeclList;

    public InterfaceDeclarationClass (InterfaceMethodDeclList InterfaceMethodDeclList) {
        this.InterfaceMethodDeclList=InterfaceMethodDeclList;
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.setParent(this);
    }

    public InterfaceMethodDeclList getInterfaceMethodDeclList() {
        return InterfaceMethodDeclList;
    }

    public void setInterfaceMethodDeclList(InterfaceMethodDeclList InterfaceMethodDeclList) {
        this.InterfaceMethodDeclList=InterfaceMethodDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceDeclarationClass(\n");

        if(InterfaceMethodDeclList!=null)
            buffer.append(InterfaceMethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDeclarationClass]");
        return buffer.toString();
    }
}
