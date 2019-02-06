// generated with ast extension for cup
// version 0.8
// 6/1/2019 2:42:17


package rs.ac.bg.etf.pp1.ast;

public class FactorFunctionCall extends Factor {

    private Designator Designator;
    private CallStart CallStart;
    private ActPars ActPars;

    public FactorFunctionCall (Designator Designator, CallStart CallStart, ActPars ActPars) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.CallStart=CallStart;
        if(CallStart!=null) CallStart.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public CallStart getCallStart() {
        return CallStart;
    }

    public void setCallStart(CallStart CallStart) {
        this.CallStart=CallStart;
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(CallStart!=null) CallStart.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(CallStart!=null) CallStart.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(CallStart!=null) CallStart.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorFunctionCall(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CallStart!=null)
            buffer.append(CallStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorFunctionCall]");
        return buffer.toString();
    }
}
