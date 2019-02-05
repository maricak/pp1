// generated with ast extension for cup
// version 0.8
// 5/1/2019 3:13:15


package rs.ac.bg.etf.pp1.ast;

public class InterfaceMethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private InterfaceMethodStart InterfaceMethodStart;
    private FormPars FormPars;

    public InterfaceMethodDecl (InterfaceMethodStart InterfaceMethodStart, FormPars FormPars) {
        this.InterfaceMethodStart=InterfaceMethodStart;
        if(InterfaceMethodStart!=null) InterfaceMethodStart.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
    }

    public InterfaceMethodStart getInterfaceMethodStart() {
        return InterfaceMethodStart;
    }

    public void setInterfaceMethodStart(InterfaceMethodStart InterfaceMethodStart) {
        this.InterfaceMethodStart=InterfaceMethodStart;
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
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
        if(InterfaceMethodStart!=null) InterfaceMethodStart.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceMethodStart!=null) InterfaceMethodStart.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceMethodStart!=null) InterfaceMethodStart.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceMethodDecl(\n");

        if(InterfaceMethodStart!=null)
            buffer.append(InterfaceMethodStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceMethodDecl]");
        return buffer.toString();
    }
}
