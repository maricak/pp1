// generated with ast extension for cup
// version 0.8
// 5/1/2019 3:13:15


package rs.ac.bg.etf.pp1.ast;

public class ForStatement implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ForStart ForStart;
    private OptionalDesignatorStmnt OptionalDesignatorStmnt;
    private OptionalCond OptionalCond;
    private OptionalDesignatorStmnt OptionalDesignatorStmnt1;
    private Statement Statement;

    public ForStatement (ForStart ForStart, OptionalDesignatorStmnt OptionalDesignatorStmnt, OptionalCond OptionalCond, OptionalDesignatorStmnt OptionalDesignatorStmnt1, Statement Statement) {
        this.ForStart=ForStart;
        if(ForStart!=null) ForStart.setParent(this);
        this.OptionalDesignatorStmnt=OptionalDesignatorStmnt;
        if(OptionalDesignatorStmnt!=null) OptionalDesignatorStmnt.setParent(this);
        this.OptionalCond=OptionalCond;
        if(OptionalCond!=null) OptionalCond.setParent(this);
        this.OptionalDesignatorStmnt1=OptionalDesignatorStmnt1;
        if(OptionalDesignatorStmnt1!=null) OptionalDesignatorStmnt1.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ForStart getForStart() {
        return ForStart;
    }

    public void setForStart(ForStart ForStart) {
        this.ForStart=ForStart;
    }

    public OptionalDesignatorStmnt getOptionalDesignatorStmnt() {
        return OptionalDesignatorStmnt;
    }

    public void setOptionalDesignatorStmnt(OptionalDesignatorStmnt OptionalDesignatorStmnt) {
        this.OptionalDesignatorStmnt=OptionalDesignatorStmnt;
    }

    public OptionalCond getOptionalCond() {
        return OptionalCond;
    }

    public void setOptionalCond(OptionalCond OptionalCond) {
        this.OptionalCond=OptionalCond;
    }

    public OptionalDesignatorStmnt getOptionalDesignatorStmnt1() {
        return OptionalDesignatorStmnt1;
    }

    public void setOptionalDesignatorStmnt1(OptionalDesignatorStmnt OptionalDesignatorStmnt1) {
        this.OptionalDesignatorStmnt1=OptionalDesignatorStmnt1;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
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
        if(OptionalDesignatorStmnt!=null) OptionalDesignatorStmnt.accept(visitor);
        if(OptionalCond!=null) OptionalCond.accept(visitor);
        if(OptionalDesignatorStmnt1!=null) OptionalDesignatorStmnt1.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForStart!=null) ForStart.traverseTopDown(visitor);
        if(OptionalDesignatorStmnt!=null) OptionalDesignatorStmnt.traverseTopDown(visitor);
        if(OptionalCond!=null) OptionalCond.traverseTopDown(visitor);
        if(OptionalDesignatorStmnt1!=null) OptionalDesignatorStmnt1.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForStart!=null) ForStart.traverseBottomUp(visitor);
        if(OptionalDesignatorStmnt!=null) OptionalDesignatorStmnt.traverseBottomUp(visitor);
        if(OptionalCond!=null) OptionalCond.traverseBottomUp(visitor);
        if(OptionalDesignatorStmnt1!=null) OptionalDesignatorStmnt1.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
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

        if(OptionalDesignatorStmnt!=null)
            buffer.append(OptionalDesignatorStmnt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalCond!=null)
            buffer.append(OptionalCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalDesignatorStmnt1!=null)
            buffer.append(OptionalDesignatorStmnt1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStatement]");
        return buffer.toString();
    }
}
