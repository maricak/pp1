// generated with ast extension for cup
// version 0.8
// 3/1/2019 1:43:23


package rs.ac.bg.etf.pp1.ast;

public class OptionalAssgnNumberClass extends OptionalAssignNum {

    private AssignNum AssignNum;

    public OptionalAssgnNumberClass (AssignNum AssignNum) {
        this.AssignNum=AssignNum;
        if(AssignNum!=null) AssignNum.setParent(this);
    }

    public AssignNum getAssignNum() {
        return AssignNum;
    }

    public void setAssignNum(AssignNum AssignNum) {
        this.AssignNum=AssignNum;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AssignNum!=null) AssignNum.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AssignNum!=null) AssignNum.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AssignNum!=null) AssignNum.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptionalAssgnNumberClass(\n");

        if(AssignNum!=null)
            buffer.append(AssignNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptionalAssgnNumberClass]");
        return buffer.toString();
    }
}
