// generated with ast extension for cup
// version 0.8
// 4/1/2019 0:28:16


package rs.ac.bg.etf.pp1.ast;

public class StatementIfElse extends Statement {

    private IfElseStatement IfElseStatement;

    public StatementIfElse (IfElseStatement IfElseStatement) {
        this.IfElseStatement=IfElseStatement;
        if(IfElseStatement!=null) IfElseStatement.setParent(this);
    }

    public IfElseStatement getIfElseStatement() {
        return IfElseStatement;
    }

    public void setIfElseStatement(IfElseStatement IfElseStatement) {
        this.IfElseStatement=IfElseStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfElseStatement!=null) IfElseStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfElseStatement!=null) IfElseStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfElseStatement!=null) IfElseStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIfElse(\n");

        if(IfElseStatement!=null)
            buffer.append(IfElseStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementIfElse]");
        return buffer.toString();
    }
}
