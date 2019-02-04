// generated with ast extension for cup
// version 0.8
// 4/1/2019 3:12:12


package rs.ac.bg.etf.pp1.ast;

public class StatementPrint extends Statement {

    private Expr Expr;
    private PrintNumParameter PrintNumParameter;

    public StatementPrint (Expr Expr, PrintNumParameter PrintNumParameter) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PrintNumParameter=PrintNumParameter;
        if(PrintNumParameter!=null) PrintNumParameter.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PrintNumParameter getPrintNumParameter() {
        return PrintNumParameter;
    }

    public void setPrintNumParameter(PrintNumParameter PrintNumParameter) {
        this.PrintNumParameter=PrintNumParameter;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PrintNumParameter!=null) PrintNumParameter.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PrintNumParameter!=null) PrintNumParameter.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PrintNumParameter!=null) PrintNumParameter.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementPrint(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintNumParameter!=null)
            buffer.append(PrintNumParameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementPrint]");
        return buffer.toString();
    }
}
