// generated with ast extension for cup
// version 0.8
// 2/1/2019 2:21:53


package rs.ac.bg.etf.pp1.ast;

public class ConditionFactClass extends CondFact {

    private Expr Expr;
    private OptionalRelopExpr OptionalRelopExpr;

    public ConditionFactClass (Expr Expr, OptionalRelopExpr OptionalRelopExpr) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.OptionalRelopExpr=OptionalRelopExpr;
        if(OptionalRelopExpr!=null) OptionalRelopExpr.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public OptionalRelopExpr getOptionalRelopExpr() {
        return OptionalRelopExpr;
    }

    public void setOptionalRelopExpr(OptionalRelopExpr OptionalRelopExpr) {
        this.OptionalRelopExpr=OptionalRelopExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(OptionalRelopExpr!=null) OptionalRelopExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(OptionalRelopExpr!=null) OptionalRelopExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(OptionalRelopExpr!=null) OptionalRelopExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionFactClass(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalRelopExpr!=null)
            buffer.append(OptionalRelopExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionFactClass]");
        return buffer.toString();
    }
}
