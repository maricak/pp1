// generated with ast extension for cup
// version 0.8
// 3/1/2019 15:5:32


package rs.ac.bg.etf.pp1.ast;

public class ConditionClass extends Condition {

    private CondTerm CondTerm;
    private OrCondFactList OrCondFactList;

    public ConditionClass (CondTerm CondTerm, OrCondFactList OrCondFactList) {
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
        this.OrCondFactList=OrCondFactList;
        if(OrCondFactList!=null) OrCondFactList.setParent(this);
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public OrCondFactList getOrCondFactList() {
        return OrCondFactList;
    }

    public void setOrCondFactList(OrCondFactList OrCondFactList) {
        this.OrCondFactList=OrCondFactList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTerm!=null) CondTerm.accept(visitor);
        if(OrCondFactList!=null) OrCondFactList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
        if(OrCondFactList!=null) OrCondFactList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        if(OrCondFactList!=null) OrCondFactList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionClass(\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OrCondFactList!=null)
            buffer.append(OrCondFactList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionClass]");
        return buffer.toString();
    }
}
