// generated with ast extension for cup
// version 0.8
// 3/1/2019 18:47:37


package rs.ac.bg.etf.pp1.ast;

public class DeclarationInterface extends Decl {

    private InterfaceDecl InterfaceDecl;

    public DeclarationInterface (InterfaceDecl InterfaceDecl) {
        this.InterfaceDecl=InterfaceDecl;
        if(InterfaceDecl!=null) InterfaceDecl.setParent(this);
    }

    public InterfaceDecl getInterfaceDecl() {
        return InterfaceDecl;
    }

    public void setInterfaceDecl(InterfaceDecl InterfaceDecl) {
        this.InterfaceDecl=InterfaceDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceDecl!=null) InterfaceDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceDecl!=null) InterfaceDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceDecl!=null) InterfaceDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclarationInterface(\n");

        if(InterfaceDecl!=null)
            buffer.append(InterfaceDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclarationInterface]");
        return buffer.toString();
    }
}
