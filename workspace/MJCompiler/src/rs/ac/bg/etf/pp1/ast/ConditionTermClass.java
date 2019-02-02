// generated with ast extension for cup
// version 0.8
// 2/1/2019 19:23:59


package rs.ac.bg.etf.pp1.ast;

public class ConditionTermClass extends CondTerm {

    private CondFact CondFact;
    private AndCondFactList AndCondFactList;

    public ConditionTermClass (CondFact CondFact, AndCondFactList AndCondFactList) {
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
        this.AndCondFactList=AndCondFactList;
        if(AndCondFactList!=null) AndCondFactList.setParent(this);
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public AndCondFactList getAndCondFactList() {
        return AndCondFactList;
    }

    public void setAndCondFactList(AndCondFactList AndCondFactList) {
        this.AndCondFactList=AndCondFactList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondFact!=null) CondFact.accept(visitor);
        if(AndCondFactList!=null) AndCondFactList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
        if(AndCondFactList!=null) AndCondFactList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        if(AndCondFactList!=null) AndCondFactList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionTermClass(\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AndCondFactList!=null)
            buffer.append(AndCondFactList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionTermClass]");
        return buffer.toString();
    }
}
