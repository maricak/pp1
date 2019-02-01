// generated with ast extension for cup
// version 0.8
// 1/1/2019 21:25:12


package rs.ac.bg.etf.pp1.ast;

public class StatementPrintClass extends Statement {

    private Expr Expr;
    private OptionalCommaNumConst OptionalCommaNumConst;

    public StatementPrintClass (Expr Expr, OptionalCommaNumConst OptionalCommaNumConst) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.OptionalCommaNumConst=OptionalCommaNumConst;
        if(OptionalCommaNumConst!=null) OptionalCommaNumConst.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public OptionalCommaNumConst getOptionalCommaNumConst() {
        return OptionalCommaNumConst;
    }

    public void setOptionalCommaNumConst(OptionalCommaNumConst OptionalCommaNumConst) {
        this.OptionalCommaNumConst=OptionalCommaNumConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(OptionalCommaNumConst!=null) OptionalCommaNumConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(OptionalCommaNumConst!=null) OptionalCommaNumConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(OptionalCommaNumConst!=null) OptionalCommaNumConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementPrintClass(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalCommaNumConst!=null)
            buffer.append(OptionalCommaNumConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementPrintClass]");
        return buffer.toString();
    }
}
