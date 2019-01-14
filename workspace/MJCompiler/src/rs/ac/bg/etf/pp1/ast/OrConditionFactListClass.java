// generated with ast extension for cup
// version 0.8
// 14/0/2019 23:0:37


package rs.ac.bg.etf.pp1.ast;

public class OrConditionFactListClass extends OrCondFactList {

    private OrCondFactList OrCondFactList;
    private CondFact CondFact;

    public OrConditionFactListClass (OrCondFactList OrCondFactList, CondFact CondFact) {
        this.OrCondFactList=OrCondFactList;
        if(OrCondFactList!=null) OrCondFactList.setParent(this);
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
    }

    public OrCondFactList getOrCondFactList() {
        return OrCondFactList;
    }

    public void setOrCondFactList(OrCondFactList OrCondFactList) {
        this.OrCondFactList=OrCondFactList;
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OrCondFactList!=null) OrCondFactList.accept(visitor);
        if(CondFact!=null) CondFact.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OrCondFactList!=null) OrCondFactList.traverseTopDown(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OrCondFactList!=null) OrCondFactList.traverseBottomUp(visitor);
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OrConditionFactListClass(\n");

        if(OrCondFactList!=null)
            buffer.append(OrCondFactList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OrConditionFactListClass]");
        return buffer.toString();
    }
}
