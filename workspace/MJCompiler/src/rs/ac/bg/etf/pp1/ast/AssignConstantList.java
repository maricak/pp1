// generated with ast extension for cup
// version 0.8
// 3/1/2019 16:21:16


package rs.ac.bg.etf.pp1.ast;

public class AssignConstantList extends AssignConstList {

    private AssignConstList AssignConstList;
    private AssignConst AssignConst;

    public AssignConstantList (AssignConstList AssignConstList, AssignConst AssignConst) {
        this.AssignConstList=AssignConstList;
        if(AssignConstList!=null) AssignConstList.setParent(this);
        this.AssignConst=AssignConst;
        if(AssignConst!=null) AssignConst.setParent(this);
    }

    public AssignConstList getAssignConstList() {
        return AssignConstList;
    }

    public void setAssignConstList(AssignConstList AssignConstList) {
        this.AssignConstList=AssignConstList;
    }

    public AssignConst getAssignConst() {
        return AssignConst;
    }

    public void setAssignConst(AssignConst AssignConst) {
        this.AssignConst=AssignConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AssignConstList!=null) AssignConstList.accept(visitor);
        if(AssignConst!=null) AssignConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AssignConstList!=null) AssignConstList.traverseTopDown(visitor);
        if(AssignConst!=null) AssignConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AssignConstList!=null) AssignConstList.traverseBottomUp(visitor);
        if(AssignConst!=null) AssignConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignConstantList(\n");

        if(AssignConstList!=null)
            buffer.append(AssignConstList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignConst!=null)
            buffer.append(AssignConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignConstantList]");
        return buffer.toString();
    }
}
