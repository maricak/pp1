// generated with ast extension for cup
// version 0.8
// 5/1/2019 3:13:15


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodStart MethodStart;
    private FormPars FormPars;
    private MethodFormParsEnd MethodFormParsEnd;
    private VarDeclList VarDeclList;
    private MethodVarsEnd MethodVarsEnd;
    private StatementList StatementList;

    public MethodDecl (MethodStart MethodStart, FormPars FormPars, MethodFormParsEnd MethodFormParsEnd, VarDeclList VarDeclList, MethodVarsEnd MethodVarsEnd, StatementList StatementList) {
        this.MethodStart=MethodStart;
        if(MethodStart!=null) MethodStart.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
        this.MethodFormParsEnd=MethodFormParsEnd;
        if(MethodFormParsEnd!=null) MethodFormParsEnd.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.MethodVarsEnd=MethodVarsEnd;
        if(MethodVarsEnd!=null) MethodVarsEnd.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodStart getMethodStart() {
        return MethodStart;
    }

    public void setMethodStart(MethodStart MethodStart) {
        this.MethodStart=MethodStart;
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
    }

    public MethodFormParsEnd getMethodFormParsEnd() {
        return MethodFormParsEnd;
    }

    public void setMethodFormParsEnd(MethodFormParsEnd MethodFormParsEnd) {
        this.MethodFormParsEnd=MethodFormParsEnd;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public MethodVarsEnd getMethodVarsEnd() {
        return MethodVarsEnd;
    }

    public void setMethodVarsEnd(MethodVarsEnd MethodVarsEnd) {
        this.MethodVarsEnd=MethodVarsEnd;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
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
        if(MethodStart!=null) MethodStart.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
        if(MethodFormParsEnd!=null) MethodFormParsEnd.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(MethodVarsEnd!=null) MethodVarsEnd.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodStart!=null) MethodStart.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
        if(MethodFormParsEnd!=null) MethodFormParsEnd.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(MethodVarsEnd!=null) MethodVarsEnd.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodStart!=null) MethodStart.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        if(MethodFormParsEnd!=null) MethodFormParsEnd.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(MethodVarsEnd!=null) MethodVarsEnd.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodStart!=null)
            buffer.append(MethodStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodFormParsEnd!=null)
            buffer.append(MethodFormParsEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodVarsEnd!=null)
            buffer.append(MethodVarsEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
