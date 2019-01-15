// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:40:57


package rs.ac.bg.etf.pp1.ast;

public class ProgramClass extends Program {

    private DeclOptionList DeclOptionList;
    private MethodDeclList MethodDeclList;

    public ProgramClass (DeclOptionList DeclOptionList, MethodDeclList MethodDeclList) {
        this.DeclOptionList=DeclOptionList;
        if(DeclOptionList!=null) DeclOptionList.setParent(this);
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
    }

    public DeclOptionList getDeclOptionList() {
        return DeclOptionList;
    }

    public void setDeclOptionList(DeclOptionList DeclOptionList) {
        this.DeclOptionList=DeclOptionList;
    }

    public MethodDeclList getMethodDeclList() {
        return MethodDeclList;
    }

    public void setMethodDeclList(MethodDeclList MethodDeclList) {
        this.MethodDeclList=MethodDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclOptionList!=null) DeclOptionList.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclOptionList!=null) DeclOptionList.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclOptionList!=null) DeclOptionList.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramClass(\n");

        if(DeclOptionList!=null)
            buffer.append(DeclOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramClass]");
        return buffer.toString();
    }
}
