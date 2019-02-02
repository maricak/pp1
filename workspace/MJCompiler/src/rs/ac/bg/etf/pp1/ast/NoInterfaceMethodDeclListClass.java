// generated with ast extension for cup
// version 0.8
// 2/1/2019 22:39:37


package rs.ac.bg.etf.pp1.ast;

public class NoInterfaceMethodDeclListClass extends InterfaceMethodDeclList {

    public NoInterfaceMethodDeclListClass () {
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
        buffer.append("NoInterfaceMethodDeclListClass(\n");

        buffer.append(tab);
        buffer.append(") [NoInterfaceMethodDeclListClass]");
        return buffer.toString();
    }
}
