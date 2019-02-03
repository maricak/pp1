// generated with ast extension for cup
// version 0.8
// 3/1/2019 21:1:15


package rs.ac.bg.etf.pp1.ast;

public class AssignConstantListEnd extends AssignConstList {

    private AssignConst AssignConst;

    public AssignConstantListEnd (AssignConst AssignConst) {
        this.AssignConst=AssignConst;
        if(AssignConst!=null) AssignConst.setParent(this);
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
        if(AssignConst!=null) AssignConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AssignConst!=null) AssignConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AssignConst!=null) AssignConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignConstantListEnd(\n");

        if(AssignConst!=null)
            buffer.append(AssignConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignConstantListEnd]");
        return buffer.toString();
    }
}
