// generated with ast extension for cup
// version 0.8
// 2/1/2019 19:23:58


package rs.ac.bg.etf.pp1.ast;

public class NoOptionalMethodDeclarationListClass extends OptionalMethodDeclList {

    public NoOptionalMethodDeclarationListClass () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoOptionalMethodDeclarationListClass(\n");

        buffer.append(tab);
        buffer.append(") [NoOptionalMethodDeclarationListClass]");
        return buffer.toString();
    }
}
