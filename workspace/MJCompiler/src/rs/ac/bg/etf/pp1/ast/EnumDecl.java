// generated with ast extension for cup
// version 0.8
// 5/1/2019 19:38:3


package rs.ac.bg.etf.pp1.ast;

public class EnumDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private EnumStart EnumStart;
    private EnumAssignList EnumAssignList;

    public EnumDecl (EnumStart EnumStart, EnumAssignList EnumAssignList) {
        this.EnumStart=EnumStart;
        if(EnumStart!=null) EnumStart.setParent(this);
        this.EnumAssignList=EnumAssignList;
        if(EnumAssignList!=null) EnumAssignList.setParent(this);
    }

    public EnumStart getEnumStart() {
        return EnumStart;
    }

    public void setEnumStart(EnumStart EnumStart) {
        this.EnumStart=EnumStart;
    }

    public EnumAssignList getEnumAssignList() {
        return EnumAssignList;
    }

    public void setEnumAssignList(EnumAssignList EnumAssignList) {
        this.EnumAssignList=EnumAssignList;
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
        if(EnumStart!=null) EnumStart.accept(visitor);
        if(EnumAssignList!=null) EnumAssignList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumStart!=null) EnumStart.traverseTopDown(visitor);
        if(EnumAssignList!=null) EnumAssignList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumStart!=null) EnumStart.traverseBottomUp(visitor);
        if(EnumAssignList!=null) EnumAssignList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDecl(\n");

        if(EnumStart!=null)
            buffer.append(EnumStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumAssignList!=null)
            buffer.append(EnumAssignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDecl]");
        return buffer.toString();
    }
}
