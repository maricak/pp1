// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:16


package rs.ac.bg.etf.pp1.ast;

public class ForStatement implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ForStart ForStart;
    private ForInitStmnt ForInitStmnt;
    private ForCond ForCond;
    private ForUpdateStmnt ForUpdateStmnt;
    private ForBody ForBody;

    public ForStatement (ForStart ForStart, ForInitStmnt ForInitStmnt, ForCond ForCond, ForUpdateStmnt ForUpdateStmnt, ForBody ForBody) {
        this.ForStart=ForStart;
        if(ForStart!=null) ForStart.setParent(this);
        this.ForInitStmnt=ForInitStmnt;
        if(ForInitStmnt!=null) ForInitStmnt.setParent(this);
        this.ForCond=ForCond;
        if(ForCond!=null) ForCond.setParent(this);
        this.ForUpdateStmnt=ForUpdateStmnt;
        if(ForUpdateStmnt!=null) ForUpdateStmnt.setParent(this);
        this.ForBody=ForBody;
        if(ForBody!=null) ForBody.setParent(this);
    }

    public ForStart getForStart() {
        return ForStart;
    }

    public void setForStart(ForStart ForStart) {
        this.ForStart=ForStart;
    }

    public ForInitStmnt getForInitStmnt() {
        return ForInitStmnt;
    }

    public void setForInitStmnt(ForInitStmnt ForInitStmnt) {
        this.ForInitStmnt=ForInitStmnt;
    }

    public ForCond getForCond() {
        return ForCond;
    }

    public void setForCond(ForCond ForCond) {
        this.ForCond=ForCond;
    }

    public ForUpdateStmnt getForUpdateStmnt() {
        return ForUpdateStmnt;
    }

    public void setForUpdateStmnt(ForUpdateStmnt ForUpdateStmnt) {
        this.ForUpdateStmnt=ForUpdateStmnt;
    }

    public ForBody getForBody() {
        return ForBody;
    }

    public void setForBody(ForBody ForBody) {
        this.ForBody=ForBody;
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
        if(ForStart!=null) ForStart.accept(visitor);
        if(ForInitStmnt!=null) ForInitStmnt.accept(visitor);
        if(ForCond!=null) ForCond.accept(visitor);
        if(ForUpdateStmnt!=null) ForUpdateStmnt.accept(visitor);
        if(ForBody!=null) ForBody.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForStart!=null) ForStart.traverseTopDown(visitor);
        if(ForInitStmnt!=null) ForInitStmnt.traverseTopDown(visitor);
        if(ForCond!=null) ForCond.traverseTopDown(visitor);
        if(ForUpdateStmnt!=null) ForUpdateStmnt.traverseTopDown(visitor);
        if(ForBody!=null) ForBody.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForStart!=null) ForStart.traverseBottomUp(visitor);
        if(ForInitStmnt!=null) ForInitStmnt.traverseBottomUp(visitor);
        if(ForCond!=null) ForCond.traverseBottomUp(visitor);
        if(ForUpdateStmnt!=null) ForUpdateStmnt.traverseBottomUp(visitor);
        if(ForBody!=null) ForBody.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStatement(\n");

        if(ForStart!=null)
            buffer.append(ForStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForInitStmnt!=null)
            buffer.append(ForInitStmnt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForCond!=null)
            buffer.append(ForCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForUpdateStmnt!=null)
            buffer.append(ForUpdateStmnt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForBody!=null)
            buffer.append(ForBody.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStatement]");
        return buffer.toString();
    }
}
